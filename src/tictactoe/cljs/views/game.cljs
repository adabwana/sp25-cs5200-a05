(ns tictactoe.cljs.views.game
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [tictactoe.cljs.state.core :as state]
   [tictactoe.cljs.components.board :refer [board-component]]
   [tictactoe.cljs.components.controls :refer [controls-component]]
   [tictactoe.cljs.components.status :refer [status-component]]))

(def game-component
  (r/create-class
   {:component-name "Game"
    :reagent-render
    (fn []
      (let [game-state (state/get-state)]
        [:div {:style {:display "flex"
                       :flex-direction "column"
                       :align-items "center"
                       :font-family "sans-serif"}}
         [:h1 {:style {:color "#2c3e50"
                       :margin-bottom "20px"}}
          "Tic Tac Toe"]
         [board-component game-state]
         [status-component game-state]
         [controls-component game-state]]))}))

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/render [game-component] root-el)))

(defn init! []
  (mount-root))