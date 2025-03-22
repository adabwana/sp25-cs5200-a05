(ns tictactoe.clj.ui
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [tictactoe.clj.core :as core]
            [tictactoe.clj.board :as board]))


;; # User Interface Module
;;
;; This module implements the **graphical user interface** for the Tic-Tac-Toe game using
;; the *Quil library* (Clojure wrapper for Processing). It provides a responsive and
;; interactive game board with real-time state visualization and player feedback.
;; The implementation follows reactive design principles, updating the display
;; based on game state changes.

;; ## Display Constants
;;
;; The UI layout system uses **fixed measurements** to ensure consistent
;; rendering across different board sizes:
;; - *cell-size*: Individual grid cell dimensions
;; - *padding*: Space around the game board
;; - *status-height*: Height of the status message area

(def cell-size 60)
(def padding 20)
(def status-height 20)

;; ## Window Management
;;
;; The window management system handles **dynamic sizing** calculations
;; based on the game board dimensions. It ensures proper scaling and
;; layout of all UI components.

(defn calculate-window-size
  "Calculate the window dimensions based on board size.
   Returns [width height] where:
   - width = board width + 2 * padding
   - height = board height + 2 * padding + status area"
  [board-size]
  (let [board-pixels (* cell-size board-size)]
    [(+ board-pixels (* 2 padding))
     (+ board-pixels (* 2 padding) status-height)]))

;; ## Initialization
;;
;; The setup system establishes the **initial rendering environment**
;; and configures the game state for first use.

(defn setup
  "Initialize the graphical environment and game state.
   Sets up:
   - Frame rate for smooth animation
   - Color mode for consistent rendering
   - Initial game state"
  []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  (core/new-game core/default-config))

;; ## Grid Rendering
;;
;; The grid system provides the **foundational game board structure**,
;; creating the visual framework for game play.

(defn draw-grid
  "Draw the game board grid.
   Creates a square grid with:
   - Consistent line weight
   - Equal cell sizes
   - Proper padding
   
   Time complexity: O(n) where n is board-size"
  [board-size]
  (q/stroke 0)
  (q/stroke-weight 1)
  (let [board-pixels (* cell-size board-size)]
    (doseq [i (range (inc board-size))]
      (let [pos (+ padding (* i cell-size))]
        ;; Vertical lines
        (q/line pos padding pos (+ padding board-pixels))
        ;; Horizontal lines
        (q/line padding pos (+ padding board-pixels) pos)))))

;; ## Mark Rendering
;;
;; The mark rendering system handles the **visual representation** of
;; player moves, creating distinct and clear symbols for each player.

(defn draw-mark
  "Draw a player's mark (X or O) in the specified cell.
   Implements distinct visual styles:
   - X: Crossed lines
   - O: Hollow circle
   
   Parameters:
   - row, col: Grid coordinates
   - mark: Player symbol (\\X or \\O)"
  [row col mark]
  (when (not= mark \space)  ; Only draw if the cell is not empty
    (let [x (+ padding (* col cell-size) (/ cell-size 2))
          y (+ padding (* row cell-size) (/ cell-size 2))
          size (* cell-size 0.8)]
      (q/stroke-weight 2)
      (if (= mark \X)
        (do
          (q/line (- x (/ size 2)) (- y (/ size 2))
                  (+ x (/ size 2)) (+ y (/ size 2)))
          (q/line (- x (/ size 2)) (+ y (/ size 2))
                  (+ x (/ size 2)) (- y (/ size 2))))
        (do
          (q/no-fill)  ; Don't fill the circle
          (q/ellipse x y size size)
          (q/fill 0))))))  ; Reset fill for other elements

;; ## Status Display
;;
;; The status system provides **real-time game state feedback**,
;; displaying current player, game outcome, and AI status.

(defn draw-status
  "Draw the game status message.
   Displays:
   - Current player or game result
   - AI status and toggle instructions
   - Win/draw notifications
   
   Parameters:
   - state map containing game-over?, winner, and current-player"
  [{:keys [game-over? winner current-player]}]
  (q/fill 0)
  (q/text-size 16)
  (q/text-align :center :center)
  (let [ai-status (if (:ai-enabled (:config @core/game-state))
                    " (AI: ON - Press 'a' to disable)"
                    " (AI: OFF - Press 'a' to enable)")
        msg (cond
              game-over? (if winner
                           (str "Player " winner " wins!")
                           "Game is a draw!")
              :else (str "Current player: " current-player
                         (when (and (:ai-enabled (:config @core/game-state))
                                    (= current-player (:ai-player (:config @core/game-state))))
                           " (AI's turn)")))]
    (q/text (str msg ai-status)
            (/ (q/width) 2)
            (- (q/height) (/ status-height 2)))))

;; ## Main Rendering Loop
;;
;; The main rendering system coordinates all visual components,
;; ensuring proper layering and consistent updates.

(defn draw-state
  "Main drawing function, renders the complete game state.
   Implements layered rendering:
   1. Clear background
   2. Draw grid
   3. Draw all marks
   4. Update status display
   
   Parameters:
   - state: Current game state (unused but required by Quil)"
  [state]
  (q/background 255)
  (let [board-size (count (:board @core/game-state))]
    (draw-grid board-size)
    (doseq [row (range board-size)
            col (range board-size)]
      (when-let [mark (get-in (:board @core/game-state) [row col])]
        (when (not= mark \space)
          (draw-mark row col mark))))
    (draw-status @core/game-state)))

;; ## Input Processing
;;
;; The input system handles **user interactions**, converting
;; mouse and keyboard events into game actions.

(defn mouse-to-grid
  "Convert mouse coordinates to grid positions.
   Returns [row col] if click is within grid, nil otherwise.
   
   Parameters:
   - x, y: Mouse coordinates
   - board-size: Current board dimensions"
  [x y board-size]
  (let [board-x (- x padding)
        board-y (- y padding)]
    (when (and (>= board-x 0)
               (>= board-y 0)
               (< board-x (* board-size cell-size))
               (< board-y (* board-size cell-size)))
      [(int (/ board-y cell-size))
       (int (/ board-x cell-size))])))

(defn mouse-clicked
  "Handle mouse click events.
   Converts click coordinates to grid position and attempts move.
   
   Parameters:
   - state: Current game state
   - event: Mouse event data containing x, y coordinates
   
   Returns: Updated state"
  [state event]
  (let [board-size (count (:board @core/game-state))]
    (when-let [[row col] (mouse-to-grid (:x event) (:y event) board-size)]
      (core/make-move row col))
    state))

(defn key-pressed
  "Handle keyboard input events.
   Supports:
   - 'r': Reset game
   - 'a': Toggle AI
   
   Parameters:
   - state: Current game state
   - event: Keyboard event data
   
   Returns: Updated state"
  [state event]
  (cond
    (= (:key event) :r)
    (core/new-game (:config @core/game-state))

    (= (:raw-key event) \a)
    (swap! core/game-state update-in [:config :ai-enabled] not))
  state)

;; ## Sketch Initialization
;;
;; The sketch initialization system creates and configures the
;; **main game window** with all necessary components and handlers.

(defn run-sketch
  "Initialize and run the game sketch.
   Sets up:
   - Window size and title
   - Event handlers
   - Drawing functions
   - Middleware for state management"
  []
  (let [board-size 9
        [width height] (calculate-window-size board-size)]
    (q/sketch
     :title "Tic Tac Toe"
     :size [width height]
     :setup setup
     :draw draw-state
     :mouse-clicked mouse-clicked
     :key-pressed key-pressed
     :middleware [m/fun-mode]
     :features [:resizable])))

#_(run-sketch)