(ns tictactoe.cljs.xxxai-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.cljs.xxxai :as ai]
   [tictactoe.cljs.board :as board]))

(def infinity 1000000)
(def neg-infinity -1000000)

(describe "Position evaluation"
          (it "should correctly evaluate a winning position"
              (let [board [[\space \space \space]
                           [\X \X \X]
                           [\space \space \space]]]
                (should= infinity (#'ai/evaluate-position board \X 3))))

          (it "should correctly evaluate a losing position"
              (let [board [[\space \space \space]
                           [\O \O \O]
                           [\space \space \space]]]
                (should= neg-infinity (#'ai/evaluate-position board \X 3))))

          (it "should correctly evaluate a neutral position"
              (let [board [[\X \O \X]
                           [\X \O \O]
                           [\O \X \X]]]
                (should= 0 (#'ai/evaluate-position board \X 3)))))

(describe "Move generation and validation"
          (it "should find all available moves"
              (let [board [[\X \space \space]
                           [\space \O \space]
                           [\space \space \space]]
                    moves (#'ai/get-valid-moves board)]
                (should= 7 (count moves))
                (should (some #(= [0 1] %) moves))
                (should (some #(= [0 2] %) moves))
                (should (some #(= [1 0] %) moves))
                (should (some #(= [1 2] %) moves))
                (should (some #(= [2 0] %) moves))
                (should (some #(= [2 1] %) moves))
                (should (some #(= [2 2] %) moves)))))

(describe "Win detection and prevention"
          (context "Winning move detection"
            (it "should detect winning move in row"
                (let [board [[\X \X \space]
                             [\space \space \space]
                             [\space \space \space]]]
                  (should= [0 2] (#'ai/find-winning-move board \X 3))))

            (it "should detect winning move in column"
                (let [board [[\X \space \space]
                             [\X \space \space]
                             [\space \space \space]]]
                  (should= [2 0] (#'ai/find-winning-move board \X 3))))

            (it "should detect winning move in diagonal"
                (let [board [[\X \space \space]
                             [\space \X \space]
                             [\space \space \space]]]
                  (should= [2 2] (#'ai/find-winning-move board \X 3)))))

          (context "Immediate threat response"
            (it "should find immediate winning move in row"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]]
                  (should= [0 2] (#'ai/find-winning-move board \X 3))))

            (it "should find immediate blocking move"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]]
                  (should= [0 2] (#'ai/find-blocking-move board \O 3))))))

(describe "Strategic decision making"
          (context "Optimal move selection"
            (it "should find winning move in simple position"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]
                      move (ai/get-best-move board \X 3)]
                  (should= [0 2] move)))

            (it "should find blocking move in simple position"
                (let [board [[\X \X \space]
                             [\O \space \space]
                             [\space \O \space]]
                      move (ai/get-best-move board \O 3)]
                  (should= [0 2] move)))

            (it "should prefer winning over blocking"
                (let [board [[\X \X \space]
                             [\O \O \space]
                             [\space \space \space]]
                      move (ai/get-best-move board \O 3)]
                  (should= [1 2] move)))  ; O should complete its own win rather than block X

            (it "should take center in empty board"
                (let [board (board/init-board 3)
                      move (ai/get-best-move board \O 3)]
                  (should= [1 1] move))))

          (context "Performance optimization"
            (it "should efficiently prune unnecessary branches"
                (let [board [[\X \O \X]
                             [\O \X \space]
                             [\space \space \space]]
                      start-time (System/currentTimeMillis)
                      move (ai/get-best-move board \O 3)
                      end-time (System/currentTimeMillis)
                      duration (- end-time start-time)]
                  (should (< duration 1000))))))

(run-specs) 