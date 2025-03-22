(ns tictactoe.cljs.components.status
  (:require
   [reagent.core :as r]
   [tictactoe.cljs.config.settings :as settings]))

(def status-component
  (r/create-class
   {:component-name "Status"
    :should-component-update
    (fn [this old-argv new-argv]
      (let [old-state (second old-argv)
            new-state (second new-argv)]
        (or (not= (:game-over? old-state) (:game-over? new-state))
            (not= (:winner old-state) (:winner new-state))
            (not= (:current-player old-state) (:current-player new-state)))))
    :reagent-render
    (fn [game-state]
      [:div {:style (settings/get-style [:status])}
       (cond
         (:game-over? game-state)
         (if-let [winner (:winner game-state)]
           (str "Player " winner " wins!")
           "Game is a draw!")

         :else
         (str "Current player: " (:current-player game-state)
              (when (and (get-in game-state [:config :ai-enabled])
                         (= (:current-player game-state) (get-in game-state [:config :ai-player])))
                " (AI's turn)")))])}))