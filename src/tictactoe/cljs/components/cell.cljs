(ns tictactoe.cljs.components.cell
  (:require
   [reagent.core :as r]
   [tictactoe.cljs.config.settings :as settings]
   [tictactoe.cljs.state.events :as events]))

(def cell-style-memo
  (memoize
   (fn [mark]
     (merge (settings/get-style [:cell])
            (when (= mark \space)
              {:hover {:background "#f0f0f0"}})))))

(def cell-click-handler
  (memoize
   (fn [row col mark game-over? ai-enabled? current-player ai-player]
     (when (and (not game-over?)
                (= mark \space)
                (not (and ai-enabled?
                          (= current-player ai-player))))
       #(events/make-move! row col)))))

(defn cell-component [row col mark game-state]
  (let [ai-enabled? (get-in game-state [:config :ai-enabled])
        current-player (:current-player game-state)
        ai-player (get-in game-state [:config :ai-player])]
    (r/create-class
     {:component-name (str "Cell-" row "-" col)
      :should-component-update
      (fn [this [_ _ _ old-mark] [_ _ _ new-mark]]
        (not= old-mark new-mark))
      :reagent-render
      (fn [row col mark game-state]
        [:div {:style (cell-style-memo mark)
               :on-click (cell-click-handler row col mark
                                             (:game-over? game-state)
                                             ai-enabled?
                                             current-player
                                             ai-player)}
         (when (not= mark \space)
           [:span {:style {:color (if (= mark \X) "#e74c3c" "#3498db")}}
            mark])])})))