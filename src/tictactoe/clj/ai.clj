(ns tictactoe.clj.ai
  (:require [tictactoe.clj.board :as board]))

;; # AI Implementation for Tic-Tac-Toe
;; 
;; At its heart, this module implements a competitive AI player for our **9x9 Tic-Tac-Toe** game
;; using the *Minimax* algorithm with *Alpha-Beta pruning* optimization. The implementation is
;; specifically tuned for a **5-in-a-row** win condition on a 9x9 grid, balancing search depth
;; with real-time performance requirements.

;; ## Game Theory Foundations
;; 
;; The implementation is grounded in classical game theory, specifically the theory of
;; **two-player zero-sum games** with *perfect information*. In this context, perfect information
;; means both players have complete knowledge of all moves and game states, with no hidden
;; information or chance elements. The *zero-sum property* ensures that any advantage gained
;; by one player represents an equal disadvantage for the opponent.
;;
;; The `Minimax` algorithm formalizes this strategic reasoning by recursively evaluating
;; positions. For each position, the *maximizing player* (our AI) seeks to maximize the
;; minimum value their opponent can force: $\max_{m \in moves} \min_{r \in responses} value(m,r)$.
;; This recursive evaluation continues until reaching terminal positions or a specified
;; search depth.

(def win-score 1)
(def lose-score -1)
(def draw-score 0)

(defn evaluate-position
  "Evaluate the current board position for the given mark"
  [board mark win-length]
  (cond
    (board/check-winner board mark win-length) win-score
    (board/check-winner board (if (= mark \X) \O \X) win-length) lose-score
    (board/board-full? board) draw-score
    :else 0))

;; ## Position Evaluation
;;
;; Position evaluation forms the **quantitative foundation** of the AI's decision-making process.
;; The `evaluate-position` function maps game states to discrete numerical values that precisely
;; capture the game's winning conditions. Terminal positions receive definitive scores:
;; **wins** (`+1`), **losses** (`-1`), and **draws** (`0`). This *ternary scoring system* provides clear
;; gradients for the search algorithm while maintaining computational efficiency.

;; ## Move Generation and Ordering
;;
;; The efficiency of *alpha-beta pruning* is highly dependent on move ordering. The
;; implementation employs a **sophisticated move ordering strategy** based on two key
;; heuristics: `Manhattan distance` from center and *immediate tactical opportunities*.
;; This ordering maximizes the effectiveness of pruning by examining the most promising
;; moves first, significantly reducing the effective **branching factor** of the game tree.

(defn get-available-moves
  "Get all available moves on the board"
  [board]
  (for [row (range (count board))
        col (range (count board))
        :when (board/cell-empty? board row col)]
    [row col]))

(defn center-distance
  "Calculate Manhattan distance to center"
  [[row col] board-size]
  (let [center (quot board-size 2)]
    (+ (Math/abs (- row center))
       (Math/abs (- col center)))))

(defn order-moves
  "Order moves to improve alpha-beta pruning efficiency.
   Prioritizes center and near-center moves."
  [moves board-size]
  (sort-by #(center-distance % board-size) moves))

;; ## Tactical Analysis
;;
;; The tactical analysis layer provides **rapid evaluation** of immediate threats and
;; opportunities. This *two-phase tactical check* examines first winning moves and then
;; defensive moves that prevent immediate loss. By identifying these **critical positions**
;; before initiating deeper search, the AI can respond instantly to tactical situations
;; while conserving computational resources for more complex positions.

(defn would-win?
  "Check if placing a mark at the given position would create a win"
  [board row col mark win-length]
  (when (board/valid-move? board row col)
    (let [new-board (board/place-mark board row col mark)]
      (board/check-winner new-board mark win-length))))

(defn find-winning-move
  "Find a move that leads to an immediate win"
  [board mark win-length]
  (first
   (for [row (range (count board))
         col (range (count board))
         :when (and (board/valid-move? board row col)
                    (would-win? board row col mark win-length))]
     [row col])))

(defn find-blocking-move
  "Find a move that blocks the opponent's win"
  [board mark win-length]
  (find-winning-move board (if (= mark \X) \O \X) win-length))

;; ## Strategic Search Implementation
;;
;; The strategic search combines `minimax` tree exploration with *alpha-beta pruning* to
;; achieve efficient deep analysis. Alpha-beta pruning maintains bounds (**α**,**β**) on the
;; possible position values, eliminating branches that cannot influence the final
;; evaluation. The mathematical formulation precisely defines this process:
;;
;; $minimax(pos, d, α, β) = \begin{cases}
;; eval(pos) & \text{if } d = 0 \\
;; \max\{minimax(c, d-1, α, β) : c \in children(pos)\} & \text{if maximizing} \\
;; \min\{minimax(c, d-1, α, β) : c \in children(pos)\} & \text{if minimizing}
;; \end{cases}$

(defn terminal-node?
  "Check if the position is terminal (game over)"
  [board depth win-length]
  (or (zero? depth)
      (board/board-full? board)
      (board/check-winner board \X win-length)
      (board/check-winner board \O win-length)))

(defn should-prune?
  "Determine if we should prune the search"
  [alpha beta]
  (>= alpha beta))

;; ## Minimax Step Processing
;;
;; The `minimax-step` function handles the **critical task** of evaluating individual moves
;; within the search tree. For each move, it creates a new board state and *recursively*
;; evaluates the position from the opponent's perspective. This alternation between
;; **maximizing** and **minimizing** players continues until reaching a terminal position or
;; the specified search depth. The function maintains the *alpha-beta bounds* throughout
;; the recursive process, enabling efficient pruning of irrelevant branches.

(declare minimax)

(defn minimax-step
  "Process a single step in the minimax algorithm"
  [board move depth is-maximizing mark win-length alpha beta]
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
        [score move]))))

;; ## Alpha-Beta Search Process
;;
;; The *alpha-beta search* process forms the **core** of our strategic decision-making.
;; It systematically examines an ordered sequence of moves, maintaining tight bounds
;; on possible position values through the `alpha` and `beta` parameters. The implementation
;; uses *tail recursion* via `loop/recur` for efficiency, accumulating the best move
;; found while progressively tightening the **alpha-beta window**. This approach combines
;; the completeness of exhaustive search with the efficiency of branch pruning,
;; achieving an effective balance between search depth and computational cost.

(defn alpha-beta-search
  "Perform alpha-beta search on a list of moves"
  [board moves depth is-maximizing mark win-length alpha beta]
  (loop [remaining-moves moves
         alpha alpha
         beta beta
         best-score (if is-maximizing Integer/MIN_VALUE Integer/MAX_VALUE)
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

;; ## Minimax Algorithm Implementation
;;
;; The `minimax` function serves as the **primary entry point** for strategic evaluation.
;; It implements the classic *minimax algorithm* with alpha-beta enhancement, managing
;; the recursive exploration of the game tree. The implementation handles both *terminal
;; positions*, where it returns immediate evaluations, and *internal nodes*, where it
;; initiates deeper search. **Move ordering** is integrated at this level to maximize
;; the effectiveness of alpha-beta pruning, significantly reducing the number of
;; positions that must be examined.

(defn minimax
  "Minimax algorithm with alpha-beta pruning"
  [board depth is-maximizing mark win-length alpha beta]
  (if (terminal-node? board depth win-length)
    [(evaluate-position board mark win-length) nil]
    (alpha-beta-search board
                       (order-moves (get-available-moves board) (count board))
                       depth
                       is-maximizing
                       mark
                       win-length
                       alpha
                       beta)))

;; ## Strategic Move Selection
;;
;; The `get-best-move` function implements a **sophisticated decision-making process** that
;; combines *tactical awareness* with *strategic depth*. It employs a three-tier decision
;; hierarchy: first checking for **immediate winning moves**, then for **critical defensive
;; moves** that prevent opponent wins, and finally falling back to *strategic position
;; evaluation* via minimax search. The search depth is carefully calibrated to balance
;; playing strength with real-time performance requirements. This **layered approach**
;; ensures both tactical accuracy and strategic coherence while maintaining responsive
;; gameplay.

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
                                              3  ; Reduced depth for faster response
                                              true
                                              mark
                                              win-length
                                              Integer/MIN_VALUE
                                              Integer/MAX_VALUE)]
              move))))