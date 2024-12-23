# Parking System Documentation

This **Markdown documentation** covers the design, file structure, roles, methods, concurrency handling, and optimization techniques of our **Java-based Parking System**.

---

## 1. **Overview**

This project demonstrates a **parking management** application that handles different types of **vehicles** (moto, car, van) and different **parking spots** (moto spot, car spot, big spot). It includes:

- A **domain** layer (`Parking`) that manages lists of parking spots.
- A **service** layer (`ParkingService`) that provides the business logic (parking/unparking).
- **Models** (`Vehicle` and its subclasses, `ParkingSpot` and its subclasses) that represent vehicles and spots.
- A **main** class to illustrate usage.
- **Unit tests** to validate the functionality.

We’ve introduced **concurrency** management (via a `ConcurrentHashMap` and a synchronized block) to ensure atomic operations when multiple threads try to park or unpark vehicles simultaneously.

---

## 2. **Project Architecture**

The **architecture** is structured in a **multi-layer** approach:

1. **Models** (`org.example.parking.model`):
    - Define abstract classes (`Vehicle`, `ParkingSpot`) and concrete classes (`Moto`, `Car`, `Van`, `MotoSpot`, `CarSpot`, `BigSpot`).
    - Each class encapsulates the logic specific to that entity (e.g., can a `Van` park on a `MotoSpot`?).

2. **Domain** (`org.example.parking.domain`):
    - Contains `Parking`, which aggregates lists of spots (moto, car, big).
    - Offers helper methods like `isFull()`, `getFreeSpotsCount()`, `areMotoSpotsFull()`, etc.

3. **Service** (`org.example.parking.service`):
    - Contains `ParkingService` for **business logic**:
        - How to park a vehicle, which spots to occupy,
        - how to free them (`unparkVehicle`),
        - concurrency handling (`ConcurrentHashMap`, synchronization).

4. **Main** (`org.example.parking.Main`):
    - Entry point demonstrating how to use the `Parking` and `ParkingService`.
    - Shows how vehicles are created and parked, and prints info like free spots.

5. **Tests** (`parking.service.ParkingServiceTest`):
    - JUnit tests to ensure correctness across various scenarios (van occupying 3 spots, fallback logic for moto, etc.).

---

## 3. **Directory Structure**


- **`model/`**: Entities (vehicles, spots)
- **`domain/`**: The `Parking` aggregate
- **`service/`**: Logic to park/unpark vehicles
- **`Main.java`**: Demonstration program
- **`ParkingServiceTest.java`**: Unit tests

---

## 4. **Roles of Files**

### 4.1. **Models** (in `org.example.parking.model`)

1. **`Vehicle`** (abstract):
    - Has a `plateNumber`
    - Defines `getRequiredCarSpots()`, `canParkOnMotoSpot()`, etc.
    - Subclasses: `Moto`, `Car`, `Van`.

2. **`Moto`, `Car`, `Van`**:
    - Each implements the `Vehicle` methods to specify:
        - How many car spots they need (Van = 3, Car = 1, Moto = 1),
        - Whether they can park on moto/car/big spots.

3. **`ParkingSpot`** (abstract):
    - Has an `id` and a boolean `isOccupied`.
    - `occupy()` sets the spot as occupied, `free()` releases it.
    - Subclasses: `MotoSpot`, `CarSpot`, `BigSpot`.

4. **`MotoSpot`, `CarSpot`, `BigSpot`**:
    - Each overrides `canFitVehicle(Vehicle v)` to check compatibility with the spot type and `isOccupied()`.

### 4.2. **Domain** (`Parking.java`)

- Manages 3 lists: `motoSpots`, `carSpots`, `bigSpots`.
- Creates the spots in its constructor (e.g., `new MotoSpot("M-0")`).
- Methods to check the global or type-specific occupancy:
    - `getFreeSpotsCount()`, `isFull()`, `isEmpty()`
    - `areMotoSpotsFull()`, `areCarSpotsFull()`, `areBigSpotsFull()`

### 4.3. **Service** (`ParkingService.java`)

- **Core business logic** for:
    - `parkVehicle(...)`: decides how to park a vehicle, tries moto/car/big fallback, or 3 car spots for a van.
    - `unparkVehicle(...)`: frees the spots from a stored map.
- Uses a **`ConcurrentHashMap<Vehicle, List<ParkingSpot>>`** (`vehicleToSpots`) to track which spots each vehicle occupies.
- Introduces a **lock** (`private final Object lock`) to synchronize the entire park/unpark logic, ensuring that a van won’t occupy 2 out of 3 spots if a concurrent thread intervenes.
- `getNumberOfSpotsOccupiedByVans()` to count how many spots are taken by vans in total.

### 4.4. **Main** (`Main.java`)

- Demonstrates usage:
    1. Creates a new `Parking` object (e.g., 2 moto spots, 5 car spots, 2 big spots).
    2. Creates a `ParkingService`.
    3. Instantiates various vehicles (`Moto`, `Car`, `Van`).
    4. Calls `parkVehicle` and prints the results (success/failure).
    5. Shows updated parking info (free spots, `isFull()`, etc.).
    6. Unparks a vehicle (van2) to illustrate freeing spots.

### 4.5. **Tests** (`ParkingServiceTest.java`)

- Uses **JUnit** for multiple scenarios:
    - **Moto** on moto spot
    - **Car** on car spot
    - **Van** on big or multiple car spots
    - Fallback behavior (e.g., Moto occupying car/big if no moto spot)
    - Boundary cases (parking with 0 spots, parking being full/empty)
    - Atomicity test: unpark a vehicle and ensure spots are freed.

---

## 5. **Key Methods**

1. **`Parking`**:
    - `getFreeSpotsCount()`, `getTotalSpots()`, `isFull()`, `isEmpty()`
    - `areMotoSpotsFull()`, `areCarSpotsFull()`, `areBigSpotsFull()`
2. **`ParkingService`**:
    - `parkVehicle(Parking, Vehicle)`: tries different spot types, atomic via `synchronized(lock)`.
    - `unparkVehicle(Vehicle)`: frees the spots and removes entry from the map.
    - `tryParkOnSpots(...)`: tries single-capacity spots.
    - `tryParkOnBigSpot(...)`: tries one big spot.
    - `tryParkVanOnCarSpots(...)`: tries multiple car spots for a van (3 by default).
    - `getNumberOfSpotsOccupiedByVans()`: sums up all spots used by vans.

---

## 6. **Techniques Used for Optimization & Concurrency**

1. **Concurrency**:
    - Replaced the plain `HashMap` with a **`ConcurrentHashMap`** for thread-safe reads and better performance under read concurrency.
    - Added a **`lock`** object to **synchronize** critical sections (`parkVehicle` and `unparkVehicle`), ensuring **atomic** updates. This prevents partial spot occupation for a van if another thread intervenes.

2. **Fallback Logic**:
    - A `Moto` first tries `motoSpots`, then `carSpots`, finally `bigSpots`.
    - A `Car` tries `carSpots` then `bigSpots`.
    - A `Van` tries a single `bigSpot` or else 3 `carSpots`.

3. **Modular design**:
    - Classes are short and cohesive: each `Vehicle` or `Spot` type has its own logic in a small file.

4. **Readability & Extensibility**:
    - The code respects **SOLID** principles. New vehicle types can be added with minimal changes.
    - **Unit Tests** cover typical and edge scenarios.

5. **Synchronized** approach:
    - Ensures **one** operation at a time in `parkVehicle` or `unparkVehicle`, guaranteeing data consistency.

---

## 7. **Possible Future Improvements**

- **Finer-grained locks** (e.g., `ReentrantLock` or `ReadWriteLock`) for more concurrency if the application handles many reads with fewer writes.
- **Sharding** the parking: multiple `Parking` instances for extremely large numbers of spots.
- **Database integration**: use a transactional system to store and manage spots if you need persistence at scale.
- **More advanced fallback**: e.g., bus occupying 2 big spots, etc.

---

## 8. **Potential Future Extensions**

Here are some additional features and architectural ideas that could further **enhance** the scalability, robustness, and functionality of the Parking System:

1. **Advanced Concurrency Control**
    - **Read/Write Locks**: Instead of a single synchronized block or simple lock, introduce a `ReadWriteLock` so that multiple threads can read parking data concurrently while writes remain exclusive.
    - **Lock Striping**: Assign locks to sections or groups of spots, allowing higher concurrency if different threads operate on different groups simultaneously.

2. **Sharding / Multi-Zone Parking**
    - **Section-Based Parking**: Split the parking into multiple zones (e.g., floors, areas), each managed by its own `ParkingService`. This reduces contention on a single lock and scales better if you have a large number of threads.
    - **Load Balancing**: Add a routing mechanism that decides which parking zone a vehicle should go to based on availability or proximity.

3. **Database / Transactional Support**
    - **Persistence**: Integrate a relational or NoSQL database so that parking data (spots, occupancy, vehicles) is not just in-memory but stored persistently.
    - **Transactional Safety**: Use transactions (e.g., ACID in a relational DB) to ensure that operations like a van occupying 3 spots succeed or fail as one atomic unit, even across application restarts or node failures.
    - **Distributed Databases**: For very large-scale deployments, consider a distributed database that can handle partitioning and replication of parking data.

4. **Event-Driven Architecture**
    - **Message Queues**: Use a system like RabbitMQ or Kafka to process parking requests asynchronously, improving throughput.
    - **Microservices**: Break down the system into microservices (parking service, billing service, notification service, etc.). This increases maintainability and horizontal scalability.

5. **Real-Time Monitoring / Telemetry**
    - **Dashboard**: Implement a real-time dashboard showing occupied/free spots, with filtering by vehicle type.
    - **Metrics and Alerts**: Expose metrics (e.g., via Prometheus) for average parking time, concurrency stats, spot utilization, etc., and set up alerts (e.g., if parking is about to be completely full).

6. **API Layer**
    - **REST or GraphQL**: Wrap your `ParkingService` in a RESTful or GraphQL API so that external clients or mobile apps can integrate.
    - **Authentication/Authorization**: Add security layers if you want different roles (e.g., admin vs. user) to access or modify parking info.

7. **Extending Vehicle Types**
    - **Bus / Truck**: Introduce more complex rules (e.g., bus might occupy two big spots, or truck might need some specialized area).
    - **Electric Vehicles**: Require charging spots, potentially tracking charger availability and usage time.

8. **Scheduling / Reservations**
    - **Pre-Booking**: Allow vehicles to reserve spots in advance, requiring a more sophisticated state machine (spot reserved vs. occupied vs. free).
    - **Pricing / Billing**: Integrate with a pricing engine to bill customers based on time or spot type.

9. **Failover and High Availability**
    - **Multiple Instances** of the application running behind a load balancer, so if one instance goes down, others handle requests.
    - **In-Memory Data Grids**: Use solutions like Hazelcast or Redis for distributed, resilient state if you don’t want a full DB.

10. **IoT / Sensor Integration**
- **Real Sensors**: Spot sensors to detect if a spot is physically occupied, integrating hardware input to keep the software state accurate.
- **Gate Systems**: Automate entry/exit with license plate recognition, feeding into the `ParkingService` in real time.

## 9. **Project Prerequisites**

To **run** and **develop** this Parking System application, you will need:

1. **Java Development Kit (JDK)**
    - Make sure to install at least **Java 8** (though Java 11+ is recommended).
    - Verify your installation by running `java -version` in your terminal.

2. **Build Tool (optional but recommended)**
    - If you want to manage dependencies, testing, and packaging in a more automated way, use **Maven** .
    - This project can also be compiled with plain `javac` if you only have raw `.java` files.

3. **IDE or Text Editor**
    - You can use any Java-supporting IDE such as **IntelliJ IDEA**, **Eclipse**, or **NetBeans**.
    - If you prefer a lightweight setup, **VS Code** (with Java extensions) or a simple text editor can also work, but you’ll handle build/test commands manually.

4. **JUnit Test Runner**
    - The project’s tests rely on **JUnit** (Jupiter).
    - If you’re using an IDE, it likely has **JUnit** integration out of the box.
    - If you’re using Maven or Gradle, they automatically handle test dependencies and running tests (e.g., `mvn test`).

5. **Git** (optional)
    - If you plan to version-control and collaborate on this project, installing **Git** is recommended.

6. **Command-Line or Terminal**
    - A basic knowledge of the command line (or Terminal on macOS/Linux) to compile and run Java applications if you’re not using a full IDE.

### Steps to Launch

1. **Clone or Download** the project source code.
2. **Open** the project in your IDE (or navigate to the project folder if using the command line).
3. **Compile** the code:
    - If using an IDE, simply build the project.
    - Using Maven, run `mvn compile`.
4. **Run Tests** (optional but recommended):
    - `mvn test` (Maven), or let your IDE run JUnit tests.
5. **Execute the main class** `org.example.parking.Main`:
    - In most IDEs, right-click on `Main.java` and select “Run”.
    - If using the command line, compile and run with:
      ```bash
      javac org/example/parking/Main.java
      java org.example.parking.Main
      ```

### Summary of Requirements

- **JDK** (Java 8 or later)
- **IDE** (IntelliJ, Eclipse, NetBeans, or a text editor + CLI)
- **JUnit** integration for testing
- (Optional) **Maven** to streamline building and testing

With these prerequisites in place, you can **compile, test, and run** the Parking System easily.


## 10. **Conclusion**

This parking system:

- Implements **object-oriented** models for vehicles and spots.
- Separates domain logic in `Parking`.
- Centralizes **parking**/**unparking** logic in `ParkingService`, with concurrency control to prevent inconsistent states.
- Is tested with JUnit to ensure correctness under multiple conditions.

It is **scalable** for moderate concurrency and easily **maintainable** thanks to a clean architecture and comprehensive tests. Further steps (sharding, DB transactions, advanced locks) can be introduced for extremely large-scale or highly concurrent environments.

**Enjoy using and extending this Parking Management System!**


