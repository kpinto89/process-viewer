# Process Viewer

A Spring Boot + Thymeleaf web app that lists running system processes.

## Features

- Displays process PID, user, command, arguments, start time, and alive status.
- Server-side rendering with Thymeleaf.
- Search/filter by PID, user, command, or arguments.
- Sort by PID, user, command, start time, or alive status.
- Configurable auto-refresh interval (off, 5s, 10s, 30s).
- Summary cards (total, visible, alive, not alive).
- JSON endpoint: `/api/processes` with same query params as UI.

## Requirements

- Java 17+
- Maven 3.8+

## Run

```powershell
mvn clean spring-boot:run
```

Open `http://localhost:8080` in your browser.

### Query Parameters

- `q`: search text
- `sort`: `pid|user|command|start|alive`
- `dir`: `asc|desc`
- `refresh`: `0..60` seconds

Example:

```powershell
http://localhost:8080/?q=java&sort=command&dir=asc&refresh=10
```

## Test

```powershell
mvn test
```

