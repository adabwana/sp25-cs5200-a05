(ns tictactoe.cljs.components.controls
  (:require
   [reagent.core :as r]
   [tictactoe.cljs.config.settings :as settings]
   [tictactoe.cljs.state.events :as events]))

(def controls-component
  (r/create-class
   {:component-name "Controls"
    :should-component-update
    (fn [this old-argv new-argv]
      (not= (get-in (second old-argv) [:config :ai-enabled])
            (get-in (second new-argv) [:config :ai-enabled])))
    :reagent-render
    (fn [game-state]
      [:div {:style (settings/get-style [:controls])}
       [:button
        {:style (settings/get-style [:button])
         :on-click events/new-game!}
        "New Game"]
       [:button
        {:style (settings/get-style [:button])
         :on-click events/toggle-ai!}
        (if (get-in game-state [:config :ai-enabled])
          "Disable AI"
          "Enable AI")]])}))