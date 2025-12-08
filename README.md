# COMP2042 Tetris - Coursework Project

A modern JavaFX implementation of Tetris with enhanced features including multiplayer mode, ghost piece visualization, combo scoring, and sound effects.

## Author
**Shahjalal** - COMP2042 Developing Maintainable Software

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [How to Play](#how-to-play)
- [Project Structure](#project-structure)
- [Design Patterns](#design-patterns)
- [Refactoring Summary](#refactoring-summary)
- [Testing](#testing)
- [Known Issues](#known-issues)

## Features

### Core Gameplay
- Classic Tetris mechanics with smooth controls
- Progressive level system with increasing difficulty
- Score tracking with level multipliers

### New Additions
- **Hold Piece** - Store a piece for later use (press C or Shift)
- **Ghost Piece** - Semi-transparent preview showing where your piece will land
- **Combo System** - Chain line clears for bonus points
- **Sound Effects** - Audio feedback for moves, clears, and game events (toggle with M)
- **Multiplayer Mode** - Local 2-player battle mode with HP-based attacks

### Multiplayer Battle Mode
Two players can battle on the same keyboard! Clear lines to attack your opponent's HP.

**Player 1 Controls (WASD):**
- W - Rotate
- A - Move Left
- S - Soft Drop
- D - Move Right
- Shift - Hold Piece
- Space - Hard Drop

**Player 2 Controls (Arrow Keys):**
- Up - Rotate
- Left - Move Left
- Down - Soft Drop
- Right - Move Right
- Ctrl - Hold Piece
- Numpad 0 - Hard Drop

**Attack Damage:**
| Lines Cleared | Damage | Name |
|---------------|--------|------|
| 1 | 1 HP | Single |
| 2 | 3 HP | Double |
| 3 | 5 HP | Triple |
| 4 | 8 HP | TETRIS |

Combos multiply damage - keep clearing lines to devastate your opponent!

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- JavaFX SDK

### Building the Project
```bash
mvn clean compile
```

### Running the Game
```bash
mvn javafx:run
```

### Running Tests
```bash
mvn test
```

## How to Play

### Single Player Controls
| Key | Action |
|-----|--------|
| ← / A | Move Left |
| → / D | Move Right |
| ↑ / W | Rotate |
| ↓ / S | Soft Drop |
| C / Shift | Hold Piece |
| N | New Game |
| M | Toggle Sound |

### Scoring
- Soft drop: 1 point per row
- Line clear bonuses increase with level
- Combo multipliers for consecutive clears
- Tetris (4 lines) gives the highest bonus

## Project Structure

```
src/main/java/com/comp2042/tetris/
├── audio/              # Sound effects system
│   ├── SoundManager.java
│   └── SoundType.java
├── collision/          # Collision detection
│   └── CollisionDetector.java
├── controllers/        # Game controllers
│   ├── GameController.java
│   └── TetrominoRotator.java
├── core/               # Core game logic
│   ├── Board.java
│   ├── ComboManager.java
│   ├── GhostPieceCalculator.java
│   ├── HoldPieceManager.java
│   ├── LevelManager.java
│   ├── Score.java
│   └── TetrisBoard.java
├── events/             # Event handling
│   ├── EventSource.java
│   ├── EventType.java
│   ├── InputEventListener.java
│   └── MoveEvent.java
├── models/             # Data models
│   ├── ClearRow.java
│   ├── DownData.java
│   ├── NextShapeInfo.java
│   └── ViewData.java
├── multiplayer/        # Multiplayer mode
│   ├── AttackCalculator.java
│   ├── MultiplayerController.java
│   ├── MultiplayerGameManager.java
│   ├── PlayerHealth.java
│   └── PlayerInputHandler.java
├── patterns/           # Design patterns
│   ├── DifficultyStrategy.java
│   ├── EasyDifficulty.java
│   ├── HardDifficulty.java
│   ├── MediumDifficulty.java
│   ├── ScoreObserver.java
│   └── ScoreSubject.java
├── pieces/             # Tetromino pieces
│   ├── IPiece.java, JPiece.java, etc.
│   ├── Tetromino.java
│   ├── TetrominoFactory.java
│   └── TetrominoGenerator.java
├── ui/                 # User interface
│   ├── ColorPalette.java
│   ├── GameOverPanel.java
│   ├── GuiController.java
│   └── NotificationPanel.java
└── utils/              # Utilities
    └── MatrixOperations.java
```

## Design Patterns

### Factory Pattern
`TetrominoFactory` centralizes piece creation, making it easy to add new piece types without modifying game logic.

### Strategy Pattern
`DifficultyStrategy` allows different difficulty levels (Easy, Medium, Hard) to be swapped at runtime, each with different speed and scoring rules.

### Observer Pattern
`ScoreObserver` and `ScoreSubject` decouple score updates from UI components, allowing multiple listeners to react to score changes.

### Singleton Pattern
`GameState` ensures only one instance of global game state exists, providing a single source of truth.

## Refactoring Summary

### Package Reorganization
Restructured from flat package to logical groupings: `core`, `pieces`, `ui`, `controllers`, `patterns`, `events`, `models`, `utils`, `audio`, `collision`, `multiplayer`.

### Class Extraction
- `CollisionDetector` - Extracted from TetrisBoard for single responsibility
- `GhostPieceCalculator` - Dedicated class for ghost piece logic
- `HoldPieceManager` - Manages hold piece state
- `ComboManager` - Tracks consecutive line clears
- `LevelManager` - Handles level progression and difficulty

### Bug Fixes
- Fixed array bounds issues in piece creation
- Corrected rotation boundary validation
- Fixed score increment logic for level multipliers

## Testing

The project includes comprehensive unit tests with 70%+ code coverage.

### Test Classes
- `CollisionDetectorTest` - Collision detection edge cases
- `TetrominoRotatorTest` - Rotation mechanics
- `ScoreTest` - Score calculations
- `HoldPieceManagerTest` - Hold piece functionality
- `GhostPieceCalculatorTest` - Ghost piece positioning
- `ComboManagerTest` - Combo tracking and bonuses
- `PlayerHealthTest` - Multiplayer HP system
- `AttackCalculatorTest` - Damage calculations

Run all tests:
```bash
mvn test
```

## Known Issues

1. Sound files need to be added to `resources/sounds/` directory
2. Multiplayer UI (side-by-side boards) is not yet implemented
3. Some edge cases in wall kick rotation not fully implemented

## Future Improvements

- Online multiplayer support
- High score leaderboard
- Custom themes and skins
- Replay system
- Touch controls for mobile

## License

This project was created for educational purposes as part of COMP2042 coursework.
