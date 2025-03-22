(ns tictactoe.cljs.core
  (:require
   [tictactoe.cljs.board :as board]
   [tictactoe.cljs.config.settings :as settings]
   [tictactoe.cljs.ai.engine :as ai-engine]))

(defn init-game
  "Initialize a new game with optional custom configuration."
  ([]
   (init-game (settings/get-config [:default])))
  ([custom-config]
   (let [config (settings/merge-config custom-config)]
     {:board (board/init-board (:board-size config))
      :current-player \X
      :game-over? false
      :winner nil
      :config config})))

(defn switch-player
  "Switch the current player."
  [current-player]
  (if (= current-player \X) \O \X))

(defn check-game-status
  "Check if the game is over and return updated state."
  [state]
  (let [{:keys [board current-player config]} state
        {:keys [win-length]} config]
    (cond
      ;; Check if current player won
      (board/check-winner board current-player win-length)
      (assoc state
             :game-over? true
             :winner current-player)

      ;; Check if board is full (draw)
      (board/board-full? board)
      (assoc state
             :game-over? true
             :winner nil)

      ;; Game continues
      :else state)))

(defn process-ai-move
  "Process AI move if it's AI's turn."
  [state]
  (let [{:keys [board config]} state
        {:keys [ai-enabled ai-player win-length]} config]
    (if (and ai-enabled
             (not (:game-over? state))
             (= (:current-player state) ai-player))
      (let [[ai-row ai-col] (ai-engine/get-best-move board ai-player win-length)]
        (if (and ai-row ai-col)
          (let [new-board (board/place-mark board ai-row ai-col ai-player)]
            (-> state
                (assoc :board new-board)
                check-game-status
                (update :current-player switch-player)))
          state))
      state)))

(defn make-move
  "Make a move and return the updated game state."
  [state row col]
  (let [{:keys [board current-player game-over?]} state]
    (if (and (not game-over?)
             (board/valid-move? board row col))
      (let [new-board (board/place-mark board row col current-player)]
        (-> state
            (assoc :board new-board)
            check-game-status
            (update :current-player switch-player)
            process-ai-move))
      state)))