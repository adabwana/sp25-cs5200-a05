(ns tictactoe.web
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [tictactoe.cljs.core :as core]
   [tictactoe.cljs.xxxconfig :as config]
   [tictactoe.cljs.board :as board]))

;; ## Game State

(defonce web-state
  (r/atom (core/init-game)))

;; ## Styling Constants

(def styles
  (r/atom (config/get-config [:styles])))

;; ## Memoized Component Helpers

(def cell-style-memo
  (memoize
   (fn [mark]
     (merge (:cell @styles)
            (when (= mark \space)
              {:hover {:background "#f0f0f0"}})))))

(def cell-click-handler
  (memoize
   (fn [row col mark game-over? ai-enabled? current-player ai-player]
     (when (and (not game-over?)
                (= mark \space)
                (not (and ai-enabled?
                          (= current-player ai-player))))
       #(swap! web-state core/make-move row col)))))

;; ## Game Board Component

(defn cell-component [row col mark]
  (let [game-state @web-state
        ai-enabled? (get-in game-state [:config :ai-enabled])
        current-player (:current-player game-state)
        ai-player (get-in game-state [:config :ai-player])]
    (r/create-class
     {:component-name (str "Cell-" row "-" col)
      :should-component-update
      (fn [this [_ _ _ old-mark] [_ _ _ new-mark]]
        (not= old-mark new-mark))
      :reagent-render
      (fn [row col mark]
        [:div {:style (cell-style-memo mark)
               :on-click (cell-click-handler row col mark
                                             (:game-over? game-state)
                                             ai-enabled?
                                             current-player
                                             ai-player)}
         (when (not= mark \space)
           [:span {:style {:color (if (= mark \X) "#e74c3c" "#3498db")}}
            mark])])})))

(def board-component
  (r/create-class
   {:component-name "Board"
    :should-component-update
    (fn [this old-argv new-argv]
      (not= (:board (second old-argv))
            (:board (second new-argv))))
    :reagent-render
    (fn []
      [:div {:style (:board @styles)}
       (for [row (range (get-in @web-state [:config :board-size]))
             col (range (get-in @web-state [:config :board-size]))
             :let [mark (get-in (:board @web-state) [row col])]]
         ^{:key (str row "-" col)}
         [cell-component row col mark])])}))

;; ## Control Components

(def controls-component
  (r/create-class
   {:component-name "Controls"
    :should-component-update
    (fn [this old-argv new-argv]
      (not= (get-in (second old-argv) [:config :ai-enabled])
            (get-in (second new-argv) [:config :ai-enabled])))
    :reagent-render
    (fn []
      [:div {:style (:controls @styles)}
       [:button
        {:style (:button @styles)
         :on-click #(reset! web-state (core/init-game))}
        "New Game"]
       [:button
        {:style (:button @styles)
         :on-click #(swap! web-state update-in [:config :ai-enabled] not)}
        (if (get-in @web-state [:config :ai-enabled])
          "Disable AI"
          "Enable AI")]])}))

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
    (fn []
      [:div {:style (:status @styles)}
       (cond
         (:game-over? @web-state)
         (if-let [winner (:winner @web-state)]
           (str "Player " winner " wins!")
           "Game is a draw!")

         :else
         (str "Current player: " (:current-player @web-state)
              (when (and (get-in @web-state [:config :ai-enabled])
                         (= (:current-player @web-state) (get-in @web-state [:config :ai-player])))
                " (AI's turn)")))])}))

;; ## Main Game Component

(def game-component
  (r/create-class
   {:component-name "Game"
    :reagent-render
    (fn []
      [:div {:style {:display "flex"
                     :flex-direction "column"
                     :align-items "center"
                     :font-family "sans-serif"}}
       [:h1 {:style {:color "#2c3e50"
                     :margin-bottom "20px"}}
        "Tic Tac Toe"]
       [board-component @web-state]
       [status-component @web-state]
       [controls-component @web-state]])}))

;; ## Game Initialization

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/render [game-component] root-el)))

(defn init! []
  (mount-root))