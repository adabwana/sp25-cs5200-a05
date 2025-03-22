;; # Board Management Module
;;
;; This module implements the **core game board mechanics** for a scalable Tic-Tac-Toe game.
;; It provides fundamental operations for an *n×n grid* system, supporting arbitrary board
;; sizes and configurable win conditions. The implementation prioritizes immutability and
;; functional purity, following Clojure's design principles.

(ns tictactoe.clj.board)

;; ## Board Initialization and Validation
;;
;; The board is represented as a **nested vector structure**, providing $O(1)$ access time
;; for any position. The implementation uses Clojure's persistent vectors to maintain
;; immutability while ensuring efficient updates.

(defn init-board
  "Initialize an empty board of size n x n.
   Returns a nested vector structure representing the game board."
  [n]
  (vec (repeat n (vec (repeat n \space)))))

;; ### Coordinate Validation
;;
;; These functions implement the *boundary checking system* for board operations.
;; They ensure all moves remain within the valid game space and maintain game integrity.

(defn valid-coordinates?
  "Check if the given coordinates are within the board boundaries.
   Uses zero-based indexing where (0,0) is the top-left corner."
  [board row col]
  (and (< -1 row (count board))
       (< -1 col (count board))))

(defn cell-empty?
  "Check if the given cell is empty.
   Empty cells are represented by the space character."
  [board row col]
  (= \space (get-in board [row col])))

(defn valid-move?
  "Check if a move is valid (within bounds and cell is empty).
   Combines boundary and emptiness checks for move validation."
  [board row col]
  (and (valid-coordinates? board row col)
       (cell-empty? board row col)))

;; ## Board Manipulation
;;
;; The board manipulation system implements *immutable state transitions*
;; for the game board. Each operation returns a new board state rather
;; than modifying the existing one.

(defn place-mark
  "Place a mark (X or O) at the specified position on the board if the move is valid.
   Returns the new board state, or the unchanged board if the move is invalid."
  [board row col mark]
  (if (valid-move? board row col)
    (assoc-in board [row col] mark)
    board))

;; ## Win Detection System
;;
;; The win detection system implements an efficient **line-checking algorithm**
;; that supports arbitrary win lengths and board sizes. It uses vector-based
;; position generation and validation to check for winning conditions.

;; ### Line Position Generation
;;
;; The *line generation subsystem* creates sequences of positions to check
;; for winning combinations. It uses delta-based calculations to support
;; all possible winning directions.

(defn generate-line-positions
  "Generate a sequence of positions for a line given start position and deltas.
   Uses vector arithmetic to calculate each position in the sequence:
   pos[i] = start + i * delta"
  [start-row start-col length row-delta col-delta]
  (for [i (range length)]
    [(+ start-row (* i row-delta))
     (+ start-col (* i col-delta))]))

(defn check-line
  "Check if all positions in a line contain the same mark.
   Implements efficient sequential checking using Clojure's every? function."
  [board positions mark]
  (every? #(= mark (get-in board %)) positions))

(defn valid-line?
  "Check if a line of positions is valid within the board boundaries.
   Validates both start and end positions using delta-based calculations:
   end_pos = start_pos + (length-1) * delta"
  [board-size start-row start-col length row-delta col-delta]
  (let [end-row (+ start-row (* (dec length) row-delta))
        end-col (+ start-col (* (dec length) col-delta))]
    (and (<= 0 start-row end-row (dec board-size))
         (if (pos? col-delta)
           (<= 0 start-col end-col (dec board-size))
           (<= 0 end-col start-col (dec board-size))))))

;; ### Directional Win Detection
;;
;; The *directional win checking system* implements a flexible algorithm
;; for detecting wins in any direction. It uses delta-based movement to
;; check horizontal, vertical, and diagonal win conditions.

(defn check-direction-win
  "Generic function to check for wins in any direction.
   Uses delta-based movement to scan the board:
   - row_delta: vertical movement (-1, 0, 1)
   - col_delta: horizontal movement (-1, 0, 1)
   
   Time complexity: O(n²) where n is the board size
   Space complexity: O(k) where k is the win length"
  [board mark win-length row-delta col-delta]
  (let [size (count board)]
    (some (fn [row]
            (some (fn [col]
                    (when (valid-line? size row col win-length row-delta col-delta)
                      (let [positions (generate-line-positions row col win-length row-delta col-delta)]
                        (check-line board positions mark))))
                  (range size)))
          (range size))))

(defn check-horizontal-win
  "Check for a win in horizontal rows.
   Optimized case of directional win with row_delta=0, col_delta=1"
  [board mark win-length]
  (check-direction-win board mark win-length 0 1))

(defn check-vertical-win
  "Check for a win in vertical columns.
   Optimized case of directional win with row_delta=1, col_delta=0"
  [board mark win-length]
  (check-direction-win board mark win-length 1 0))

(defn check-diagonal-win
  "Check for a win in diagonals.
   Checks both main diagonal (↘) and anti-diagonal (↙) directions"
  [board mark win-length]
  (or (check-direction-win board mark win-length 1 1)      ; Main diagonal
      (check-direction-win board mark win-length 1 -1)))   ; Anti-diagonal

;; ## Game State Analysis
;;
;; These functions provide **high-level game state analysis** capabilities,
;; integrating the directional checking system to determine game outcomes.

(defn check-winner
  "Check if the given mark has won.
   Integrates all directional checks to determine if a win condition exists."
  [board mark win-length]
  (or (check-horizontal-win board mark win-length)
      (check-vertical-win board mark win-length)
      (check-diagonal-win board mark win-length)))

(defn board-full?
  "Check if the board is completely filled.
   Uses efficient sequence operations to check for any remaining empty spaces.
   
   Time complexity: O(n²) where n is the board size
   Space complexity: O(1) using lazy sequence evaluation"
  [board]
  (not-any? #(= \space %) (flatten board))) 