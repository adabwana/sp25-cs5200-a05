(ns tictactoe.clj.main
  (:require [tictactoe.clj.ui :as ui])
  (:gen-class))

(defn -main
  "Start the Tic Tac Toe game"
  [& args]
  (ui/run-sketch))