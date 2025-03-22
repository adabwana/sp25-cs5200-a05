(ns tictactoe.clj.core-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.clj.board :refer :all]
   [tictactoe.clj.core :refer :all]))

(describe "Board Management"
          (context "Board Initialization"
            (it "should create an empty 3x3 board"
                (let [board (init-board 3)]
                  (should= 3 (count board))
                  (should= 3 (count (first board)))
                  (should (every? #(= \space %) (flatten board)))))

            (it "should create an empty 9x9 board"
                (let [board (init-board 9)]
                  (should= 9 (count board))
                  (should= 9 (count (first board)))
                  (should (every? #(= \space %) (flatten board)))))

            (it "should validate board coordinates"
                (let [board (init-board 3)]
                  (should (valid-coordinates? board 0 0))
                  (should (valid-coordinates? board 2 2))
                  (should-not (valid-coordinates? board -1 0))
                  (should-not (valid-coordinates? board 0 3))
                  (should-not (valid-coordinates? board 3 3)))))

          (context "Move Validation"
            (it "should place a mark on an empty cell"
                (let [board (init-board 3)
                      [row col] [1 1]
                      mark \X
                      new-board (place-mark board row col mark)]
                  (should= mark (get-in new-board [row col]))))

            (it "should prevent placing a mark on an occupied cell"
                (let [board (init-board 3)
                      [row col] [1 1]
                      mark1 \X
                      mark2 \O
                      board-with-X (place-mark board row col mark1)
                      board-with-failed-O (place-mark board-with-X row col mark2)]
                  (should= mark1 (get-in board-with-failed-O [row col]))))))

(describe "Game State Management"
          (before
           (reset! game-state {:board (init-board 3)
                               :current-player \X
                               :game-over? false
                               :winner nil
                               :config {:board-size 3
                                        :win-length 3
                                        :ai-enabled false
                                        :ai-player \O}}))

          (context "Player Management"
            (it "should track current player"
                (should= \X (:current-player @game-state))
                (make-move 0 0)
                (should= \O (:current-player @game-state))
                (make-move 1 1)
                (should= \X (:current-player @game-state))))

          (context "Game State Updates"
            (it "should initialize game state correctly"
                (let [state @game-state]
                  (should= \X (:current-player state))
                  (should-not (:game-over? state))
                  (should= nil (:winner state))
                  (should= 3 (count (:board state)))))

            (it "should update game state after moves"
                (make-move 0 0)
                (should= \X (get-in @game-state [:board 0 0]))
                (should= \O (:current-player @game-state)))

            (it "should prevent moves after game over"
                (make-move 0 0) ; X
                (make-move 1 0) ; O
                (make-move 0 1) ; X
                (make-move 1 1) ; O
                (make-move 0 2) ; X wins
                (should (:game-over? @game-state))
                (make-move 2 2)  ; Should not place mark
                (should= \space (get-in @game-state [:board 2 2])))))

(describe "Win Conditions"
          (before
           (reset! game-state {:board (init-board 3)
                               :current-player \X
                               :game-over? false
                               :winner nil
                               :config {:board-size 3
                                        :win-length 3
                                        :ai-enabled false
                                        :ai-player \O}}))

          (context "3x3 Board Win Detection"
            (it "should detect horizontal wins"
                (make-move 0 0) ; X
                (make-move 1 0) ; O
                (make-move 0 1) ; X
                (make-move 1 1) ; O
                (make-move 0 2) ; X
                (should (:game-over? @game-state))
                (should= \X (:winner @game-state)))

            (it "should detect vertical wins"
                (make-move 0 0) ; X
                (make-move 0 1) ; O
                (make-move 1 0) ; X
                (make-move 1 1) ; O
                (make-move 2 0) ; X
                (should (:game-over? @game-state))
                (should= \X (:winner @game-state)))

            (it "should detect diagonal wins"
                (make-move 0 0) ; X
                (make-move 0 1) ; O
                (make-move 1 1) ; X
                (make-move 0 2) ; O
                (make-move 2 2) ; X
                (should (:game-over? @game-state))
                (should= \X (:winner @game-state)))

            (it "should detect draws"
                (make-move 0 0) ; X
                (make-move 0 1) ; O
                (make-move 0 2) ; X
                (make-move 1 1) ; O
                (make-move 1 0) ; X
                (make-move 1 2) ; O
                (make-move 2 1) ; X
                (make-move 2 0) ; O
                (make-move 2 2) ; X
                (should (:game-over? @game-state))
                (should= nil (:winner @game-state)))))

(describe "9x9 Board Functionality"
          (before
           (reset! game-state {:board (init-board 9)
                               :current-player \X
                               :game-over? false
                               :winner nil
                               :config {:board-size 9
                                        :win-length 5
                                        :ai-enabled false  ; Disable AI for these tests
                                        :ai-player \O}}))

          (context "Win Conditions"
            (it "should detect horizontal wins with 5 in a row"
                (doseq [col (range 5)]
                  (make-move 0 col)  ; X moves
                  (when (< col 4)
                    (make-move 1 col)))  ; O moves
                (should (:game-over? @game-state))
                (should= \X (:winner @game-state)))

            (it "should detect vertical wins with 5 in a row"
                (doseq [row (range 5)]
                  (make-move row 0)  ; X moves
                  (when (< row 4)
                    (make-move row 1)))  ; O moves
                (should (:game-over? @game-state))
                (should= \X (:winner @game-state)))

            (it "should detect diagonal wins with 5 in a row"
                (doseq [i (range 5)]
                  (make-move i i)  ; X moves
                  (when (< i 4)
                    (make-move i (inc i))))  ; O moves
                (should (:game-over? @game-state))
                (should= \X (:winner @game-state)))))

(describe "AI Integration"
          (it "should enable AI moves"
              (swap! game-state assoc-in [:config :ai-enabled] true)
              (make-move 4 4)  ; Player X moves
              (Thread/sleep 100)  ; Give AI time to make its move
              (should (some #(= \O %) (flatten (:board @game-state))))  ; Should find an O on the board
              (should= \X (:current-player @game-state))))  ; Should be X's turn again after AI moves

(run-specs)
