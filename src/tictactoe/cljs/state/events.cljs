(ns tictactoe.cljs.state.events
  (:require
   [tictactoe.cljs.state.core :as state]
   [tictactoe.cljs.core :as game]))

(defn make-move!
  "Make a move at the specified position"
  [row col]
  (state/update-state! game/make-move row col))

(defn toggle-ai!
  "Toggle AI on/off"
  []
  (state/update-state! update-in [:config :ai-enabled] not))

(defn new-game!
  "Start a new game"
  []
  (state/reset-state!))