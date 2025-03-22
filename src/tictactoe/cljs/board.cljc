(ns tictactoe.cljs.board)

(defn init-board
  "Initialize an empty board of the specified size."
  [size]
  (vec (repeat size (vec (repeat size \space)))))

(defn valid-move?
  "Check if a move is valid (within bounds and space is empty)."
  [board row col]
  (and (< -1 row (count board))
       (< -1 col (count board))
       (= \space (get-in board [row col]))))

(defn place-mark
  "Place a mark on the board at the specified position."
  [board row col mark]
  (assoc-in board [row col] mark))

(defn board-full?
  "Check if the board is completely filled."
  [board]
  (not-any? #(some #{\space} %) board))

(defn get-row
  "Get a specific row from the board."
  [board row]
  (nth board row))

(defn get-col
  "Get a specific column from the board."
  [board col]
  (mapv #(nth % col) board))

(defn get-diagonal
  "Get a diagonal line starting from [row col] in the specified direction."
  [board row col [dx dy]]
  (let [size (count board)]
    (loop [r row, c col, result []]
      (if (or (< r 0) (>= r size)
              (< c 0) (>= c size))
        result
        (recur (+ r dx) (+ c dy)
               (conj result (get-in board [r c])))))))

(defn check-line
  "Check if a line contains a winning sequence."
  [line player win-length]
  (let [len (count line)]
    (loop [start 0]
      (if (> (+ start win-length) len)
        false
        (if (every? #(= player %) (subvec line start (+ start win-length)))
          true
          (recur (inc start)))))))

(defn check-winner
  "Check if the specified player has won."
  [board player win-length]
  (let [size (count board)]
    (or
     ;; Check rows
     (some #(check-line (get-row board %) player win-length)
           (range size))
     ;; Check columns
     (some #(check-line (get-col board %) player win-length)
           (range size))
     ;; Check diagonals
     (some #(let [[row col] %]
              (or (check-line (get-diagonal board row col [1 1]) player win-length)
                  (check-line (get-diagonal board row col [1 -1]) player win-length)))
           (for [i (range size)
                 j (range size)]
             [i j])))))

(defn winner?
  "Check if there is a winner on the board."
  [board]
  (let [win-length 5]
    (or (check-winner board \X win-length)
        (check-winner board \O win-length))))