(ns tictactoe.clj.ui-spec
  (:require
   [speclj.core :refer :all]
   [tictactoe.clj.ui :refer :all]
   [tictactoe.clj.core :as core :refer [make-move]]
   [quil.core :as q]))

(describe "Window Calculations"
          (it "should calculate correct window size"
      ;; 9x9 board: (9 * 60) + (2 * 20) = 540 + 40 = 580 width
      ;;            (9 * 60) + (2 * 20) + 20 = 540 + 40 + 20 = 600 height
              (should= [580 600] (calculate-window-size 9))
      ;; 3x3 board: (3 * 60) + (2 * 20) = 180 + 40 = 220 width
      ;;            (3 * 60) + (2 * 20) + 20 = 180 + 40 + 20 = 240 height
              (should= [220 240] (calculate-window-size 3)))

          (it "should convert mouse coordinates to grid positions"
              (core/new-game {:board-size 9 :win-length 3})
              (let [board-size 9]
        ;; Test center of first cell (should be [0 0])
                (should= [0 0] (mouse-to-grid (+ padding (/ cell-size 2))
                                              (+ padding (/ cell-size 2))
                                              board-size))
        ;; Test outside grid (should be nil)
                (should= nil (mouse-to-grid 0 0 board-size))
                (should= nil (mouse-to-grid 1000 1000 board-size)))))

(describe "Game State Management"
          (before
           (core/new-game {:board-size 9 :win-length 5 :ai-enabled false}))

          (it "should initialize with correct state"
              (should= 9 (count (:board @core/game-state)))  ; 9x9 board
              (should= \X (:current-player @core/game-state))  ; X starts
              (should-not (:game-over? @core/game-state))  ; Game not over
              (should= nil (:winner @core/game-state)))  ; No winner

          (it "should update state after valid move"
              (make-move 0 0)  ; Make a move at [0 0]
              (should= \X (get-in @core/game-state [:board 0 0]))  ; X should be placed
              (should= \O (:current-player @core/game-state)))  ; Should be O's turn

          (it "should not update state for invalid move"
              (make-move 0 0)  ; Make first move
              (let [old-state @core/game-state]
                (make-move 0 0)  ; Try to move in same spot
                (should= old-state @core/game-state))))  ; State should not change

(describe "Input Handling"
          (before
           (core/new-game {:board-size 9 :win-length 5 :ai-enabled false}))

          (it "should handle valid mouse clicks"
              (let [event {:x (+ padding (/ cell-size 2))
                           :y (+ padding (/ cell-size 2))}
                    state {}]
                (mouse-clicked state event)
                (should= \X (get-in @core/game-state [:board 0 0]))))

          (it "should handle restart key press"
              (let [event {:key :r}
                    state {}]
                (make-move 0 0)  ; Make a move first
                (should= \X (get-in @core/game-state [:board 0 0]))
                (key-pressed state event)  ; Press R
                (should= \space (get-in @core/game-state [:board 0 0]))))  ; Board should be cleared

          (it "should handle AI toggle"
              (let [event {:raw-key \a}
                    state {}]
                (should-not (get-in @core/game-state [:config :ai-enabled]))
                (key-pressed state event)  ; Press A
                (should (get-in @core/game-state [:config :ai-enabled])))))

(describe "Win Detection"
          (before
           (core/new-game {:board-size 9 :win-length 5 :ai-enabled false}))

          (it "should detect horizontal win"
              (doseq [col (range 5)]  ; Make X win with 5 in a row
                (make-move 0 col)  ; X moves
                (when (< col 4)
                  (make-move 1 col)))  ; O moves
              (should (:game-over? @core/game-state))
              (should= \X (:winner @core/game-state)))

          (it "should detect vertical win"
              (doseq [row (range 5)]  ; Make X win with 5 in a column
                (make-move row 0)  ; X moves
                (when (< row 4)
                  (make-move row 1)))  ; O moves
              (should (:game-over? @core/game-state))
              (should= \X (:winner @core/game-state)))

          (it "should detect diagonal win"
              (doseq [i (range 5)]  ; Make X win diagonally
                (make-move i i)  ; X moves
                (when (< i 4)
                  (make-move i (inc i))))  ; O moves
              (should (:game-over? @core/game-state))
              (should= \X (:winner @core/game-state))))

(run-specs)