---

```markdown
# Product Requirements Document (PRD)
**Product Name:** Mini E-Commerce Backend Platform  
**Version:** 1.0.0 (Updated with Technical Specs)  
**Status:** Draft

---

## 1. Product Overview
The **Mini E-Commerce Backend Platform** is a foundational service designed to enable businesses to manage products and categories efficiently while allowing users to discover products seamlessly.

The platform acts as the core backend system for a simple e-commerce experience, ensuring:
* Products are well-organized.
* Data is consistent and reliable.
* Users can search and browse products easily.
* The system is observable and maintainable in production.

## 2. Problem Statement
Small and growing e-commerce systems often face the following challenges:
* **Data Inconsistency:** Product data becomes messy and inconsistent over time.
* **Poor Discovery:** Searching for products is slow, limited, or inaccurate.
* **Fragility:** Backend changes often introduce unexpected errors.
* **Lack of Visibility:** There is no easy way to know when or why something goes wrong in production.

## 3. Business Goals
* Enable efficient management of product catalogs.
* Improve product discoverability for end users.
* Reduce operational risk caused by data inconsistency.
* Increase confidence in backend reliability.
* Provide visibility into system health via monitoring endpoints.

## 4. Target Users & Personas
* **Store Admin:** Manages categories and products. Needs accuracy.
* **End User:** Browses and searches products. Needs speed.
* **Engineering Team:** Maintains system. Needs logs and clean architecture.

## 5. User Stories
| ID | Module | Actor | Story |
| :--- | :--- | :--- | :--- |
| **US-01** | Category | Store Admin | I want to create, view, and delete categories. |
| **US-04** | Product | Store Admin | I want to add, update, and remove products. |
| **US-07** | Discovery | End User | I want to search products by name and filter by price. |
| **US-08** | Discovery | End User | I want to browse products with pagination. |

---

## 6. Technical Specifications & Guidelines (NEW)
This section outlines the strict technical constraints and architectural standards required for the implementation.

### 6.1. Architecture & Design Patterns
* **Layered Architecture:** The application must strictly follow the `Controller` $\rightarrow$ `Service` $\rightarrow$ `Repository` pattern.
* **Dependency Injection:**
    * **MUST** use **Constructor Injection**.
    * **FORBIDDEN**: Field injection using `@Autowired`.
* **DTO Pattern:**
    * Strict separation between Entities and DTOs.
    * Use `RequestDTO` for incoming data and `ResponseDTO` for outgoing data.
* **No Lombok:**
    * Do not use the Lombok library.
    * Use standard Java Getters/Setters/Constructors for Entities.
    * Use **Java Records** (Java 17+) for DTOs to ensure immutability and conciseness.

### 6.2. Database & Data Access
* **JPA Repository:** Use Spring Data JPA.
* **Query Strategy:**
    * **MUST** use **Native Query** (`@Query(value = "SELECT ...", nativeQuery = true)`) for custom data retrieval.
    * Avoid *Derived Query Methods* (e.g., `findByNameAndStatus...`) to maintain SQL control and performance visibility.
* **Pagination & Filtering:**
    * Implement standard pagination using `Pageable`.
    * Implement filtering logic according to Best Practices (e.g., using Specification or Dynamic Native Queries).

### 6.3. Observability & Logging
* **Spring Boot Actuator:**
    * Expose endpoints: `/actuator/health`, `/actuator/info`, `/actuator/metrics`.
* **Logging (SLF4J):**
    * **FORBIDDEN**: `System.out.println`.
    * **REQUIREMENT**: Use `Logger` (SLF4J).
    * **Log Levels:**
        * `INFO`: Business flow (e.g., "Product created: ID 123").
        * `DEBUG`: Development details (e.g., "Incoming request payload...").
        * `ERROR`: Failures and Exceptions.

---

## 7. Error Handling Specification
The system must implement **Centralized Exception Handling** to ensure consistent API responses.

### 7.1. Components
* **Location:** Create a package/folder named `exception`.
* **Custom Exceptions:**
    * `ResourceNotFoundException`: When data is not found (404).
    * `BusinessException`: When business logic rules are violated (400).
* **Handler:** Use `@ControllerAdvice` and `@ExceptionHandler`.
* **Constraint:** **Do not** use `try-catch` blocks in the Controller layer. Let exceptions propagate to the Global Handler.

### 7.2. Unified Error Response Format (JSON)
All errors must return the following JSON structure:
```json
{
  "code": "BUSINESS_ERROR",
  "message": "Produk dengan stok lebih dari 0 tidak boleh dihapus",
  "details": [
    "Optional detail 1",
    "Optional detail 2"
  ]
}

```

### 7.3. HTTP Status Mapping

* `400 Bad Request`: Validation errors, BusinessException.
* `404 Not Found`: ResourceNotFoundException.
* `500 Internal Server Error`: Unhandled/Unexpected system errors.

---

## 8. Configuration & Environments

The application must support **Multiple Spring Profiles** to ensure separation of concerns between environments.

### 8.1. Profiles

* `dev`: For local development.
* `test`: For integration testing.
* `prod`: For production deployment.

### 8.2. Configuration Files

* `application.yaml` (Main coordinator)
* `application-dev.yaml`
* `application-test.yaml`
* `application-prod.yaml`

### 8.3. Constraints

* **Database Config:** Each profile must use a different database configuration.
* **Security:** No secrets/credentials allowed in source code (Hardcoded).
* **Environment Variables:** Use `${VAR_NAME}` syntax for sensitive data (DB passwords, API keys) in `application-prod.yaml`.

---

## 9. Out of Scope

* User accounts and authentication.
* Payments and checkout flow.
* Order management.
* Promotions and discounts.

```

```