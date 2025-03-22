(ns tictactoe.cljs.config.settings
  (:require [tictactoe.cljs.config.constants :as constants]))

(defn get-config
  "Get configuration by path. Example: (get-config [:default :board-size])"
  [path]
  (get-in constants/game-constants path))

(defn get-style
  "Get style configuration by path. Example: (get-style [:board])"
  [path]
  (get-in constants/styles path))

(defn merge-config
  "Merge custom configuration with default configuration"
  [custom-config]
  (merge (get-config [:default]) custom-config))