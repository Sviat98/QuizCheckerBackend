# QuizChecker Backend

A Ktor-based backend server for QuizChecker application with PostgreSQL database integration.

## Features

- RESTful API with Ktor
- PostgreSQL database with Exposed ORM
- HikariCP connection pooling
- Koin dependency injection
- JSON serialization with kotlinx.serialization
- CORS support
- Call logging and monitoring
- Status pages for error handling

## Tech Stack

- **Kotlin**: 1.9.22
- **Ktor**: 2.3.7
- **Koin**: 3.5.3 (Dependency Injection)
- **Exposed**: 0.46.0 (ORM)
- **HikariCP**: 5.1.0 (Connection Pooling)
- **PostgreSQL**: 42.7.1
- **Logback**: 1.4.14

## Project Structure

```
QuizCheckerBackend/
├── gradle/
│   ├── libs.versions.toml          # Version catalog
│   └── wrapper/
├── src/main/
│   ├── kotlin/com/bashkevich/quizcheckerbackend/
│   │   ├── Application.kt          # Main application entry point
│   │   ├── di/
│   │   │   └── AppModule.kt        # Koin DI module
│   │   ├── plugins/
│   │   │   ├── DependencyInjection.kt
│   │   │   ├── Serialization.kt
│   │   │   ├── HTTP.kt
│   │   │   ├── Monitoring.kt
│   │   │   └── Routing.kt
│   │   ├── routes/
│   │   │   ├── HealthRoutes.kt
│   │   │   └── UserRoutes.kt
│   │   ├── data/
│   │   │   ├── DatabaseFactory.kt
│   │   │   ├── models/
│   │   │   │   └── User.kt
│   │   │   └── repositories/
│   │   │       └── UserRepository.kt
│   │   └── services/
│   │       └── UserService.kt
│   └── resources/
│       ├── application.yaml        # Application configuration
│       └── logback.xml            # Logging configuration
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Prerequisites

- JDK 11 or higher
- PostgreSQL 12 or higher
- Gradle (wrapper included)

## Database Setup

1. Install PostgreSQL if not already installed

2. Create a database:
```sql
CREATE DATABASE quizchecker;
```

3. Update database credentials in `src/main/resources/application.yaml`:
```yaml
storage:
  driverClassName: "org.postgresql.Driver"
  jdbcURL: "jdbc:postgresql://localhost:5432/quizchecker"
  user: "your_username"
  password: "your_password"
```

The application will automatically create the necessary tables on startup.

## Running the Application

### Using Gradle Wrapper (Recommended)

On Windows:
```bash
gradlew.bat run
```

On Unix/Linux/macOS:
```bash
./gradlew run
```

### Development Mode

To run with auto-reload on file changes:
```bash
./gradlew run --continuous
```

The server will start at `http://localhost:8080`

## API Endpoints

### Health Check

- **GET** `/health`
  - Returns server health status
  - Response: `{"status": "UP", "message": "QuizChecker Backend is running"}`

### Basic API

- **GET** `/api/hello`
  - Returns a hello message with timestamp
  - Response: `{"message": "Hello from QuizChecker Backend!", "timestamp": 1234567890}`

- **POST** `/api/echo`
  - Echoes back the message sent
  - Request: `{"message": "your message"}`
  - Response: `{"echo": "your message"}`

### User Management

- **GET** `/api/users`
  - List all users
  - Response: Array of user objects

- **GET** `/api/users/{id}`
  - Get user by ID
  - Response: User object

- **POST** `/api/users`
  - Create a new user
  - Request: `{"name": "John Doe", "email": "john@example.com"}`
  - Response: Created user object with ID

- **PUT** `/api/users/{id}`
  - Update an existing user
  - Request: `{"name": "Jane Doe", "email": "jane@example.com"}`
  - Response: Updated user object

- **DELETE** `/api/users/{id}`
  - Delete a user
  - Response: `{"message": "User deleted successfully"}`

## Testing with curl

### Health Check
```bash
curl http://localhost:8080/health
```

### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'
```

### Get All Users
```bash
curl http://localhost:8080/api/users
```

### Get User by ID
```bash
curl http://localhost:8080/api/users/1
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane Doe", "email": "jane@example.com"}'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Building for Production

### Create a fat JAR
```bash
./gradlew buildFatJar
```

The JAR file will be created in `build/libs/`

### Run the JAR
```bash
java -jar build/libs/QuizCheckerBackend-all.jar
```

## Configuration

### Application Configuration
Edit `src/main/resources/application.yaml` to configure:
- Server port and host
- Database connection
- Module loading

### Logging Configuration
Edit `src/main/resources/logback.xml` to configure:
- Log levels
- Log format
- Log output destination

## Development

### Adding New Routes

1. Create a new route file in `src/main/kotlin/com/bashkevich/quizcheckerbackend/routes/`
2. Define your routes using Ktor's routing DSL
3. Register the routes in `plugins/Routing.kt`

### Adding New Dependencies

1. Add the dependency to `gradle/libs.versions.toml`:
```toml
[versions]
my-library = "1.0.0"

[libraries]
my-library = { module = "com.example:my-library", version.ref = "my-library" }
```

2. Add it to `build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.my.library)
}
```

## Troubleshooting

### Port already in use
If port 8080 is already in use, change it in `application.yaml`:
```yaml
ktor:
  deployment:
    port: 8081
```

### Database connection issues
- Verify PostgreSQL is running
- Check database credentials in `application.yaml`
- Ensure the database exists
- Check firewall settings

### Build issues
- Clean the build: `./gradlew clean`
- Refresh dependencies: `./gradlew build --refresh-dependencies`

## License

This project is licensed under the MIT License.

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
