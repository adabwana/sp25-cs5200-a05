(ns tictactoe.cljs.components.board
  (:require
   [reagent.core :as r]
   [tictactoe.cljs.config.settings :as settings]
   [tictactoe.cljs.components.cell :refer [cell-component]]))

(def board-component
  (r/create-class
   {:component-name "Board"
    :should-component-update
    (fn [this old-argv new-argv]
      (not= (:board (second old-argv))
            (:board (second new-argv))))
    :reagent-render
    (fn [game-state]
      [:div {:style (settings/get-style [:board])}
       (for [row (range (get-in game-state [:config :board-size]))
             col (range (get-in game-state [:config :board-size]))
             :let [mark (get-in (:board game-state) [row col])]]
         ^{:key (str row "-" col)}
         [cell-component row col mark game-state])])})) 