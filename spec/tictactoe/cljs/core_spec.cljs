(ns tictactoe.cljs.core-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.cljs.core :as core]
   [tictactoe.cljs.board :as board]))

(describe "Game state"
          (context "Configuration"
            (it "should have correct default configuration"
                (should= 9 (:board-size core/default-config))
                (should= 5 (:win-length core/default-config))
                (should= true (:ai-enabled core/default-config))
                (should= \O (:ai-player core/default-config))))

          (context "Player management"
            (it "should switch players correctly"
                (should= \O (core/switch-player \X))
                (should= \X (core/switch-player \O)))))

(describe "AI functionality"
          (context "Move generation"
            (it "should get valid AI moves"
                (let [board (board/init-board 3)
                      move (core/get-ai-move board)]
                  (should= [1 1] move)  ; AI should take center first
                  (should (board/valid-move? board (first move) (second move)))))

            (it "should make AI moves when enabled"
                (let [state (-> (core/init-game)
                                (assoc-in [:config :ai-enabled] true)
                                (core/make-move 4 4))] ; Player X moves
                  (should (some #(= \O %) (flatten (:board state))))  ; Should find an O on the board
                  (should= \X (:current-player state))))))  ; Should be X's turn again

(describe "Game mechanics"
          (context "Basic moves"
            (it "should initialize game with empty board"
                (let [state (core/init-game)]
                  (should= 9 (count (:board state)))
                  (should= 9 (count (first (:board state))))
                  (should (every? #(= \space %) (flatten (:board state))))))

            (it "should track moves and player turns"
                (let [state (-> (core/init-game)
                                (assoc-in [:config :ai-enabled] false))
                      state-after-move (core/make-move state 0 0)]
                  (should= \X (get-in state-after-move [:board 0 0]))
                  (should= \O (:current-player state-after-move))))

            (it "should prevent moves on occupied cells"
                (let [state (core/init-game)
                      state-with-X (core/make-move state 1 1)
                      state-with-failed-O (core/make-move state-with-X 1 1)]
                  (should= \X (get-in state-with-failed-O [:board 1 1])))))

          (context "Game completion"
            (it "should detect horizontal wins"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3 :ai-enabled false})
                                (core/make-move 0 0)  ; X
                                (core/make-move 1 0)  ; O
                                (core/make-move 0 1)  ; X
                                (core/make-move 1 1)  ; O
                                (core/make-move 0 2))] ; X wins
                  (should (:game-over? state))
                  (should= \X (:winner state))))

            (it "should detect vertical wins"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3 :ai-enabled false})
                                (core/make-move 0 0)  ; X
                                (core/make-move 0 1)  ; O
                                (core/make-move 1 0)  ; X
                                (core/make-move 1 1)  ; O
                                (core/make-move 2 0))] ; X wins
                  (should (:game-over? state))
                  (should= \X (:winner state))))

            (it "should detect diagonal wins"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3 :ai-enabled false})
                                (core/make-move 0 0)  ; X
                                (core/make-move 0 1)  ; O
                                (core/make-move 1 1)  ; X
                                (core/make-move 0 2)  ; O
                                (core/make-move 2 2))] ; X wins
                  (should (:game-over? state))
                  (should= \X (:winner state))))

            (it "should detect draws"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3 :ai-enabled false})
                                (core/make-move 0 0) ; X
                                (core/make-move 0 1) ; O
                                (core/make-move 0 2) ; X
                                (core/make-move 1 1) ; O
                                (core/make-move 1 0) ; X
                                (core/make-move 1 2) ; O
                                (core/make-move 2 1) ; X
                                (core/make-move 2 0) ; O
                                (core/make-move 2 2))] ; X
                  (should (:game-over? state))
                  (should= nil (:winner state))))

            (it "should prevent moves after game over"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3 :ai-enabled false})
                                (core/make-move 0 0) ; X
                                (core/make-move 1 0) ; O
                                (core/make-move 0 1) ; X
                                (core/make-move 1 1) ; O
                                (core/make-move 0 2) ; X wins
                                (core/make-move 2 2))] ; Should not place mark
                  (should (:game-over? state))
                  (should= \space (get-in (:board state) [2 2]))))))

(run-specs)