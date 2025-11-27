# Refactoring Documentation

This document details all the refactoring work I did on the original Tetris codebase. My goal was to transform a working but messy codebase into something clean, maintainable, and extensible.

## Package Reorganization

### Before
The original code had most classes in a flat package structure, making it hard to understand the relationships between components.

### After
I reorganized into logical packages:

| Package | Purpose | Key Classes |
|---------|---------|-------------|
| `core` | Core game logic | TetrisBoard, Score, LevelManager |
| `pieces` | Tetromino definitions | Tetromino, TetrominoFactory, IPiece, etc. |
| `controllers` | Game control flow | GameController, TetrominoRotator |
| `ui` | User interface | GuiController, ColorPalette |
| `events` | Event handling | InputEventListener, MoveEvent |
| `models` | Data transfer objects | ViewData, ClearRow, DownData |
| `patterns` | Design pattern implementations | DifficultyStrategy, ScoreObserver |
| `collision` | Collision detection | CollisionDetector |
| `audio` | Sound system | SoundManager, SoundType |
| `multiplayer` | Battle mode | MultiplayerController, PlayerHealth |
| `utils` | Utility functions | MatrixOperations |

## Design Patterns Implemented

### 1. Factory Pattern (TetrominoFactory)

**Problem:** Piece creation was scattered throughout the code with `new` calls.

**Solution:** Centralized all piece creation in `TetrominoFactory`.

```java
/* before - scattered new calls */
Tetromino piece = new IPiece();

/* after - factory method */
Tetromino piece = TetrominoFactory.createTetromino(TetrominoType.I_PIECE);
```

**Benefits:**
- Easy to add new piece types
- Can implement weighted random selection
- Single point of change for piece creation

### 2. Strategy Pattern (DifficultyStrategy)

**Problem:** Difficulty settings were hardcoded, making it impossible to change at runtime.

**Solution:** Created `DifficultyStrategy` interface with Easy, Medium, Hard implementations.

```java
public interface DifficultyStrategy {
    int getStartingLevel();
    int getDropSpeed(int level);
    int getScoreMultiplier();
}
```

**Benefits:**
- Swap difficulty at runtime
- Easy to add new difficulty modes
- Clean separation of difficulty logic

### 3. Observer Pattern (ScoreObserver)

**Problem:** UI was tightly coupled to score updates.

**Solution:** Implemented observer pattern for score notifications.

```java
public interface ScoreObserver {
    void onScoreChanged(int newScore);
}

public interface ScoreSubject {
    void addObserver(ScoreObserver observer);
    void removeObserver(ScoreObserver observer);
    void notifyObservers();
}
```

**Benefits:**
- Multiple components can react to score changes
- Decoupled UI from game logic
- Easy to add new score displays

### 4. Singleton Pattern (GameState)

**Problem:** Global game state was passed around as parameters.

**Solution:** Singleton for game-wide state access.

**Benefits:**
- Single source of truth
- Easier state management
- Reduced parameter passing

## Single Responsibility Refactoring

### CollisionDetector Extraction

**Before:** TetrisBoard handled collision detection inline.

**After:** Dedicated `CollisionDetector` class with static methods.

```java
public class CollisionDetector {
    public static boolean checkCollision(int[][] board, int[][] piece, int x, int y);
    public static boolean canMoveDown(int[][] board, int[][] piece, Point position);
    public static boolean canMoveLeft(int[][] board, int[][] piece, Point position);
    public static boolean canMoveRight(int[][] board, int[][] piece, Point position);
    public static boolean canRotate(int[][] board, int[][] piece, Point position);
}
```

### GhostPieceCalculator

Extracted ghost piece logic into its own class for testability.

### HoldPieceManager

Manages hold piece state with clear API:
- `holdPiece(Tetromino current)` - Store current, return previously held
- `canHold()` - Check if hold is available
- `resetHoldLock()` - Called when new piece spawns

### ComboManager

Tracks consecutive line clears:
- Increments on successful clear
- Resets on piece lock without clear
- Calculates bonus points

### LevelManager

Handles level progression:
- Tracks total lines cleared
- Calculates current level
- Determines drop speed

## Bug Fixes

### 1. Array Bounds in createNewBrick

**Problem:** Piece spawning could cause array index out of bounds.

**Fix:** Added boundary validation before accessing array.

### 2. Rotation Boundary Issues

**Problem:** Rotating near walls could place piece outside board.

**Fix:** Added position validation in rotation logic.

### 3. Score Increment Logic

**Problem:** Score wasn't properly multiplied by level.

**Fix:** Applied level multiplier correctly in score calculation.

## Code Quality Improvements

### Naming Conventions
- Renamed `Brick` to `Tetromino` (domain-appropriate)
- Used descriptive method names (`canMoveDown` vs `check`)
- Clear variable names throughout

### Comments
- Added Javadocs to all public methods
- Inline comments explain WHY, not what
- Natural, conversational style

### Functional Programming
- Used streams where appropriate
- Preferred immutable data where possible
- Switch expressions for type safety

## Testing

Created comprehensive unit tests:
- `CollisionDetectorTest` - 15 test cases
- `TetrominoRotatorTest` - 8 test cases
- `ScoreTest` - 6 test cases
- `HoldPieceManagerTest` - 7 test cases
- `GhostPieceCalculatorTest` - 4 test cases
- `ComboManagerTest` - 7 test cases
- `PlayerHealthTest` - 9 test cases
- `AttackCalculatorTest` - 7 test cases

Total: 63+ test cases, targeting 70%+ code coverage.

## Lessons Learned

1. **Start with package structure** - Getting this right early makes everything else easier.

2. **Extract classes liberally** - Single responsibility makes testing SO much easier.

3. **Design patterns aren't magic** - They're just good solutions to common problems.

4. **Write tests as you go** - I caught several bugs early because I was testing.

5. **Comments should explain why** - The code shows what, comments show intent.
