(ns tictactoe.clj.ai-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.clj.ai :refer :all]
   [tictactoe.clj.board :as board]))

(describe "AI for Tic-Tac-Toe"
          (describe "Position evaluation"
                    (it "should correctly evaluate a winning position"
                        (let [board [[\space \space \space]
                                     [\X \X \X]
                                     [\space \space \space]]]
                          (should= win-score (evaluate-position board \X 3))))

                    (it "should correctly evaluate a losing position"
                        (let [board [[\space \space \space]
                                     [\O \O \O]
                                     [\space \space \space]]]
                          (should= lose-score (evaluate-position board \X 3))))

                    (it "should correctly evaluate a draw position"
                        (let [board [[\X \O \X]
                                     [\X \O \O]
                                     [\O \X \X]]]
                          (should= draw-score (evaluate-position board \X 3))))))

(describe "Move generation and ordering"
          (it "should find all available moves"
              (let [board [[\X \space \space]
                           [\space \O \space]
                           [\space \space \space]]
                    moves (get-available-moves board)]
                (should= 7 (count moves))
                (should-contain [0 1] moves)
                (should-contain [0 2] moves)
                (should-contain [1 0] moves)
                (should-contain [1 2] moves)
                (should-contain [2 0] moves)
                (should-contain [2 1] moves)
                (should-contain [2 2] moves)))

          (it "should prioritize center moves"
              (let [board (board/init-board 3)
                    moves (get-available-moves board)
                    ordered-moves (order-moves moves 3)]
                (should= [1 1] (first ordered-moves)))))

(describe "Win detection"
          (it "should detect winning move in row"
              (let [board [[\X \X \space]
                           [\space \space \space]
                           [\space \space \space]]]
                (should (would-win? board 0 2 \X 3))
                (should-not (would-win? board 1 1 \X 3))))

          (it "should detect winning move in column"
              (let [board [[\X \space \space]
                           [\X \space \space]
                           [\space \space \space]]]
                (should (would-win? board 2 0 \X 3))
                (should-not (would-win? board 2 1 \X 3))))

          (it "should detect winning move in diagonal"
              (let [board [[\X \space \space]
                           [\space \X \space]
                           [\space \space \space]]]
                (should (would-win? board 2 2 \X 3))
                (should-not (would-win? board 2 1 \X 3)))))

(describe "Finding immediate moves"
          (it "should find immediate winning move in row"
              (let [board [[\X \X \space]
                           [\O \O \space]
                           [\space \space \space]]]
                (should= [0 2] (find-winning-move board \X 3))))

          (it "should find immediate blocking move"
              (let [board [[\X \X \space]
                           [\O \O \space]
                           [\space \space \space]]]
                (should= [0 2] (find-blocking-move board \O 3)))))

(describe "Alpha-Beta pruning"
          (it "should detect when to prune"
              (should (should-prune? 5 3))
              (should-not (should-prune? 3 5)))

          (it "should find winning move in simple position"
              (let [board [[\X \X \space]
                           [\O \O \space]
                           [\space \space \space]]
                    moves (get-available-moves board)
                    [score move] (alpha-beta-search board moves 3 true \X 3 Integer/MIN_VALUE Integer/MAX_VALUE)]
                (should= [0 2] move)
                (should= win-score score)))

          (it "should find blocking move in simple position"
              (let [board [[\X \X \space]
                           [\O \space \space]
                           [\space \O \space]]
                    moves (get-available-moves board)
                    [score move] (alpha-beta-search board moves 3 true \O 3 Integer/MIN_VALUE Integer/MAX_VALUE)]
                (should= [0 2] move)))

          (it "should prefer winning over blocking"
              (let [board [[\X \X \space]
                           [\O \O \space]
                           [\space \space \space]]
                    moves (get-available-moves board)
                    [score move] (alpha-beta-search board moves 3 true \O 3 Integer/MIN_VALUE Integer/MAX_VALUE)]
                (should= [1 2] move)  ; O should complete its own win rather than block X
                (should= win-score score)))

          (it "should prune unnecessary branches"
              (let [board [[\X \O \X]
                           [\O \X \space]
                           [\space \space \space]]
                    moves (get-available-moves board)
                    start-time (System/currentTimeMillis)
                    result (alpha-beta-search board moves 5 true \O 3 Integer/MIN_VALUE Integer/MAX_VALUE)
                    end-time (System/currentTimeMillis)
                    duration (- end-time start-time)]
              ; With pruning, this should complete quickly
                (should (< duration 1000)))))  ; Should take less than 1 second

(describe "Decision making"
          (it "should block immediate wins"
              (let [board [[\X \X \space]
                           [\O \space \space]
                           [\space \O \space]]
                    best-move (get-best-move board \O 3)]
                (should= [0 2] best-move)))

          (it "should take winning move when available"
              (let [board [[\O \O \space]
                           [\X \X \space]
                           [\space \space \space]]
                    best-move (get-best-move board \O 3)]
                (should= [0 2] best-move)))

          (it "should prefer center in empty board"
              (let [board (board/init-board 3)
                    best-move (get-best-move board \O 3)]
                (should= [1 1] best-move))))

(run-specs)