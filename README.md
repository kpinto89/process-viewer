# Process Viewer

A Spring Boot + Thymeleaf web app that lists running system processes.

## Features

- Displays process PID, user, command, arguments, start time, and alive status.
- Server-side rendering with Thymeleaf.
- Auto-refreshes every 5 seconds.

## Requirements

- Java 17+
- Maven 3.8+

## Run

```powershell
mvn clean spring-boot:run
```

Open `http://localhost:8080` in your browser.

## Test

```powershell
mvn test
```

