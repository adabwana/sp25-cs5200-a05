(ns dev
  (:require [scicloj.clay.v2.api :as clay]))


(defn build []
  (clay/make!
   {:format              [:quarto :html]
    :book                {:title "CS5200: MiniMax Tic-Tac-Toe"}
    ;; :subdirs-to-sync     ["notebooks" "data" "images"]
    :source-path         ["src/index.clj"]
    :base-target-path    "docs"
    ;; :live-reload true
    :clean-up-target-dir true}))

(comment
  (build))