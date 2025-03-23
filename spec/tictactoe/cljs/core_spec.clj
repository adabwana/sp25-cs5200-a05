(ns tictactoe.cljs.core-spec
  (:require
   [criterium.core :as crit]
   [speclj.core :refer :all]
   [tictactoe.cljs.core :as core]
   [tictactoe.cljs.board :as board]
   [tictactoe.cljs.config.settings :as settings]))

;; Performance Benchmarks
(println "\n=== Core Game Logic Benchmarks ===")

(println "\nBenchmarking game initialization:")
#_(crit/quick-bench (core/init-game))

#_(let [state (core/init-game)]
    (println "\nBenchmarking player move:")
    (crit/quick-bench (core/make-move state 4 4)))

(let [state (-> (core/init-game)
                (assoc-in [:config :ai-enabled] true)
                (assoc :current-player \O))]
  (println "\nBenchmarking AI move processing:")
  (crit/quick-bench (core/process-ai-move state)))

#_(let [state (-> (core/init-game {:board-size 3 :win-length 3})
                  (core/make-move 0 0)
                  (core/make-move 0 1)
                  (core/make-move 1 1))]
    (println "\nBenchmarking game status check:")
    (crit/quick-bench (core/check-game-status state)))

;; Test Specs
(describe "Game state"
          (context "Configuration"
            (it "should initialize with default configuration"
                (let [state (core/init-game)
                      default-config (settings/get-config [:default])]
                  (should= (:board-size default-config) (-> state :config :board-size))
                  (should= (:win-length default-config) (-> state :config :win-length))
                  (should= (:ai-enabled default-config) (-> state :config :ai-enabled))
                  (should= (:ai-player default-config) (-> state :config :ai-player))))

            (it "should accept custom configuration"
                (let [custom-config {:board-size 5 :win-length 4}
                      state (core/init-game custom-config)]
                  (should= 5 (-> state :config :board-size))
                  (should= 4 (-> state :config :win-length)))))

          (context "Player management"
            (it "should switch players correctly"
                (should= \O (core/switch-player \X))
                (should= \X (core/switch-player \O)))))

(describe "Game mechanics"
          (context "Move processing without AI"
            (it "should process valid moves"
                (let [state (-> (core/init-game)
                                (assoc-in [:config :ai-enabled] false))
                      new-state (core/make-move state 4 4)]
                  (should= \X (get-in new-state [:board 4 4]))
                  (should= \O (:current-player new-state))))

            (it "should ignore invalid moves"
                (let [state (-> (core/init-game)
                                (assoc-in [:config :ai-enabled] false))
                      state-with-move (core/make-move state 4 4)
                      invalid-move-state (core/make-move state-with-move 4 4)]
                  (should= state-with-move invalid-move-state))))

          (context "Game status"
            (it "should detect wins"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3})
                                (assoc-in [:config :ai-enabled] false)
                                (core/make-move 0 0)  ; X
                                (core/make-move 1 0)  ; O
                                (core/make-move 0 1)  ; X
                                (core/make-move 1 1)  ; O
                                (core/make-move 0 2))] ; X wins
                  (should (:game-over? state))
                  (should= \X (:winner state))))

            (it "should detect draws"
                (let [state (-> (core/init-game {:board-size 3 :win-length 3})
                                (assoc-in [:config :ai-enabled] false)
                                (core/make-move 0 0) ; X
                                (core/make-move 0 1) ; O
                                (core/make-move 1 1) ; X
                                (core/make-move 0 2) ; O
                                (core/make-move 2 0) ; X
                                (core/make-move 1 0) ; O
                                (core/make-move 1 2) ; X
                                (core/make-move 2 2) ; O
                                (core/make-move 2 1))] ; X
                  ;; Final board should look like:
                  ;; X O O
                  ;; O X X
                  ;; X X O
                  (should (:game-over? state))
                  (should= nil (:winner state))))))

(describe "AI integration"
          (context "AI move processing"
            (it "should process AI moves when enabled"
                (let [initial-state (core/init-game)  ; AI enabled by default
                      after-move (core/make-move initial-state 4 4)]
                  (should= \X (get-in after-move [:board 4 4]))  ; Player's X move
                  (should (some #(= \O %) (flatten (:board after-move))))  ; AI's O move
                  (should= \X (:current-player after-move))))  ; Back to X's turn

            (it "should not process AI moves when disabled"
                (let [state (-> (core/init-game)
                                (assoc-in [:config :ai-enabled] false))
                      after-move (core/make-move state 4 4)]
                  (should= \X (get-in after-move [:board 4 4]))  ; Player's move
                  (should= \O (:current-player after-move))  ; Switch to O
                  (should-not (some #(= \O %) (flatten (:board after-move))))))))  ; No AI move

(run-specs)