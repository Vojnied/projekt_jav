# Centipede

A JavaFX-based clone of the classic Centipede arcade game with a Spring Boot REST backend for score persistence. Built as a semester project for the Java I course at VSB — Technical University of Ostrava.

The player controls a cannon at the bottom of the screen and shoots at a centipede that descends through a field of obstacles (mushrooms). Each centipede segment hit turns into an obstacle; splitting a segment creates a new independent centipede. Scores are persisted to an H2 database via a REST API.

## Architecture

```
JavaFX Client                        Spring Boot Server (embedded, same JVM)
┌─────────────────┐   HTTP (8080)   ┌──────────────────────────────────┐
│  App.java       │ ──────────────→ │  GameApiController (REST)        │
│  MenuController │                 │         ↓                       │
│  GameController │                 │  GameService (business logic)    │
│  EndScreenCtrl  │                 │         ↓                       │
│  ScoreRepository│ ←────────────── │  ScoreEntityRepository (Spring  │
│   (REST client) │                 │        Data JPA)                 │
└─────────────────┘                 │         ↓                       │
                                    │  H2 File DB (data/centipede)    │
                                    └──────────────────────────────────┘
```

Both layers start in the same JVM — `App.init()` boots Spring Boot before the JavaFX window opens.

## Data Model

The server persists four entity types with 1:N relationships:

```
Player 1──N GameSession 1──1 ScoreEntity N──1 DifficultyLevel
```

| Entity | Stores |
|--------|--------|
| **Player** | `id`, `nickname` (unique), `createdAt` |
| **GameSession** | `id`, `startedAt`, `endedAt`, `causeOfDeath` (CENTIPEDE / QUIT), `player` (FK) |
| **ScoreEntity** | `id`, `points`, `achievedAt`, `player` (FK), `session` (FK), `difficultyLevel` (FK) |
| **DifficultyLevel** | `id`, `levelNumber` (1–10, pre-seeded) |

- One player can have many game sessions and many scores.
- Each score is linked to exactly one session, one player, and one difficulty level.
- Cause of death is either `CENTIPEDE` (player health reached 0) or `QUIT` (ESC pressed).

## Prerequisites

- **Java 21** JDK or later
- **Maven 3.x** (for compilation)
- Lombok annotation processing (built into the Maven compiler config)

## Project Structure

```
projekt_jav/
├── pom.xml                         # Maven build (Spring Boot 3.2.5, JavaFX 23, H2, Lombok)
├── data/                           # H2 database files (created at runtime)
├── src/main/java/
│   ├── module-info.java            # Java module descriptors
│   ├── lab/                        # JavaFX client layer
│   │   ├── App.java                # Entry point (JavaFX Application)
│   │   ├── GameController.java     # Game-loop logic and input handling
│   │   ├── MenuController.java     # Main menu screen (nickname entry)
│   │   ├── EndScreenController.java# End-of-game score screen
│   │   ├── World.java              # Game world, entity management, collision
│   │   ├── Cannon.java             # Player ship
│   │   ├── Centipede.java          # Centipede movement and splitting
│   │   ├── CentipedeSegment.java   # Single centipede segment
│   │   ├── Bullet.java             # Player projectile
│   │   ├── Obstacle.java           # Mushroom / block obstacles
│   │   ├── DrawingThread.java      # Game loop (AnimationTimer)
│   │   └── score/
│   │       ├── Score.java           # Client-side DTO
│   │       └── ScoreRepository.java # HTTP client for REST API
│   └── cz/vsb/fei/java2/server/    # Spring Boot server layer
│       ├── SpringBootApp.java      # @SpringBootApplication entry point
│       ├── entity/                 # JPA entities (Player, ScoreEntity,
│       │                           #   GameSession, DifficultyLevel, ScoreRecord)
│       ├── repository/             # Spring Data JPA repositories
│       ├── service/                # Business logic (GameService)
│       ├── controller/             # REST API endpoints (GameApiController)
│       └── config/                 # Startup config (DataInitializer)
├── src/main/resources/
│   ├── application.properties      # Spring Boot config (H2, port 8080)
│   └── lab/
│       ├── menu.fxml               # Menu screen layout
│       ├── gameWindow.fxml         # Game screen layout
│       ├── endScreen.fxml          # End screen layout
│       ├── application.css         # Global styles
│       └── *.png                   # Sprites (ship, bullet, centipede, blocks)
```

## Setup

Clone the repository and compile the project:

```bash
git clone <repository-url>
cd projekt_jav
mvn clean compile
```

Maven resolves all dependencies (Spring Boot, JavaFX, H2, Lombok, JUnit) automatically.

## Running the Game

The game embeds a Spring Boot server that starts automatically. Run the main class `lab.App`.

### From an IDE (recommended)

1. Open the project in your IDE as a Maven project.
2. Locate the main class `lab.App`.
3. Run it as a Java application (add `--add-modules javafx.controls,javafx.fxml` to VM options if needed).

### From the command line

```bash
java --module-path <path-to-javafx-sdk>/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp target/classes lab.App
```

> Replace `<path-to-javafx-sdk>` with your JavaFX 23 SDK installation directory. The SDK can be downloaded from [gluonhq.com](https://gluonhq.com/products/javafx/).

On startup the console prints `H2 console available at http://localhost:8080/h2-console` — the REST API and H2 console are both accessible while the game is running.

## REST API

All endpoints are served at `http://localhost:8080/api`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/players` | Find or create a player by nickname |
| `POST` | `/api/sessions` | Create a new game session |
| `PUT` | `/api/sessions/{id}/end` | End a session with cause (CENTIPEDE / QUIT) |
| `POST` | `/api/scores` | Save a score for a session |
| `GET` | `/api/scores` | List all scores (leaderboard, descending) |
| `GET` | `/api/scores/above?min=` | List scores above a threshold |
| `DELETE` | `/api/scores/{id}` | Delete a score |
| `GET` | `/api/difficulties` | List available difficulty levels (1–10) |

## H2 Console

While the game is running, the H2 web console is available at `http://localhost:8080/h2-console`:

- **JDBC URL:** `jdbc:h2:file:./data/centipede`
- **User:** `sa`
- **Password:** *(empty)*

## Controls

| Key     | Action          |
| ------- | --------------- |
| `W`     | Move up         |
| `A`     | Move left       |
| `S`     | Move down       |
| `D`     | Move right      |
| `Space` | Fire bullet     |
| `Esc`   | Return to menu  |
