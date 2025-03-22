(ns tictactoe.cljs.config.constants)

(def game-constants
  {:default
   {:board-size 9
    :win-length 5
    :ai-enabled true
    :ai-player \O}

   :ai
   {:max-depth 3
    :infinity 1000000
    :neg-infinity -1000000
    :win-score 1
    :lose-score -1
    :draw-score 0
    :cache-size {:lru-threshold 1000
                 :move-scores 100}}})

(def styles
  {:board {:display "grid"
           :grid-template-columns "repeat(9, 50px)"
           :gap "2px"
           :background "#ccc"
           :padding "10px"
           :border-radius "5px"}
   :cell {:width "50px"
          :height "50px"
          :display "flex"
          :align-items "center"
          :justify-content "center"
          :background "white"
          :font-size "24px"
          :font-weight "bold"
          :cursor "pointer"
          :user-select "none"
          :transition "background-color 0.2s"
          :hover {:background "#f0f0f0"}}
   :controls {:margin-top "20px"
              :display "flex"
              :gap "10px"}
   :button {:padding "8px 16px"
            :border-radius "4px"
            :border "none"
            :background "#007bff"
            :color "white"
            :cursor "pointer"
            :font-size "14px"
            :transition "background-color 0.2s"
            :hover {:background "#0056b3"}}
   :status {:margin-top "10px"
            :font-size "18px"
            :font-weight "500"
            :text-align "center"
            :color "#333"}})