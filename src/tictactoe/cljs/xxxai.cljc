(ns tictactoe.cljs.xxxai
  (:require
   [tictactoe.cljs.board :as board]))

(def ^:private max-depth 3)
(def ^:private infinity 1000000)
(def ^:private neg-infinity -1000000)

(def win-score 1)
(def lose-score -1)
(def draw-score 0)

;; Memoized evaluation function
(def evaluate-position
  (memoize
   (fn [board mark win-length]
     (cond
       (board/check-winner board mark win-length) win-score
       (board/check-winner board (if (= mark \X) \O \X) win-length) lose-score
       (board/board-full? board) draw-score
       :else 0))))

(defn- get-available-moves
  "Get all available moves on the board using transients for better performance"
  [board]
  (persistent!
   (reduce
    (fn [moves row]
      (reduce
       (fn [acc col]
         (if (board/valid-move? board row col)
           (conj! acc [row col])
           acc))
       moves
       (range (count board))))
    (transient [])
    (range (count board)))))

;; Memoized center distance calculation
(def center-distance
  (memoize
   (fn [[row col] board-size]
     (let [center (quot board-size 2)]
       (+ (Math/abs (- row center))
          (Math/abs (- col center)))))))

;; Cache for move scoring
(def ^:private move-order-cache (atom {}))

(defn- calculate-move-scores
  "Pre-calculate scores for all possible moves on a board size using transients"
  [board-size]
  (persistent!
   (reduce
    (fn [scores pos]
      (let [[row col] pos
            center (quot board-size 2)
            dist (center-distance pos board-size)
            is-center? (= pos [center center])
            is-corner? (and (or (= row 0) (= row (dec board-size)))
                            (or (= col 0) (= col (dec board-size))))]
        (assoc! scores pos
                (cond
                  is-center? 0
                  is-corner? (inc dist)
                  :else (+ dist 2)))))
    (transient {})
    (for [row (range board-size)
          col (range board-size)]
      [row col]))))

;; Memoized move scoring
(def get-move-scores
  (memoize
   (fn [board-size]
     (if-let [scores (get @move-order-cache board-size)]
       scores
       (let [scores (calculate-move-scores board-size)]
         (swap! move-order-cache assoc board-size scores)
         scores)))))

(defn- order-moves
  "Order moves to improve alpha-beta pruning efficiency"
  [moves board-size]
  (let [scores (get-move-scores board-size)]
    (sort-by #(get scores %) moves)))

(defn- would-win?
  "Check if placing a mark at the given position would create a win"
  [board row col mark win-length]
  (when (board/valid-move? board row col)
    (let [new-board (board/place-mark board row col mark)]
      (board/check-winner new-board mark win-length))))

(def find-winning-move
  (memoize
   (fn [board mark win-length]
     (let [board-size (count board)
           scores (get-move-scores board-size)]
       (first
        (for [[row col] (sort-by #(get scores %)
                                 (for [row (range board-size)
                                       col (range board-size)
                                       :when (board/valid-move? board row col)]
                                   [row col]))
              :when (would-win? board row col mark win-length)]
          [row col]))))))

(defn- find-blocking-move
  "Find a move that blocks the opponent's win"
  [board mark win-length]
  (find-winning-move board (if (= mark \X) \O \X) win-length))

(defn- terminal-node?
  "Check if the position is terminal (game over)"
  [board depth win-length]
  (or (zero? depth)
      (board/board-full? board)
      (board/check-winner board \X win-length)
      (board/check-winner board \O win-length)))

(defn- should-prune?
  "Determine if we should prune the search"
  [alpha beta]
  (>= alpha beta))

(declare minimax)

(def minimax-step
  (memoize
   (fn [board move depth is-maximizing mark win-length alpha beta]
     (let [[row col] move
           opponent (if (= mark \X) \O \X)
           current-mark (if is-maximizing mark opponent)
           new-board (board/place-mark board row col current-mark)]
       (if (terminal-node? new-board depth win-length)
         [(evaluate-position new-board mark win-length) move]
         (let [[score _] (minimax board
                                  (dec depth)
                                  (not is-maximizing)
                                  mark
                                  win-length
                                  alpha
                                  beta)]
           [score move]))))))

(defn- alpha-beta-search
  "Perform alpha-beta search on a list of moves"
  [board moves depth is-maximizing mark win-length alpha beta]
  (loop [remaining-moves moves
         alpha alpha
         beta beta
         best-score (if is-maximizing neg-infinity infinity)
         best-move nil]
    (if (or (empty? remaining-moves) (should-prune? alpha beta))
      [best-score best-move]
      (let [move (first remaining-moves)
            [score final-move] (minimax-step board
                                             move
                                             depth
                                             is-maximizing
                                             mark
                                             win-length
                                             alpha
                                             beta)
            [new-score new-move] (if (if is-maximizing (> score best-score) (< score best-score))
                                   [score final-move]
                                   [best-score best-move])
            new-alpha (if is-maximizing (max alpha score) alpha)
            new-beta (if is-maximizing beta (min beta score))]
        (recur (rest remaining-moves)
               new-alpha
               new-beta
               new-score
               new-move)))))

(def minimax
  (memoize
   (fn [board depth is-maximizing mark win-length alpha beta]
     (if (terminal-node? board depth win-length)
       [(evaluate-position board mark win-length) nil]
       (alpha-beta-search board
                          (order-moves (get-available-moves board) (count board))
                          depth
                          is-maximizing
                          mark
                          win-length
                          alpha
                          beta)))))

(defn get-best-move
  "Get the best move for the current board position"
  [board mark win-length]
  (let [winning-move (find-winning-move board mark win-length)
        blocking-move (find-blocking-move board mark win-length)]
    (cond
      ;; If we can win, take it
      winning-move winning-move
      ;; If opponent can win, block it
      blocking-move blocking-move
      ;; Otherwise use minimax with alpha-beta pruning
      :else (let [[_ move] (alpha-beta-search board
                                              (order-moves (get-available-moves board) (count board))
                                              max-depth
                                              true
                                              mark
                                              win-length
                                              neg-infinity
                                              infinity)]
              move))))