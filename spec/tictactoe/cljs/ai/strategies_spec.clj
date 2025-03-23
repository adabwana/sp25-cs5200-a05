(ns tictactoe.cljs.ai.strategies-spec
  (:require
   [criterium.core :as crit]
   [speclj.core :refer :all]
   [tictactoe.cljs.ai.strategies :as strat]))

;; Performance Benchmarks
(println "\n=== Strategy Benchmarks ===")

(let [board [[\X \O \X]
             [\X \O \O]
             [\O \X \X]]]
  (println "\nBenchmarking position evaluation:")
  (crit/quick-bench (strat/evaluate-position board \X 3)))

(let [board [[\X \space \space]
             [\space \O \space]
             [\space \space \space]]]
  (println "\nBenchmarking move generation:")
  (crit/quick-bench (strat/get-available-moves board)))

(println "\nBenchmarking move scoring (9x9 board):")
(crit/quick-bench (strat/get-move-scores 9))

(let [moves [[0 0] [4 4] [0 8] [8 0] [8 8]]]
  (println "\nBenchmarking move ordering:")
  (crit/quick-bench (strat/order-moves moves 9)))

(let [board [[\X \X \space]
             [\O \O \space]
             [\space \space \space]]]
  (println "\nBenchmarking winning move detection:")
  (crit/quick-bench (strat/find-winning-move board \X 3)))

;; Test Specs
(describe "Position evaluation"
          (it "should correctly evaluate a winning position"
              (let [board [[\space \space \space]
                           [\X \X \X]
                           [\space \space \space]]]
                (should= 1 (strat/evaluate-position board \X 3))))

          (it "should correctly evaluate a losing position"
              (let [board [[\space \space \space]
                           [\O \O \O]
                           [\space \space \space]]]
                (should= -1 (strat/evaluate-position board \X 3))))

          (it "should correctly evaluate a neutral position"
              (let [board [[\X \O \X]
                           [\X \O \O]
                           [\O \X \X]]]
                (should= 0 (strat/evaluate-position board \X 3)))))

(describe "Move generation and scoring"
          (it "should find all available moves"
              (let [board [[\X \space \space]
                           [\space \O \space]
                           [\space \space \space]]
                    moves (strat/get-available-moves board)]
                (should= 7 (count moves))
                (should (some #(= [0 1] %) moves))
                (should (some #(= [0 2] %) moves))))

          (it "should calculate center distance correctly"
              (let [board-size 3]
                (should= 0 (strat/center-distance [1 1] board-size))
                (should= 2 (strat/center-distance [0 0] board-size))))

          (it "should order moves with center preference"
              (let [moves [[0 0] [1 1] [0 2]]
                    board-size 3
                    ordered (strat/order-moves moves board-size)]
                (should= [1 1] (first ordered)))))

(describe "Win detection"
          (context "Winning move detection"
            (it "should detect winning move in row"
                (let [board [[\X \X \space]
                             [\space \space \space]
                             [\space \space \space]]]
                  (should= [0 2] (strat/find-winning-move board \X 3))))

            (it "should detect winning move in column"
                (let [board [[\X \space \space]
                             [\X \space \space]
                             [\space \space \space]]]
                  (should= [2 0] (strat/find-winning-move board \X 3)))))

          (context "Blocking move detection"
            (it "should find blocking move for opponent win"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]]
                  (should= [0 2] (strat/find-blocking-move board \O 3))))))

(run-specs)