# 🚀 Ocean Probe Navigation Service
## 📌 Overview 
This service provide APIs Register a probe and control the probe navigation using set of commands.

---

## 📦 Features 
✅ **Register A Probe with navigation controller service** : Register and create Probe device identity.

✅ **Ocean floor representation with obstacles as 2D grid** : Ocean floor represented as 2D grid.

✅ **Move the probe in directions** : Move forwards and backwards.

✅ **Change directions** : Turn left and right.

✅ **Avoid obstacles** : Avoid obstacles in the grid.

✅ **Stay on the grid**

✅ **Print navigation trail** : Print a summary of the co-ordinates visited.

---

## 🚀 Quick Start Guide
### **1️⃣ Prerequisites**
Ensure you have the following installed:
- [JDK 17+](https://adoptium.net/)
- [Maven 3+](https://maven.apache.org/)

### **2️⃣ Clone the Repository**
```sh 
git clone git@github.com:jabeevulla/kata-probe-navigation-service.git
cd kata-probe-navigation-service

```
### **4️⃣ Run the Application**
```sh
mvn clean install
mvn spring-boot:run
```

### **5️⃣ Test the API**
```sh
TODO - Add CURL
```

### **6️⃣ Access Local Database**
-	Start your Spring Boot application.
-	Open the browser and go to:
👉 http://localhost:8080/h2-console
-	Enter the following credentials:
-	JDBC URL: jdbc:h2:mem:probe_db
-	Username: sa
-	Password: password

---
### **🛠️ Project Structure**
```
├── HELP.md
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── codekata
│   │   │           └── oceanprobe
│   │   │               └── probenavigationservice
│   │   │                   ├── KataProbeNavigationServiceApplication.java
│   │   │                   ├── controller
│   │   │                   ├── dto
│   │   │                   ├── entity
│   │   │                   ├── exception
│   │   │                   ├── repository
│   │   │                   └── service
│   │   └── resources
│   │       ├── application.properties
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── codekata
│                   └── oceanprobe
│                       └── probenavigationservice
│                           └── KataProbeNavigationServiceApplicationTests.java

```

---

### **✅ Running Tests**
Run **unit & integration** tests:

```sh
mvn test
```

---
### **📦 Deployment**
#### **TODO**: 

---

## 🛠️ System design

### **Problem statement**
Develop a REST API that allows control of the probe from the surface using a set of interpreted controls.
End users are a team that explores the bottom of the sea using a remotely controlled submersible probe.

---

### **Solution Overview** 
The system scopes the implementation of basic navigation controller service for remotely operating the probe on predefined ocean floor. 
- **Probe controller UI** integrate with **Probe controller API** to send commands
- **Probe controller API** validate maneuver command by checking **Probe** presence in **Database**
- **Probe controller API** computes the new position and checks for obstacles 
- **Probe controller API** moves **Probe** by storing command into database and **Command Queue**
- **Probe Command Stream Handler** orchestrate commands to Probe device and updates database for status of command
- **Probe controller API** returns **Navigation Trail** and **new position** if command is valid and next move is not an obstacle

**Note:** The diagrams are Mermaid.js diagram. Copy the script to [mermaid.live](https://mermaid.live/) visualise the diagram.

**High level Solution diagram**
```mermaid
%%{init: {"flowchart": {"htmlLabels": false}} }%%
flowchart LR
    controllerUI["`Probe controller UI`"]
    controllerAPI["`Probe controller API`"]
    probeStore[(**OceanStore**
    OceanFloot, Probes,
    NavHistory
    )]
    navigationQueue[[Navigation Commands Queue]]
    ProbeDevice["`Probe Device`"]
    ProbeWebSocketHandler["`ProbeCommandStreamHandler`"]
    
    controllerUI --> controllerAPI
    controllerAPI --> probeStore
    controllerAPI --> navigationQueue
    ProbeWebSocketHandler --> navigationQueue
    ProbeDevice --> ProbeWebSocketHandler
    ProbeWebSocketHandler --> probeStore
```

**Sequence Diagram** 
```mermaid
sequenceDiagram
    title Probe Maneuver Workflow

    participant UI as Probe Controller UI
    participant API as Probe Controller API
    participant DB as Database
    participant Queue as Command Queue
    participant StreamHandler as Probe Command Stream Handler
    participant Probe as Probe Device

    UI ->> API: Send Maneuver Command
    API ->> DB: Validate Probe Presence
    DB -->> API: Probe Exists ✅ / ❌ Not Found
    API ->> DB: Pull Ocean floor grid
    API ->> API: Compute New Position
    API ->> API: Check for Obstacles
    API ->> DB: Store Command in Database with status Failed ❌ if Obstacle was Found 🚧
    alt is ✅ Path Clear
        API ->> Queue: Push Command to Command Queue
        API -->> UI: Return Navigation Trail & New Position (if valid)
    else is Obstacle Found 🚧
        API-->> UI: Move can not be completed. Obstacle Found 🚧. Stay in the same position.
    end

    Note over Queue, StreamHandler: Command Queue Processes Movement

    StreamHandler ->> Probe: Send Command to Probe Device
    Probe -->> StreamHandler: Acknowledge Movement / Failure
    StreamHandler ->> DB: Update Probe Position & Command Status
```
---
### [**📦 Features**](#features)

| **Feature ID** | **Feature**                                            | **Details**                                        |
|----------------|--------------------------------------------------------|----------------------------------------------------|
| **F-001**      | ✅ Register A Probe with navigation controller service  | Register and create Probe device identity.         |
| **F-002**      | ✅ Ocean floor representation with obstacles as 2D grid | Ocean floor represented as a 2D grid.              |
| **F-003**      | ✅ Move the probe in directions                         | Move forwards and backwards.                       |
| **F-004**      | ✅ Change directions                                    | Turn left and right.                               |
| **F-005**      | ✅ Avoid obstacles                                      | Avoid obstacles in the grid.                       |
| **F-006**      | ✅ Stay on the grid                                     | Ensure the probe does not leave the grid boundary. |
| **F-007**      | ✅ Print navigation trail                               | Print a summary of the coordinates visited.        |

---

### **💬 Assumptions**
#### **Requirements assumptions** 
- The predefined ocean floor as grid is a static configuration in the backend 
- Surface controlling unit and probe device unit assumed to be integrated/bound via backend system
- Probe device have intelligence to return to base location using navigation history in the event of probe loosing connection with backend service 
#### **Solution design assumptions**
- Security assumption : Security is not scoped into this design assuming that over all probe integration platform will have security implemented. For example: Authentication and Authorisation, API gateways etc.
- For the purpose of enabling this service testing, simple probe registration api is implemented. This should ideally be separate system all together for probe device management.

---
## **📽️ Low level design**
The navigation controller system is designed to be REST API application, providing APIs for various probe controlling operations. 

### **💻 Tech stack**

| **Purpose**                     | **Recommended Technology**        | **Technology Used in Design** |
|---------------------------------|-----------------------------------|-------------------------------|
| **Streaming & Event Queue**     | Kafka, RabbitMQ                   | BlockingQueue (Java)          |
| **REST API Framework**          | Spring Boot                       | Spring Boot                   |
| **Database**                    | PostgreSQL, MySQL                 | H2 (In-Memory)                |
| **Real-time Updates**           | Kafka streaming                   | WebSockets                    |
| **API Documentation**           | OpenAPI (Swagger), Postman        | Swagger                       |
| **Infrastructure & Deployment** | Kubernetes, Docker                | Spring Boot embedded Tomcat   |
| **Configuration Management**    | Spring Config, Consul             | application.yml               |
| **Logging & Monitoring**        | ELK Stack, Prometheus + Grafana   | Spring Boot Logs              |
| **Authentication & Security**   | OAuth2, Keycloak, Spring Security | N/A                           |

### **💽 Data structure** 
#### **OCEAN FLOOR**: 

Ocean floor is represented as 2D grid.

The grid is represented as a 2D matrix, where:
-	0 = Open space
-	1 = Obstacle (wall)
-	S = Start point
-	E = End point

Example: 
```txt
S  0  1  0  0
0  1  0  1  0
0  0  0  1  E
```
**Implementation:**

```java
int[][] grid = {
    {0, 0, 1, 0, 0},
    {0, 1, 0, 1, 0},
    {0, 0, 0, 1, 0}
};
```
#### **DIRECTIONS**
Directions are represented as 2D array.
Each entry in the DIRECTIONS array represents a movement (Row, Column):

**🔄 Directional Movement Table**

| **Direction** | **Row (x)** | **Column (y)** | **Meaning**                  |
|---------------|-------------|----------------|------------------------------|
| **Right**     | `0`         | `+1`           | Move right (increase column) |
| **Down**      | `+1`        | `0`            | Move down (increase row)     |
| **Left**      | `0`         | `-1`           | Move left (decrease column)  |
| **Up**        | `-1`        | `0`            | Move up (decrease row)       |

**Implementation:**

```java
// Directions for moving Up, Down, Left, Right
private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
```

### **📂Database Design**
```mermaid
erDiagram

    PROBES {
        UUID id PK
        VARCHAR(255) name
        INT x_position
        INT y_position
        VARCHAR(10) direction
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    NAVIGATION_TRAIL {
        UUID id PK
        UUID probe_id FK
        INT x_position
        INT y_position
        VARCHAR(10) direction
        TIMESTAMP moved_at
    }

    OCEAN_FLOOR {
        INT id PK
        INT x_position
        INT y_position
        BOOLEAN is_obstacle
    }

    PROBES ||--o{ NAVIGATION_TRAIL : "has many"
    NAVIGATION_TRAIL }|--|| PROBES : "belongs to"
```

### **🔁 Algorithms Used**
No search algorithms are considered as all the options required for features deal with precise know location (Current position).

### **🔄 Data Flow** 
TODO


### 📚 API Documentation
- Swagger UI: [API documentation](http://localhost:8080/swagger-ui.html)

#### **🚀 Register Probe API**

This API allows registering a new **probe** in the ocean navigation system. The probe will be assigned a unique ID and initial position.

---
**👉 API Endpoint**
```
POST /api/v1/probes/register
```

**👉 Request Body**
```json
{
  "name": "Explorer-1",
  "xPosition": 0,
  "yPosition": 0,
  "direction": "NORTH"
}
```

**👉 Request Parameters**

| Parameter   | Type     | Description                                                 | Required |
|-------------|----------|-------------------------------------------------------------|----------|
| `name`      | `string` | Name of the probe                                           | ✅ Yes    |
| `xPosition` | `int`    | Initial X position on the grid                              | ✅ Yes    |
| `yPosition` | `int`    | Initial Y position on the grid                              | ✅ Yes    |
| `direction` | `enum`   | Initial facing direction (`NORTH`, `SOUTH`, `EAST`, `WEST`) | ✅ Yes    |

**👉 Response**

✅ **Success Response (201 Created)**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Explorer-1",
  "xPosition": 0,
  "yPosition": 0,
  "direction": "NORTH",
  "status": "ACTIVE"
}
```

❌ **Error Responses**

| HTTP Status       | Error Message               | Reason                                  |
|-------------------|-----------------------------|-----------------------------------------|
| `400 Bad Request` | `Invalid input data`        | Missing or incorrect request parameters |
| `409 Conflict`    | `Probe name already exists` | A probe with the same name exists       |

---

#### 🚀 Probe Navigation API Design

The **Probe Navigation API** enables controlling a probe within a 2D grid environment. The probe can:
- Move **forwards** and **backwards**.
- Turn **left** and **right**.
- **Stay within the grid boundaries**.
- **Avoid obstacles** placed in the grid.
- **Log its navigation trail** (visited coordinates).

---

#### 📍 **API Endpoints**

🔹 **Move Probe**

**Endpoint:**
```http
POST /api/v1/probes/{probeId}/move
```

**Request Body:**
```json
{
  "commands": ["F", "L", "B", "R"]
}
```

**Command Options:**

| Command | Action        |
|---------|---------------|
| `F`     | Move Forward  |
| `B`     | Move Backward |
| `L`     | Turn Left     |
| `R`     | Turn Right    |

**Response:**

```json
{
  "probeId": "uuid",
  "finalPosition": { "x": 2, "y": 3 },
  "finalDirection": "NORTH",
  "navigationTrail": [
    { "x": 1, "y": 1 },
    { "x": 1, "y": 2 },
    { "x": 2, "y": 2 },
    { "x": 2, "y": 3 }
  ],
  "status": "COMPLETED"
}
```

---

#### ⚙️ **Probe Movement Logic**

1️⃣ **Direction & Movement Rules**

| Direction | `F` (Forward) | `B` (Backward) |
|-----------|---------------|----------------|
| `NORTH`   | (x, y+1)      | (x, y-1)       |
| `SOUTH`   | (x, y-1)      | (x, y+1)       |
| `EAST`    | (x+1, y)      | (x-1, y)       |
| `WEST`    | (x-1, y)      | (x+1, y)       |

2️⃣ **Turning Logic**

| Current Direction | `L` (Turn Left) | `R` (Turn Right) |
|-------------------|-----------------|------------------|
| `NORTH`           | `WEST`          | `EAST`           |
| `SOUTH`           | `EAST`          | `WEST`           |
| `EAST`            | `NORTH`         | `SOUTH`          |
| `WEST`            | `SOUTH`         | `NORTH`          |

3️⃣ **Boundary & Obstacle Checks**
- The probe **cannot move outside the grid**.
- If an **obstacle is detected**, the probe **stops at the last valid position**.
- The **API response should indicate if a move was blocked due to an obstacle**.

---

#### 🛑 **Edge Cases & Validations**
- ✅ **Invalid Commands:** Return `400 Bad Request`.
- ✅ **Probe Not Found:** Return `404 Not Found`.
- ✅ **Move Into Obstacle:** Stop movement & return `422 Unprocessable Entity`.
- ✅ **Out-of-Bounds Moves:** Reject with `400 Bad Request`.

---

# 🛠️ Test Approach Documentation

## 📌 Overview
This document outlines the **testing strategy** for the **Probe Maneuver System**, ensuring all components are tested for **functionality, performance, integration, and reliability**.

---

## ✅ Test Strategy
| **Test Type**           | **Description**                                                                                  |
|-------------------------|--------------------------------------------------------------------------------------------------|
| **Unit Testing**        | Tests individual methods and logic in **Controller, Service, and Repository layers**.            |
| **Integration Testing** | Ensures **API endpoints, database interactions, and event queues** work together correctly.      |
| **System Testing**      | Validates end-to-end flow for **Probe registration, movement, and navigation trail generation**. |
| **Performance Testing** | Out of scope: Recommended to capture this for real use case.                                     |
| **Security Testing**    | Out of scope: Recommended to capture this for real use case.                                     |
| **Regression Testing**  | Out of scope: Recommended to capture this for real use case.                                     |

---

### 🧪 **Unit & Integration Testing Approach**
#### **✅ Scope**
- Test individual **methods in the Service layer**.
- Test individual API **Controller layer**.
- Ensure **pathfinding logic** correctly computes new positions.
- Validate **probe movement restrictions** (boundaries, obstacles).

#### **🛠️ Tools & Frameworks**
- **JUnit 5**
- **Mockito** (for mocking dependencies)
- **Spring Boot Test** (for unit + integration tests)

#### **📝 Example Unit Test Case**
```java
TODO
```

### 🧪 **System Testing Approach**
#### **✅ Scope**
- Test APIs using CURL/ Postman. 
- Recommended to write automation API testing using Karate or similar framework

### 📜 API Versioning

**📌 URL Path Versioning (Used in System)**
- Example: API versions are included in the URL, e.g., /api/v1/probes/move
---

### 📌 Requirement Traceability Matrix (RTM)

#### 📦 Overview
A **Requirement Traceability Matrix (RTM)** ensures that all business requirements are mapped to **work items, test cases, and builds**, providing full traceability and verification across the software development lifecycle.

In this design, we document RTM within the **README** for simplicity. However, the **recommended approach** is to use **advanced tools** for better tracking, automation, and integration.

---

#### 🔗 **Recommended RTM Implementation Approach**
| **Category**                | **Recommended Tool**       | **Purpose**                                   |
|-----------------------------|----------------------------|-----------------------------------------------|
| **Requirements Management** | IBM DOORS Next, Confluence | Captures business capabilities & requirements |
| **Project Management**      | Jira, Azure DevOps         | Tracks work items (Epics, Features, Stories)  |
| **Test Management**         | TestRail                   | Manages test cases & execution                |
| **CI/CD & Automation**      | GitHub Actions, Jenkins    | Automates RTM generation & validation         |

---

#### 🔄 **RTM Structure in the Design**
For this project, we will structure RTM as follows:

| **Business Capability**  | **Business Requirement**                                  | **Work Item (Epic, Feature, Story)**                                                                  | **Test Case**                                    | **Build**                  | **Status**    |
|--------------------------|-----------------------------------------------------------|-------------------------------------------------------------------------------------------------------|--------------------------------------------------|----------------------------|---------------|
| **Probe Navigation**     | "Register a probe"                                        | Epic: **Register Probe** → Feature: **API for registration** → Story: **POST /api/probes/register**   | TC-001: Register a probe successfully            | Register Probe API         | ✅ Completed   |
| **Probe Maneuver**       | "Move probe while avoiding obstacles"                     | Epic: **Maneuver Probe** → Feature: **Obstacle detection** → Story: **Compute movement path**         | TC-002: Move probe, <br/>TC-003: Detect obstacle | Prove navigation API /move | ✅ Completed   |
| **Navigation Trail**     | "Retrieve navigation history"                             | Epic: **Probe Navigation Trail** → Feature: **Store path history** → Story: **GET /api/probes/trail** | TC-004: Validate correct trail history           | Prove navigation API /move | ✅ Completed   |
| **Navigation streaming** | "Probe device should consume controller commands and move | Epic: **Probe device integration**                                                                    | ❌ OUT OF SCOPE                                   | ❌️OUT OF SCOPE             | ❌OUT OF SCOPE |


## 🚨 Known Issues

### 🛑 **1. Register Probe is failing due to data persistence error.**
#### **Issue Summary:**
- This error occurs when **multiple transactions** try to **update the same entity** simultaneously
- Reason could be around defining joins in the repository classes 

#### **Error Log:**
```sh
org.springframework.orm.ObjectOptimisticLockingFailureException: 
Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): 
[com.codekata.oceanprobe.probenavigationservice.entity.Probe#76916b63-9b55-4d53-b37d-da43dd844f8d]