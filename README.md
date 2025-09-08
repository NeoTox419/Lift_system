
---
# Lift System — Elevator Simulation (Java + Spring Boot)

A backend-only elevator (lift) controller that demonstrates a real scheduling algorithm (**LOOK**) via simple REST APIs.
The app simulates one elevator in a building, accepts floor requests, and moves one floor per “tick” (auto every 5s or manual via API).
It includes input validation, concurrency safety, readable logs, and an interview-friendly `/status` output.

---

## Table of Contents
- [Why this project?](#why-this-project)
- [What it does](#what-it-does)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Manual Test Recipes](#manual-test-recipes)
- [How the LOOK algorithm works](#how-the-look-algorithm-works)
- [Edge Cases & Guarantees](#edge-cases--guarantees)
- [Notes for Interviewers](#notes-for-interviewers)
- [Future Enhancements](#future-enhancements)
- [License](#license)

---

## Why this project?
Elevators look simple but require careful **scheduling** to feel fast and fair. This project shows how to design such logic in a **clean backend**:
- Real algorithm (**LOOK**, inspired by OS disk scheduling)
- **REST APIs** with clear JSON responses
- **Thread safety** (HTTP requests vs. scheduled ticks)
- **Readable terminal logs** for demos

It’s a compact, resume-ready project that goes beyond CRUD and touches real system behavior.

---

## What it does
- Single elevator, floors `0..N` (you enter `N` at startup).
- Accepts floor requests; moves one floor per tick.
- Two ways to move time forward:
    - **Auto mode**: a scheduled job ticks every 5 seconds (with clear logs).
    - **Manual mode**: call `/api/lifts/tick` to advance one step.
- Implements **LOOK**:
    - Services stops **in the current direction** first.
    - Picks up **in-path** new requests without reversing.
    - Reverses **only** when the current direction has no more stops.
- Validates inputs and deduplicates repeated requests.

---

## Tech Stack
- **Java 21+**
- **Spring Boot 3**
- **Maven**
- Logging via `slf4j` (pretty ANSI logs for demo clarity)
- JUnit 5 tests for core LOOK behavior

---

## Project Structure
```
src/
  main/
    java/com/saikat/liftsystem/
      LiftsystemApplication.java
      controller/
        HealthController.java
        ConfigController.java
        ElevatorController.java
        AutoController.java
        ApiErrors.java
        DebugController.java        (optional debug view)
      jobs/
        AutoMoveJob.java            (auto tick + logs)
      model/
        Elevator.java               (LOOK scheduling)
        ElevatorDirection.java
        RequestAck.java             (response for /request)
        ElevatorStatusDto.java      (/status payload)
      service/
        ElevatorService.java        (validation + locking)
      util/
        Ansi.java                   (console colors/icons)
  test/
    java/com/saikat/liftsystem/model/
      ElevatorTest.java             (LOOK unit tests)
```

---

## Getting Started

### Prerequisites
- Java **21+**
- Maven **3.9+**

### Run
```bash
mvn spring-boot:run
```

On startup you’ll see:
```
Enter the number of floors in the building (>=2):
```
Type e.g. `12`. The service then listens on `http://localhost:8080`.

**Auto logs** print every 5s, for example:
```
=========== LIFT STATUS (AUTO TICK) ===========
Current floor : 3
Direction     : ▲ UP
Next stop     : 5
Pending stops : 2
===============================================
```

> Tip: You can pause/resume auto mode via endpoints below.

---

## API Endpoints

### Core
```
GET  http://localhost:8080/api/health
GET  http://localhost:8080/api/config
```

### Elevator
```
GET|POST http://localhost:8080/api/lifts/request/{floor}
GET|POST http://localhost:8080/api/lifts/tick
GET       http://localhost:8080/api/lifts/status
```

**Examples**

`/api/lifts/request/{floor}` returns:
```json
{ "accepted": true, "reason": "ok", "floor": 5 }
```
or
```json
{ "accepted": false, "reason": "duplicate", "floor": 5 }
```
or
```json
{ "accepted": false, "reason": "already-here", "floor": 0 }
```

`/api/lifts/status` returns:
```json
{
  "id": 1,
  "currentFloor": 3,
  "direction": "UP",
  "nextStop": 5,
  "pendingStops": 2
}
```

### Auto Mode
```
GET  http://localhost:8080/api/auto/status
POST http://localhost:8080/api/auto/pause
POST http://localhost:8080/api/auto/resume
```

### Debug (optional, for interviews)
```
GET  http://localhost:8080/api/lifts/debug
```
Example:
```json
{
  "id": 1,
  "currentFloor": 3,
  "direction": "UP",
  "upStops": [5, 7, 9],
  "downStops": [2],
  "nextStop": 5,
  "pendingStops": 4
}
```

---

## Manual Test Recipes

### Basic
```bash
# Request floor 3, then 1 (demonstrates reverse after finishing UP)
curl http://localhost:8080/api/lifts/request/3
curl http://localhost:8080/api/lifts/request/1
```

### Sequential (2 → 4 → 6)
```bash
curl http://localhost:8080/api/lifts/request/2
curl http://localhost:8080/api/lifts/request/4
curl http://localhost:8080/api/lifts/request/6
```
Expected: services 2 → 4 → 6 in order while moving UP.

### In-path Pickup (go in same direction)
- From 3 → 7, call 5; it should stop at 5 en route without reversing.

### Reverse Direction
- While going DOWN to 2, request 9; it finishes 2, then reverses to 9.

### Closest-First (in current direction)
- Request 9, 6, 8 quickly; order should be 6 → 8 → 9.

### Edge Cases
```bash
curl http://localhost:8080/api/lifts/request/0     # already here (if starting at 0)
curl http://localhost:8080/api/lifts/request/999   # 400 out of range
```

---

## How the LOOK algorithm works
- Maintain two ordered sets of stops:
    - `upStops` for floors **above** the current floor (ascending order).
    - `downStops` for floors **below** the current floor (descending traversal).
- While moving **UP**:
    - Always head to the **nearest higher** stop next.
    - New higher requests are picked up **in path**.
    - When no higher stops remain, **reverse** if there are lower stops.
- While moving **DOWN**: symmetric behavior.
- When **IDLE**: choose the closer of the nearest up/down stop (ties prefer UP).
- **One floor per tick** keeps logs and demos intuitive.

Why LOOK over FIFO?
- Reduces wasted travel.
- Naturally handles “go in same direction” and “reverse after finishing”.

---

## Edge Cases & Guarantees
- **Out of range** floors (e.g., `-1`, `200` in a 12-floor building) → HTTP 400 with a friendly JSON error.
- **Current floor** request → `{ accepted: false, reason: "already-here" }`, no movement.
- **Duplicate** request for the same floor → ignored with `{ accepted: false, reason: "duplicate" }`.
- When queue is empty → direction is **IDLE** (low-power waiting state).
- Concurrency: `/request` and ticks are **synchronized** to avoid races.

---

## Notes for Interviewers
- This project demonstrates **REST API design**, **scheduling algorithms**, and **safe concurrency** in Spring Boot.
- `/status` is intentionally **interview-friendly** (shows `currentFloor`, `direction`, `nextStop`, `pendingStops`).
- Terminal logs use ▲/▼/■ and colors to make the demo self-explanatory.

---

## Future Enhancements
- Multiple elevators + dispatcher strategy (nearest, load balancing).
- Persistence (store requests/history).
- Door open/close timing, capacity limits, multi-rider stops.
- Metrics endpoint (avg wait time, utilization).
- Dockerfile + CI tests.

