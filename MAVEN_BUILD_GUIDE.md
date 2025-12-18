# Maven Build Instructions for Bubble Shooter Game

## Project Information
- **Project Name:** Bubble Shooter Game
- **Group ID:** com.shooting.game
- **Artifact ID:** bubble-shooter
- **Version:** 1.0.0
- **Main Class:** Shooting.BubbleShooterGame

## Directory Structure
```
BubbleShooter/
├── pom.xml                          # Maven configuration file
├── README.md                        # Project documentation
├── .gitignore                       # Git ignore file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── Shooting/
│   │   │       ├── Bubble.java                 # Bubble entity class
│   │   │       └── BubbleShooterGame.java      # Main game class
│   │   └── resources/                          # Resources (images, sounds, etc.)
│   └── test/
│       └── java/                               # Test files
└── target/                                     # Build output directory
    ├── BubbleShooter.jar                       # Simple JAR
    ├── BubbleShooter-jar-with-dependencies.jar # JAR with dependencies
    └── BubbleShooter-executable.jar            # Shaded executable JAR
```

## Prerequisites
1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

## Build Commands

### 1. Clean Previous Build
```bash
mvn clean
```
Removes the `target/` directory and all compiled files.

### 2. Compile Source Code
```bash
mvn compile
```
Compiles Java source files to `.class` files in `target/classes/`.

### 3. Run Tests
```bash
mvn test
```
Runs all unit tests in the `src/test/java/` directory.

### 4. Package Application (Create JAR)
```bash
mvn package
```
Creates JAR files in the `target/` directory.

### 5. Clean and Build
```bash
mvn clean package
```
Recommended: Cleans previous build and creates fresh JAR files.

### 6. Install to Local Repository
```bash
mvn install
```
Installs the JAR to your local Maven repository (~/.m2/repository).

### 7. Skip Tests During Build
```bash
mvn package -DskipTests
```
Builds the project without running tests (faster).

## Generated JAR Files

After running `mvn package`, three JAR files are created:

1. **BubbleShooter.jar**
   - Standard JAR file
   - Run with: `java -jar target/BubbleShooter.jar`

2. **BubbleShooter-jar-with-dependencies.jar**
   - Includes all dependencies (created by maven-assembly-plugin)
   - Run with: `java -jar target/BubbleShooter-jar-with-dependencies.jar`

3. **BubbleShooter-executable.jar**
   - Shaded JAR (created by maven-shade-plugin)
   - Recommended for distribution
   - Run with: `java -jar target/BubbleShooter-executable.jar`

## Running the Application

### Option 1: Run from JAR file
```bash
java -jar target/BubbleShooter-executable.jar
```

### Option 2: Run using Maven
```bash
mvn exec:java -Dexec.mainClass="Shooting.BubbleShooterGame"
```

### Option 3: Compile and Run manually
```bash
# Compile
javac -d bin src/main/java/Shooting/*.java

# Run
java -cp bin Shooting.BubbleShooterGame
```

## Maven Lifecycle Phases

| Phase | Description |
|-------|-------------|
| `validate` | Validate the project is correct |
| `compile` | Compile the source code |
| `test` | Run unit tests |
| `package` | Package compiled code into JAR |
| `verify` | Run integration tests |
| `install` | Install package to local repository |
| `deploy` | Deploy to remote repository |

## Common Maven Goals

| Goal | Command | Description |
|------|---------|-------------|
| Clean | `mvn clean` | Remove target directory |
| Compile | `mvn compile` | Compile source code |
| Test | `mvn test` | Run tests |
| Package | `mvn package` | Create JAR file |
| Install | `mvn install` | Install to local repo |
| Site | `mvn site` | Generate project website |

## Maven Plugins Used

### 1. Maven Compiler Plugin (v3.11.0)
- Compiles Java source code
- Configured for Java 11

### 2. Maven JAR Plugin (v3.3.0)
- Creates JAR file
- Configures main class in MANIFEST.MF

### 3. Maven Assembly Plugin (v3.6.0)
- Creates JAR with dependencies
- Useful for standalone distribution

### 4. Maven Shade Plugin (v3.5.1)
- Creates uber/fat JAR
- Bundles all dependencies into single JAR

### 5. Maven Surefire Plugin (v3.2.2)
- Runs unit tests
- Generates test reports

## Troubleshooting

### Issue: Maven not found
**Solution:** Add Maven to your PATH environment variable
```bash
# Windows
set PATH=%PATH%;C:\path\to\maven\bin

# Linux/Mac
export PATH=$PATH:/path/to/maven/bin
```

### Issue: Java version mismatch
**Solution:** Ensure JAVA_HOME points to JDK 11 or higher
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-11

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-11
```

### Issue: Build fails with "package does not exist"
**Solution:** Run `mvn clean` then `mvn package`

### Issue: JAR file won't run
**Solution:** 
1. Verify JAR was created: `ls target/*.jar`
2. Check Java version: `java -version`
3. Try different JAR: `java -jar target/BubbleShooter-executable.jar`

## Additional Maven Commands

### View Dependency Tree
```bash
mvn dependency:tree
```

### Update Dependencies
```bash
mvn versions:use-latest-versions
```

### Generate Project Documentation
```bash
mvn javadoc:javadoc
```

### Check for Newer Dependency Versions
```bash
mvn versions:display-dependency-updates
```

### Create Source JAR
```bash
mvn source:jar
```

### Run with Debug Output
```bash
mvn -X package
```

## Distribution

To distribute your application:
1. Use `BubbleShooter-executable.jar` (it's self-contained)
2. Ensure users have Java 11+ installed
3. Provide run instructions: `java -jar BubbleShooter-executable.jar`

## Next Steps

1. Add unit tests in `src/test/java/`
2. Add resources (images, sounds) in `src/main/resources/`
3. Configure CI/CD pipeline
4. Add JavaDoc comments
5. Create installer (using jpackage or launch4j)

## Support

For Maven issues, refer to:
- Maven Documentation: https://maven.apache.org/guides/
- Maven Repository: https://mvnrepository.com/
- Stack Overflow: https://stackoverflow.com/questions/tagged/maven
