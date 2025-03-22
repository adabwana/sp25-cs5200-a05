(ns index
  (:require
   [scicloj.kindly.v4.kind :as kind]
   [scicloj.kindly.v4.api :as kindly]
   [web :as ttt-web]))

;; # Tic Tac Toe with AI
;; 
;; This is a 9x9 Tic Tac Toe game implemented in Clojure with an AI opponent using Minimax with Alpha-Beta pruning.
;; The game requires 5 marks in a row to win.

;; ## Game Rules
;; 
;; - The game is played on a 9x9 grid
;; - Players take turns placing their marks (X or O)
;; - The first player to get 5 marks in a row (horizontally, vertically, or diagonally) wins
;; - If the board fills up before anyone wins, the game is a draw
;; - You play as X, and the AI plays as O

;; ## Interactive Game
;; 
;; Below is an interactive version of the game. You can:
;; - Click any empty cell to make your move
;; - Use the "New Game" button to restart
;; - Toggle AI on/off with the AI button
;; 
;; The game shows:
;; - The current player (X or O)
;; - Whether AI is enabled
;; - Game status (ongoing, win, or draw)

^:kind/reagent
(ttt-web/init-game)

;; ## Implementation Details
;; 
;; The game uses several key algorithms and data structures:
;; 
;; 1. **Board Representation**
;;    - The board is represented as a 2D vector of characters
;;    - Empty cells contain spaces, and filled cells contain 'X' or 'O'
;; 
;; 2. **AI Strategy**
;;    - The AI uses the Minimax algorithm with Alpha-Beta pruning
;;    - It looks ahead several moves to find the best possible move
;;    - The evaluation function considers:
;;      - Winning positions (highest score)
;;      - Blocking opponent's wins
;;      - Center and near-center positions (preferred)
;; 
;; 3. **Win Detection**
;;    - The game checks for 5-in-a-row patterns after each move
;;    - It efficiently scans horizontal, vertical, and diagonal lines
;;    - The scanning algorithm is optimized to check only valid line positions