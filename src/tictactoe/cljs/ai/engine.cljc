(ns tictactoe.cljs.ai.engine
  (:require
   [tictactoe.cljs.board :as board]
   [tictactoe.cljs.config.settings :as settings]
   [tictactoe.cljs.ai.strategies :as strat]))

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

(defn- minimax-step
  "Process a single step in the minimax algorithm"
  [board move depth is-maximizing mark win-length alpha beta]
  (let [[row col] move
        opponent (if (= mark \X) \O \X)
        current-mark (if is-maximizing mark opponent)
        new-board (board/place-mark board row col current-mark)]
    (if (terminal-node? new-board depth win-length)
      [(strat/evaluate-position new-board mark win-length) move]
      (let [[score _] (minimax board
                               (dec depth)
                               (not is-maximizing)
                               mark
                               win-length
                               alpha
                               beta)]
        [score move]))))

(defn- alpha-beta-search
  "Perform alpha-beta search on a list of moves"
  [board moves depth is-maximizing mark win-length alpha beta]
  (let [{:keys [infinity neg-infinity]} (settings/get-config [:ai])]
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
                 new-move))))))

(defn- minimax
  "Minimax algorithm with alpha-beta pruning"
  [board depth is-maximizing mark win-length alpha beta]
  (if (terminal-node? board depth win-length)
    [(strat/evaluate-position board mark win-length) nil]
    (alpha-beta-search board
                       (strat/order-moves (strat/get-available-moves board) (count board))
                       depth
                       is-maximizing
                       mark
                       win-length
                       alpha
                       beta)))

(defn get-best-move
  "Get the best move for the current board position"
  [board mark win-length]
  (let [{:keys [max-depth infinity neg-infinity]} (settings/get-config [:ai])
        winning-move (strat/find-winning-move board mark win-length)
        blocking-move (strat/find-blocking-move board mark win-length)]
    (cond
      ;; If we can win, take it
      winning-move winning-move
      ;; If opponent can win, block it
      blocking-move blocking-move
      ;; Otherwise use minimax with alpha-beta pruning
      :else (let [[_ move] (alpha-beta-search board
                                              (strat/order-moves (strat/get-available-moves board) (count board))
                                              max-depth
                                              true
                                              mark
                                              win-length
                                              neg-infinity
                                              infinity)]
              move))))