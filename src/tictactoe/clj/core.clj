(ns tictactoe.clj.core
  (:require
   [tictactoe.clj.board :as board]
   [tictactoe.clj.ai :as ai]))

;; # Game Core Module
;;
;; This module implements the **central game management system** for the Tic-Tac-Toe application.
;; It orchestrates the interaction between the *board mechanics* and *AI system*, managing game
;; state transitions and enforcing game rules. The implementation uses Clojure's atom-based
;; state management for controlled mutability while maintaining thread safety.

;; ## Game Configuration
;;
;; The configuration system provides **default settings** for game initialization.
;; These parameters define the game's scale and behavior:
;; - *board-size*: Determines the grid dimensions (9×9)
;; - *win-length*: Number of marks needed in a line to win (5)
;; - *ai-enabled*: Controls AI player participation
;; - *ai-player*: Designates which mark (X/O) the AI controls

(def default-config
  {:board-size 9
   :win-length 5
   :ai-enabled true
   :ai-player \O})

;; ## State Management
;;
;; The game state is managed through a **centralized atom**, providing atomic
;; updates and consistent state transitions. The state structure includes:
;; - *board*: Current game board configuration
;; - *current-player*: Active player's mark (X/O)
;; - *game-over?*: Game termination flag
;; - *winner*: Winner's mark, if any
;; - *config*: Active game configuration

(def game-state
  (atom {:board nil
         :current-player \X
         :game-over? false
         :winner nil
         :config default-config}))

;; ## Player Management
;;
;; The player management system handles *turn alternation* and maintains
;; the game's turn-based structure. It implements simple but robust
;; player switching logic.

(defn switch-player
  "Switch the current player.
   Implements a binary toggle between X and O marks, maintaining
   the strict alternation of turns."
  [current-player]
  (if (= current-player \X) \O \X))

;; ## Game Status Updates
;;
;; The status update system manages **game state transitions** after each move.
;; It checks for win conditions, draws, and handles player transitions,
;; ensuring the game progresses according to standard Tic-Tac-Toe rules.

(defn update-game-status!
  "Update the game status after a move.
   Performs three key checks in order:
   1. Win condition for current player
   2. Board full (draw) condition
   3. Player transition for next turn
   
   Side effects: Updates game-state atom with new status"
  [board current-player]
  (let [next-player (switch-player current-player)
        win-length (get-in @game-state [:config :win-length])]
    (cond
      (board/check-winner board current-player win-length)
      (swap! game-state assoc
             :game-over? true
             :winner current-player)

      (board/board-full? board)
      (swap! game-state assoc
             :game-over? true)

      :else
      (swap! game-state assoc
             :current-player next-player))))

;; ## AI Integration
;;
;; The AI integration system manages the **autonomous player** functionality.
;; It coordinates with the AI module to generate moves when appropriate,
;; maintaining seamless interaction between human and AI players.

(defn ai-move
  "Make an AI move if it's the AI's turn.
   Coordinates with the AI system to:
   1. Check if AI move is appropriate
   2. Generate and validate the move
   3. Update game state accordingly
   
   Side effects: Updates game state when AI moves"
  []
  (let [{:keys [board current-player config]} @game-state
        {:keys [ai-enabled ai-player win-length]} config]
    (when (and ai-enabled
               (not (:game-over? @game-state))
               (= current-player ai-player))
      (when-let [[row col] (ai/get-best-move board current-player win-length)]
        (let [new-board (board/place-mark board row col current-player)]
          (swap! game-state assoc :board new-board)
          (update-game-status! new-board current-player))))))

;; ## Move Processing
;;
;; The move processing system handles the **core game loop** operations,
;; managing both human and AI moves. It ensures move validity and maintains
;; proper game state transitions throughout play.

(defn make-move
  "Make a move and update the game state.
   Processes moves through several stages:
   1. Validate game state and move legality
   2. Apply move to board
   3. Update game status
   4. Trigger AI response if appropriate
   
   Side effects: Updates game state and may trigger AI move"
  [row col]
  (let [current-state @game-state
        current-board (:board current-state)
        current-player (:current-player current-state)]
    (when (and (not (:game-over? current-state))
               (board/valid-move? current-board row col))
      (let [new-board (board/place-mark current-board row col current-player)]
        (swap! game-state assoc :board new-board)
        (update-game-status! new-board current-player)
        (ai-move)))))

;; ## Game Initialization
;;
;; The initialization system provides **clean game state creation**,
;; supporting both default and custom configurations. It ensures
;; proper setup of all game components before play begins.

(defn new-game
  "Initialize a new game with the specified board size and win length.
   Supports both default and custom configurations:
   - Without args: Uses default-config
   - With config: Uses provided configuration map
   
   Side effects: Resets game-state atom to initial values"
  ([]
   (new-game default-config))
  ([config]
   (reset! game-state
           {:board (board/init-board (:board-size config))
            :current-player \X
            :game-over? false
            :winner nil
            :config config}))) 