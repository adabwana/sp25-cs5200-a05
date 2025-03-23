# Tic Tac Toe with AI

## PLAY GAME IN BROWSER [HERE](https://adabwana.github.io/sp25-cs5200-a05/)

An implementation of Tic Tac Toe that pushes beyond the traditional 3x3 boundaries. This project showcases AI gameplay on a 9x9 grid, leveraging Clojure's immutable data structures and the classical Minimax algorithm enhanced with Alpha-Beta pruning.

## Core Architecture

This implementation rests on three pillars:

### 1. Game State Management (`core.clj`)
The nerve center of our game, orchestrating the intricate dance between player moves and AI responses. Built on Clojure's atom-based state management, it ensures thread-safe mutations while maintaining responsive gameplay.

- **State Handling**: Atomic updates guarantee consistent game progression
- **Event Processing**: Seamless handling of user interactions
- **Flow Control**: Orchestrates the rhythm between human and AI turns

### 2. Board Logic (`board.clj`)
The mathematical foundation of our game world. This module implements the game's physical laws - from move validation to victory detection - with a focus on immutability and functional purity.

- **Board Representation**: Efficient 2D vector structure
- **Win Detection**: Sophisticated algorithms for checking 5-in-a-row patterns
- **Move Validation**: Robust boundary and occupation checking

### 3. AI Engine (`ai.clj`)
The strategic brain of our system, implementing a competitive AI player through classical game theory principles. The AI employs Minimax with Alpha-Beta pruning, specifically tuned for the unique challenges of a 9x9 grid with a 5-in-a-row victory condition.

- **Decision Making**: Minimax algorithm with Alpha-Beta optimization
- **Position Evaluation**: Sophisticated scoring system for board positions
- **Move Ordering**: Strategic prioritization for pruning efficiency

## Distinctive Features

- **Extended Grid**: 9x9 playing field with 5-in-a-row victory condition
- **Intelligent Opposition**: AI powered by game theory principles
- **Performance Insights**: Real-time metrics on AI decision-making
- **Strategic Depth**: Multiple levels of move analysis and prediction
- **Tactical Awareness**: Immediate threat detection and response

## Getting Started

The game runs within a development container, ensuring consistent environment setup across systems.

### Prerequisites
1. [**Docker Desktop**](https://docs.docker.com/get-started/get-docker/)

   - Install Docker Desktop for your operating system
   - Ensure Docker daemon is running

2. [**Visual Studio Code**](https://code.visualstudio.com/download)

3. [**Dev Containers Extension**](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)

### Setup & Running
1. **Clone & Open**

   ```bash
   git clone [repository-url]
   code [repository-directory]
   ```

2. **Development Container**

   - When prompted, click "Reopen in Container"
   - Or use Command Palette (`Ctrl/Cmd + Shift + P`): "Dev Containers: Reopen in Container"
   - Wait for container build to complete

3. **Running the Game**
   
   **Quil Version (Desktop)**
   - Open terminal in VS Code (`Ctrl + Shift + ``)
   - Run: `clj -M -m tictactoe.clj.main`

   **ClojureScript Version (Browser)**
   - Use Calva Jack-in (`Ctrl + Alt + C`, `Ctrl + Alt + J`)
   - Select "deps.edn + shadow-cljs"
   - Include all aliases when prompted
   - Once REPL is connected, open browser to `http://localhost:1992`

## Control Interface

- **Mouse**: Click any empty cell for X placement
- **Keyboard**:
  - `R`: Reset game state
  - `A`: Toggle AI participation
  - `Q`: Exit application

## Technical Performance

The AI implementation balances computational depth with real-time responsiveness through several optimizations:

### 1. Search Space Reduction

- **Alpha-Beta Pruning**: Dramatically reduces node evaluation count
- **Move Ordering**: Prioritizes high-potential positions (center)
- **Early Termination**: Recognizes decisive positions quickly

### 2. Strategic Optimizations

- **Center Control**: Prioritizes strategically valuable positions
- **Threat Detection**: Immediate response to winning opportunities
- **Pattern Recognition**: Efficient board state evaluation

### Performance Metrics

#### AI Move Processing Times
Performance measurements from `spec/tictactoe/cljs/core_spec.clj`:

| Board Size | Mean Time | Std Deviation |
|------------|-----------|---------------|
| 3x3        | 1.97 ms   | ±0.28 ms     |
| 4x4        | 9.47 ms   | ±1.31 ms     |
| 5x5        | 34.23 ms  | ±5.14 ms     |
| 6x6        | 99.22 ms  | ±10.24 ms    |
| 7x7        | 259.04 ms | ±24.83 ms    |
| 8x8        | 586.41 ms | ±44.40 ms    |
| 9x9        | 1.26 s    | ±45.90 ms    |

These measurements demonstrate the exponential growth in computation time as board size increases, while maintaining playable response times even on the full 9x9 grid. Key observations:

- **Early Game**: Sub-10ms decisions on smaller boards
- **Mid-size Boards**: Sub-100ms on 6x6 and smaller
- **Full 9x9**: ~1.2s for maximum complexity

## Technical Foundation

### 1. Data Architecture
- **Board State**: Immutable 2D vector structure
- **Move History**: Tracked for analysis and undo capability
- **State Management**: Atomic updates for consistency

### 2. AI Implementation
- **Search Algorithm**: Depth-first Minimax with pruning
- **Evaluation Function**: Multi-factor position scoring
- **Move Generation**: Optimized for 9x9 grid dynamics

### 3. Performance Tuning
- **Search Depth**: Adaptive based on position complexity
- **Memory Management**: Efficient state representation
- **Computation Distribution**: Balanced decision tree exploration 