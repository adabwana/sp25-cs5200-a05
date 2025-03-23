(ns tictactoe.cljs.ai.engine-spec
  (:require
   [criterium.core :as crit]
   [speclj.core :refer :all]
   [tictactoe.cljs.ai.engine :as engine]
   [tictactoe.cljs.board :as board]))

;; Performance Benchmarks
(println "\n=== Engine Benchmarks ===")

(let [early-board [[\X \space \space]
                   [\space \O \space]
                   [\space \space \space]]]
  (println "\nBenchmarking early game best move:")
  (crit/quick-bench (engine/get-best-move early-board \O 3)))

(let [mid-board [[\X \O \X]
                 [\O \X \space]
                 [\space \space \space]]]
  (println "\nBenchmarking mid game best move:")
  (crit/quick-bench (engine/get-best-move mid-board \O 3)))

(let [win-board [[\X \X \space]
                 [\O \O \space]
                 [\space \space \space]]]
  (println "\nBenchmarking winning position detection:")
  (crit/quick-bench (engine/get-best-move win-board \O 3)))

(let [board [[\X \O \X]
             [\O \X \space]
             [\space \space \space]]]
  (println "\nBenchmarking terminal node detection:")
  (crit/quick-bench (#'engine/terminal-node? board 3 3)))

(let [board [[\X \O \X]
             [\O \X \space]
             [\space \space \space]]]
  (println "\nBenchmarking alpha-beta search:")
  (crit/quick-bench (#'engine/alpha-beta-search
                     board
                     [[2 0] [2 1] [2 2]]
                     3
                     true
                     \O
                     3
                     -1000000
                     1000000)))

(describe "Strategic decision making"
          (context "Optimal move selection"
            (it "should find winning move in simple position"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]
                      move (engine/get-best-move board \X 3)]
                  (should= [0 2] move)))

            (it "should find blocking move in simple position"
                (let [board [[\X \X \space]
                             [\O \space \space]
                             [\space \O \space]]
                      move (engine/get-best-move board \O 3)]
                  (should= [0 2] move)))

            (it "should prefer winning over blocking"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]
                      move (engine/get-best-move board \O 3)]
                  (should= [1 2] move)))  ; O should complete its own win rather than block X

            (it "should take center in empty board"
                (let [board (board/init-board 3)
                      move (engine/get-best-move board \O 3)]
                  (should= [1 1] move))))

          (context "Performance optimization"
            (it "should efficiently prune unnecessary branches"
                (let [board [[\X \O \X]
                             [\O \X \space]
                             [\space \space \space]]
                      start-time (System/currentTimeMillis)
                      move (engine/get-best-move board \O 3)
                      end-time (System/currentTimeMillis)
                      duration (- end-time start-time)]
                  (should (< duration 1000))))))

(describe "Terminal state detection"
          (it "should recognize game-ending states"
              (let [board [[\X \X \X]
                           [\O \O \space]
                           [\space \space \space]]]
                (should (#'engine/terminal-node? board 3 3))))

          (it "should recognize non-terminal states"
              (let [board [[\X \O \X]
                           [\O \X \space]
                           [\space \space \space]]]
                (should-not (#'engine/terminal-node? board 3 3)))))

(describe "Alpha-Beta pruning"
          (it "should prune when alpha >= beta"
              (should (#'engine/should-prune? 5 3)))

          (it "should not prune when alpha < beta"
              (should-not (#'engine/should-prune? 2 5))))

(run-specs) 