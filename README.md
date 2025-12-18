# Bubble Shooter Game

An enhanced bubble shooter game built with Java Swing, featuring beautiful graphics, animations, and engaging gameplay.

## Project Structure

```
BubbleShooter/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── Shooting/
│   │   │       ├── Bubble.java
│   │   │       └── BubbleShooterGame.java
│   │   └── resources/
│   └── test/
│       └── java/
└── target/
```

## Prerequisites

- Java JDK 11 or higher
- Apache Maven 3.6 or higher

## Building the Project

### Using Maven Command Line

1. **Clean and compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Create JAR file:**
   ```bash
   mvn clean package
   ```

   This will generate multiple JAR files in the `target/` directory:
   - `BubbleShooter.jar` - Simple JAR
   - `BubbleShooter-jar-with-dependencies.jar` - JAR with all dependencies
   - `BubbleShooter-executable.jar` - Shaded executable JAR

## Running the Game

### From JAR file:
```bash
java -jar target/BubbleShooter.jar
```

Or using the executable JAR:
```bash
java -jar target/BubbleShooter-executable.jar
```

### From source:
```bash
mvn exec:java -Dexec.mainClass="Shooting.BubbleShooterGame"
```

Or compile and run manually:
```bash
javac -d bin src/main/java/Shooting/*.java
java -cp bin Shooting.BubbleShooterGame
```

## Game Instructions

- **Objective:** Clear all bubbles from the screen by matching 3 or more bubbles of the same color
- **Controls:** 
  - Move mouse to aim
  - Click to shoot bubble
  - Match 3+ bubbles of the same color to pop them
  - Disconnected bubbles fall for bonus points
- **Scoring:**
  - 10 points × level for each bubble matched
  - 5 points × level for each floating bubble removed
- **Win:** Clear all bubbles to advance to the next level
- **Lose:** Bubbles reach the bottom

## Maven Commands Reference

| Command | Description |
|---------|-------------|
| `mvn clean` | Clean build artifacts |
| `mvn compile` | Compile source code |
| `mvn test` | Run tests |
| `mvn package` | Create JAR file |
| `mvn clean package` | Clean and create JAR |
| `mvn clean install` | Install to local repository |

## Project Details

- **Group ID:** com.shooting.game
- **Artifact ID:** bubble-shooter
- **Version:** 1.0.0
- **Main Class:** Shooting.BubbleShooterGame

## Features

- Beautiful animated graphics with gradients and effects
- Smooth 60 FPS gameplay
- Progressive difficulty (more colors as you level up)
- Score tracking and level progression
- Trajectory preview with wall bounce calculation
- Particle effects and animations
- Responsive UI with progress tracking

## License

This project is for educational purposes.
