(ns tictactoe.cljs.board-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.cljs.board :as board]))

(describe "Board creation"
          (it "should create an empty board of specified size"
              (let [board (board/init-board 3)]
                (should= 3 (count board))
                (should= 3 (count (first board)))
                (should (every? #(= \space %) (flatten board))))))

(describe "Move mechanics"
          (context "Move validation"
            (it "should validate moves within board boundaries"
                (let [board (board/init-board 3)]
                  (should (board/valid-move? board 0 0))
                  (should (board/valid-move? board 2 2))
                  (should-not (board/valid-move? board -1 0))
                  (should-not (board/valid-move? board 0 3))
                  (should-not (board/valid-move? board 3 3))))

            (it "should only allow moves on empty cells"
                (let [board (board/place-mark (board/init-board 3) 1 1 \X)]
                  (should (board/valid-move? board 0 0))
                  (should-not (board/valid-move? board 1 1)))))

          (context "Move execution"
            (it "should correctly place marks on the board"
                (let [board (board/init-board 3)
                      board (board/place-mark board 1 1 \X)]
                  (should= \X (get-in board [1 1]))
                  (should= \space (get-in board [0 0]))))))

(describe "Victory conditions"
          (context "Standard win patterns"
            (it "should detect horizontal wins"
                (let [board [[\X \X \X]
                             [\O \O \space]
                             [\space \space \space]]]
                  (should (board/check-winner board \X 3))
                  (should-not (board/check-winner board \O 3))))

            (it "should detect vertical wins"
                (let [board [[\X \O \space]
                             [\X \O \space]
                             [\X \space \space]]]
                  (should (board/check-winner board \X 3))
                  (should-not (board/check-winner board \O 3))))

            (it "should detect diagonal wins"
                (let [board [[\X \O \space]
                             [\O \X \space]
                             [\space \space \X]]]
                  (should (board/check-winner board \X 3))
                  (should-not (board/check-winner board \O 3))))

            (it "should detect anti-diagonal wins"
                (let [board [[\space \O \X]
                             [\O \X \space]
                             [\X \space \space]]]
                  (should (board/check-winner board \X 3))
                  (should-not (board/check-winner board \O 3)))))

          (context "Extended win patterns"
            (it "should detect wins with longer sequences"
                (let [board (-> (board/init-board 9)
                                (board/place-mark 2 2 \X)
                                (board/place-mark 3 3 \X)
                                (board/place-mark 4 4 \X)
                                (board/place-mark 5 5 \X)
                                (board/place-mark 6 6 \X))]
                  (should (board/check-winner board \X 5))
                  (should-not (board/check-winner board \O 5))))

            (it "should detect off-diagonal wins"
                (let [board (-> (board/init-board 9)
                                (board/place-mark 2 6 \X)
                                (board/place-mark 3 5 \X)
                                (board/place-mark 4 4 \X)
                                (board/place-mark 5 3 \X)
                                (board/place-mark 6 2 \X))]
                  (should (board/check-winner board \X 5))
                  (should-not (board/check-winner board \O 5))))))

(describe "Game state"
          (it "should correctly identify a full board"
              (let [board [[\X \O \X]
                           [\X \O \O]
                           [\O \X \X]]]
                (should (board/board-full? board))))

          (it "should correctly identify a non-full board"
              (let [board [[\X \O \X]
                           [\X \O \O]
                           [\O \X \space]]]
                (should-not (board/board-full? board)))))

(run-specs)