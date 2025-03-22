(ns tictactoe.cljs.ai.strategies
  (:require
   [tictactoe.cljs.board :as board]
   [tictactoe.cljs.config.settings :as settings]))

;; ## Score Evaluation

(def evaluate-position
  (memoize
   (fn [board mark win-length]
     (let [{:keys [win-score lose-score draw-score]}
           (settings/get-config [:ai])]
       (cond
         (board/check-winner board mark win-length) win-score
         (board/check-winner board (if (= mark \X) \O \X) win-length) lose-score
         (board/board-full? board) draw-score
         :else 0)))))

;; ## Move Generation

(defn get-available-moves
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

;; ## Move Scoring

(def center-distance
  (memoize
   (fn [[row col] board-size]
     (let [center (quot board-size 2)]
       (+ (Math/abs (- row center))
          (Math/abs (- col center)))))))

;; Cache for move scoring
(def move-scores-cache (atom {}))

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

(def get-move-scores
  (memoize
   (fn [board-size]
     (if-let [scores (get @move-scores-cache board-size)]
       scores
       (let [scores (calculate-move-scores board-size)]
         (swap! move-scores-cache assoc board-size scores)
         scores)))))

(defn order-moves
  "Order moves to improve alpha-beta pruning efficiency"
  [moves board-size]
  (let [scores (get-move-scores board-size)]
    (sort-by #(get scores %) moves)))

;; ## Winning Move Detection

(defn would-win?
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

(defn find-blocking-move
  "Find a move that blocks the opponent's win"
  [board mark win-length]
  (find-winning-move board (if (= mark \X) \O \X) win-length))