(ns tictactoe.cljs.state.core
  (:require
   [reagent.core :as r]
   [tictactoe.cljs.core :as game]))

;; Application state
(defonce app-state
  (r/atom (game/init-game)))

;; State access helpers
(defn get-state
  "Get the current game state"
  []
  @app-state)

(defn update-state!
  "Update the game state with a function"
  [f & args]
  (apply swap! app-state f args))

(defn reset-state!
  "Reset the game state to initial state"
  []
  (reset! app-state (game/init-game)))