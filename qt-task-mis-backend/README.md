# USER TASK MANAGEMENT SYSTEM

## Tools

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [PostgreSQL](https://www.postgresql.org/)

## Get started

### Prerequisites

- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

### Installation

1. Clone the repo
   ```sh
   git clone
    ```
2. Install Maven dependencies
    ```sh
    mvn install
    ```
3. Create a database in PostgreSQL
    ```sh
    CREATE DATABASE qt_user_task_management;
    ```
4. Create a user in PostgreSQL
    ```sh
    CREATE USER postgres WITH PASSWORD 'test';
    ```
5. Grant privileges to the user
    ```sh
    GRANT ALL PRIVILEGES ON DATABASE qt_user_task_management TO postgres;
    ```
6. Run the application
    ```sh
    mvn spring-boot:run
    ```
7. Open the application in a browser
    ```sh
    http://localhost:8080
    ```

