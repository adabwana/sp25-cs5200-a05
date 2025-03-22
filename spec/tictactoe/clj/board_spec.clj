(ns tictactoe.clj.board-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.clj.board :refer :all]))

(describe "Board Operations"
          (context "Board initialization"
            (it "should create an empty board of specified size"
                (let [board (init-board 3)]
                  (should= 3 (count board))
                  (should= 3 (count (first board)))
                  (should (every? #(= \space %) (flatten board))))))

          (context "Coordinate validation"
            (it "should validate coordinates within bounds"
                (let [board (init-board 3)]
                  (should (valid-coordinates? board 0 0))
                  (should (valid-coordinates? board 2 2))
                  (should-not (valid-coordinates? board -1 0))
                  (should-not (valid-coordinates? board 0 3))
                  (should-not (valid-coordinates? board 3 3))))

            (it "should check if cell is empty"
                (let [board (place-mark (init-board 3) 1 1 \X)]
                  (should (cell-empty? board 0 0))
                  (should-not (cell-empty? board 1 1)))))

          (context "Move validation and placement"
            (it "should validate moves"
                (let [board (place-mark (init-board 3) 1 1 \X)]
                  (should (valid-move? board 0 0))
                  (should-not (valid-move? board 1 1))
                  (should-not (valid-move? board 3 3))))

            (it "should place marks correctly"
                (let [board (init-board 3)
                      board (place-mark board 1 1 \X)]
                  (should= \X (get-in board [1 1]))
                  (should= \space (get-in board [0 0])))))

          (context "Line generation and validation"
            (it "should generate correct line positions"
                (let [positions (generate-line-positions 0 0 3 1 1)]
                  (should= [[0 0] [1 1] [2 2]] positions)))

            (it "should validate lines within board"
                (let [board-size 3]
                  (should (valid-line? board-size 0 0 3 1 1))      ; diagonal
                  (should (valid-line? board-size 0 0 3 0 1))      ; horizontal
                  (should (valid-line? board-size 0 0 3 1 0))      ; vertical
                  (should-not (valid-line? board-size 0 0 4 1 1))  ; too long
                  (should-not (valid-line? board-size 2 2 3 1 1)))))  ; out of bounds


          (context "Win detection"
            (it "should detect horizontal wins"
                (let [board [[\X \X \X]
                             [\O \O \space]
                             [\space \space \space]]]
                  (should (check-horizontal-win board \X 3))
                  (should-not (check-horizontal-win board \O 3))))

            (it "should detect vertical wins"
                (let [board [[\X \O \space]
                             [\X \O \space]
                             [\X \space \space]]]
                  (should (check-vertical-win board \X 3))
                  (should-not (check-vertical-win board \O 3))))

            (it "should detect main diagonal wins"
                (let [board [[\X \O \space]
                             [\O \X \space]
                             [\space \space \X]]]
                  (should (check-diagonal-win board \X 3))
                  (should-not (check-diagonal-win board \O 3))))

            (it "should detect anti-diagonal wins"
                (let [board [[\space \O \X]
                             [\O \X \space]
                             [\X \space \space]]]
                  (should (check-diagonal-win board \X 3))
                  (should-not (check-diagonal-win board \O 3))))

            (it "should detect wins with longer sequences"
                (let [board (-> (init-board 9)
                                (place-mark 2 2 \X)
                                (place-mark 3 3 \X)
                                (place-mark 4 4 \X)
                                (place-mark 5 5 \X)
                                (place-mark 6 6 \X))]
                  (should (check-diagonal-win board \X 5))
                  (should-not (check-diagonal-win board \O 5))))

            (it "should detect off-diagonal wins"
                (let [board (-> (init-board 9)
                                (place-mark 2 6 \X)
                                (place-mark 3 5 \X)
                                (place-mark 4 4 \X)
                                (place-mark 5 3 \X)
                                (place-mark 6 2 \X))]
                  (should (check-diagonal-win board \X 5))
                  (should-not (check-diagonal-win board \O 5)))))

          (context "Board state"
            (it "should detect full board"
                (let [board [[\X \O \X]
                             [\X \O \O]
                             [\O \X \X]]]
                  (should (board-full? board)))
                (let [board [[\X \O \X]
                             [\X \O \O]
                             [\O \X \space]]]
                  (should-not (board-full? board))))))

(run-specs) 