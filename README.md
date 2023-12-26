# TaskFlow Backend 

## Project Description

TaskFlow is a collaborative task management platform that emerged from a collaborative effort to address the shortcomings of existing task management tools. Driven by a commitment to innovation, the development team chose to build a robust platform using Spring Boot and Angular. TaskFlow aims to simplify task management complexities for individuals, team leaders, and managers in dynamic work environments.

## Setup Instructions

### Clone the Project

```bash
git clone [<repository_url>](https://github.com/mohamedchkir/TaskFlow.git)
cd taskflow
```

### Build and Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The backend will be accessible at `http://localhost:8080`.

## Functionality

### CRUD Operations for Tasks

1. **Task Creation:**
   - Tasks cannot be created in the past.
   - Users must enter multiple tags for tasks.

2. **Task Scheduling:**
   - Task scheduling is restricted to 3 days in advance.

3. **Task Completion:**
   - Marking a task as complete must be done before the deadline.

4. **Task Assignment:**
   - Users can assign tasks to themselves, not to others.

5. **Token-Based Operations:**
   - Two tokens per day for replacing tasks assigned by the manager.
   - One token per month for task deletion.
   - Deleting a task created by the same user does not affect tokens.

## Technologies Used

- **Spring Boot:** Backend framework for building Java-based applications.
- **Spring Data JPA:** Simplifies data access using Java Persistence API.
- **MapStruct:** Simplifies mapping between DTOs and entities.
- **Liquibase:** Manages database schema changes.
- **JUnit and Mockito:** Used for unit testing.
- **SonarQube:** Ensures code quality through static code analysis.
