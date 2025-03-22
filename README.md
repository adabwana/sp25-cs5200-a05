# Tic Tac Toe with AI

A 9x9 Tic Tac Toe game implemented in Clojure using Quil for graphics and Minimax with Alpha-Beta Pruning for AI.

## Program Architecture

### Components

1. **Game State Management (`core.clj`)**
   - Uses Quil's state management for game state
   - Handles user input (mouse clicks and keyboard events)
   - Manages game flow between player and AI turns
   - Renders game board and UI elements

2. **Board Logic (`board.clj`)**
   - Provides functions for board manipulation
   - Checks for wins and draws
   - Validates moves

3. **AI Logic (`ai.clj`)**
   - Implements Minimax algorithm with Alpha-Beta Pruning
   - Includes move ordering for optimization
   - Handles special cases (immediate wins/blocks)
   - Tracks performance metrics

### Key Features

- 9x9 grid with 5-in-a-row win condition
- Player vs AI gameplay
- Performance metrics display
- Efficient AI using Alpha-Beta Pruning
- Move ordering optimization
- Immediate win/block detection

## How to Run

The game is set up to run in a development container with all necessary dependencies pre-installed.

1. **Run the Game**
   - Open the project in VS Code with the development container
   - The game will start automatically

## Controls

- Click on any empty cell to make a move as Player X
- Press 'R' to restart the game
- Press 'A' to toggle AI on/off (AI is on by default)
- Press 'Q' to quit

## Algorithm Performance

The AI uses several optimizations to improve performance:

1. **Alpha-Beta Pruning**
   - Reduces the number of nodes evaluated
   - Prunes branches that cannot affect the final decision
   - Performance metrics show nodes evaluated and time per move

2. **Move Ordering**
   - Prioritizes center and near-center moves
   - Checks for immediate wins and blocks first
   - Improves pruning efficiency

3. **Special Case Handling**
   - Immediate win detection
   - Blocking opponent's winning moves
   - Reduces unnecessary tree traversal

### Performance Metrics

The game displays real-time metrics:
- Number of nodes evaluated
- Time taken for last move

Typical performance:
- Early game: 50-200ms per move
- Mid game: 200-500ms per move
- Late game: faster due to fewer available moves

## Implementation Notes

1. **Board Representation**
   - 2D vector of characters (\X, \O, \space)
   - Efficient access and updates
   - Easy win checking

2. **AI Strategy**
   - Maximizing player (AI) seeks highest score
   - Minimizing player (opponent) seeks lowest score
   - Depth limit of 3 for performance
   - Scores: Win = 1, Loss = -1, Draw = 0

3. **Optimizations**
   - Forward pruning of obvious moves
   - Center-based move ordering
   - Early termination on winning positions 