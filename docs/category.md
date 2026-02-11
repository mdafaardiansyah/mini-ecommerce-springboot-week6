# Category API Specification

## Overview

Category API provides endpoints for managing product categories in the e-commerce system.

**Base URL:** `https://idp-week6.glanze.space/api/v1`

**Content-Type:** `application/json`

---

## Endpoints

### 1. Get All Categories

Retrieve a paginated list of all categories.

**Endpoint:** `GET /categories`

**Query Parameters:**

| Parameter | Type   | Default | Description                          |
|-----------|--------|---------|--------------------------------------|
| page      | int    | 0       | Page number (0-indexed)              |
| size      | int    | 10      | Number of items per page             |
| sortBy    | String | name    | Field to sort by (name, id, createdAt) |
| sortDir   | String | asc     | Sort direction: asc or desc          |

**Request Example:**
```bash
GET /api/v1/categories?page=0&size=10&sortBy=name&sortDir=asc
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Operation completed successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Electronics",
        "description": "Electronic devices, gadgets, and accessories",
        "createdAt": "2026-02-10T12:00:00Z",
        "updatedAt": "2026-02-10T12:00:00Z"
      },
      {
        "id": 2,
        "name": "Clothing",
        "description": "Men's and women's apparel and fashion items",
        "createdAt": "2026-02-10T12:05:00Z",
        "updatedAt": "2026-02-10T12:05:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "totalElements": 2,
      "totalPages": 1,
      "first": true,
      "last": true
    }
  },
  "timestamp": "2026-02-10T12:00:00Z"
}
```

---

### 2. Get Category by ID

Retrieve a specific category by its ID.

**Endpoint:** `GET /categories/{id}`

**Path Parameters:**

| Parameter | Type | Description          |
|-----------|------|----------------------|
| id        | Long | Unique category ID   |

**Request Example:**
```bash
GET /api/v1/categories/1
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Operation completed successfully",
  "data": {
    "id": 1,
    "name": "Electronics",
    "description": "Electronic devices, gadgets, and accessories",
    "createdAt": "2026-02-10T12:00:00Z",
    "updatedAt": "2026-02-10T12:00:00Z"
  },
  "timestamp": "2026-02-10T12:00:00Z"
}
```

**Error Response (404 Not Found):**
```json
{
  "code": 404,
  "status": "NOT_FOUND",
  "message": "Category with ID 999 not found",
  "data": null,
  "timestamp": "2026-02-10T12:00:00Z"
}
```

---

### 3. Create Category

Create a new category.

**Endpoint:** `POST /categories`

**Request Body:**

| Field      | Type   | Required | Description                |
|------------|--------|----------|----------------------------|
| name       | String | Yes      | Category name (3-100 chars) |
| description| String | No       | Category description        |

**Request Example:**
```bash
POST /api/v1/categories
Content-Type: application/json

{
  "name": "Books",
  "description": "Books, ebooks, and educational materials"
}
```

**Response (201 Created):**
```json
{
  "code": 201,
  "status": "CREATED",
  "message": "Category created successfully",
  "data": {
    "id": 3,
    "name": "Books",
    "description": "Books, ebooks, and educational materials",
    "createdAt": "2026-02-10T12:10:00Z",
    "updatedAt": "2026-02-10T12:10:00Z"
  },
  "timestamp": "2026-02-10T12:10:00Z"
}
```

**Error Response (400 Bad Request):**
```json
{
  "code": 400,
  "status": "BAD_REQUEST",
  "message": "Validation failed",
  "data": {
    "name": "Name must be between 3 and 100 characters"
  },
  "timestamp": "2026-02-10T12:10:00Z"
}
```

---

### 4. Update Category

Update an existing category.

**Endpoint:** `PUT /categories/{id}`

**Path Parameters:**

| Parameter | Type | Description          |
|-----------|------|----------------------|
| id        | Long | Unique category ID   |

**Request Body:**

| Field      | Type   | Required | Description                |
|------------|--------|----------|----------------------------|
| name       | String | Yes      | Category name (3-100 chars) |
| description| String | No       | Category description        |

**Request Example:**
```bash
PUT /api/v1/categories/1
Content-Type: application/json

{
  "name": "Electronics & Gadgets",
  "description": "Electronic devices, gadgets, accessories, and smart home products"
}
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Category updated successfully",
  "data": {
    "id": 1,
    "name": "Electronics & Gadgets",
    "description": "Electronic devices, gadgets, accessories, and smart home products",
    "createdAt": "2026-02-10T12:00:00Z",
    "updatedAt": "2026-02-10T12:15:00Z"
  },
  "timestamp": "2026-02-10T12:15:00Z"
}
```

**Error Response (404 Not Found):**
```json
{
  "code": 404,
  "status": "NOT_FOUND",
  "message": "Category with ID 999 not found",
  "data": null,
  "timestamp": "2026-02-10T12:15:00Z"
}
```

---

### 5. Delete Category

Delete a category by ID (soft delete).

**Endpoint:** `DELETE /categories/{id}`

**Path Parameters:**

| Parameter | Type | Description          |
|-----------|------|----------------------|
| id        | Long | Unique category ID   |

**Request Example:**
```bash
DELETE /api/v1/categories/1
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Category deleted successfully",
  "data": {
    "id": 1,
    "name": "Electronics",
    "description": "Electronic devices, gadgets, and accessories",
    "createdAt": "2026-02-10T12:00:00Z",
    "updatedAt": "2026-02-10T12:20:00Z"
  },
  "timestamp": "2026-02-10T12:20:00Z"
}
```

**Error Response (404 Not Found):**
```json
{
  "code": 404,
  "status": "NOT_FOUND",
  "message": "Category with ID 999 not found",
  "data": null,
  "timestamp": "2026-02-10T12:20:00Z"
}
```

---

### 6. Search Categories

Search categories by name.

**Endpoint:** `GET /categories/search`

**Query Parameters:**

| Parameter | Type   | Default | Description                      |
|-----------|--------|---------|----------------------------------|
| name      | String | -       | Category name to search for      |
| page      | int    | 0       | Page number (0-indexed)          |
| size      | int    | 10      | Number of items per page         |

**Request Example:**
```bash
GET /api/v1/categories/search?name=electronic&page=0&size=10
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Operation completed successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Electronics",
        "description": "Electronic devices, gadgets, and accessories",
        "createdAt": "2026-02-10T12:00:00Z",
        "updatedAt": "2026-02-10T12:00:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "totalElements": 1,
      "totalPages": 1,
      "first": true,
      "last": true
    }
  },
  "timestamp": "2026-02-10T12:00:00Z"
}
```

---

## Data Model

### Category Object

| Field      | Type            | Description                              |
|------------|-----------------|------------------------------------------|
| id         | Long            | Unique category identifier                |
| name       | String          | Category name (3-100 characters)         |
| description| String (nullable)| Category description                      |
| createdAt  | String (ISO 8601)| Creation timestamp in UTC                |
| updatedAt  | String (ISO 8601)| Last update timestamp in UTC             |

---

## Error Codes

| Code | Status           | Description                              |
|------|------------------|------------------------------------------|
| 200  | OK               | Request successful                        |
| 201  | CREATED          | Resource created successfully             |
| 400  | BAD_REQUEST      | Invalid request data                      |
| 404  | NOT_FOUND        | Resource not found                        |
| 500  | INTERNAL_ERROR   | Unexpected server error                   |

---

## Notes

- **Soft Delete:** Deleted categories are marked as deleted but not removed from the database
- **Pagination:** All list endpoints support pagination
- **Sorting:** Use `sortBy` and `sortDir` parameters for custom sorting
- **Validation:** Category name must be between 3 and 100 characters
- **Timestamps:** All timestamps are in UTC (ISO 8601 format with 'Z' suffix)

---

## Try It Out

**Swagger UI:** `https://idp-week6.glanze.space/swagger-ui/index.html`

**Example cURL Commands:**

```bash
# Get all categories
curl -X GET "https://idp-week6.glanze.space/api/v1/categories?page=0&size=10"

# Get category by ID
curl -X GET "https://idp-week6.glanze.space/api/v1/categories/1"

# Create new category
curl -X POST "https://idp-week6.glanze.space/api/v1/categories" \
  -H "Content-Type: application/json" \
  -d '{"name":"Books","description":"Books and ebooks"}'

# Update category
curl -X PUT "https://idp-week6.glanze.space/api/v1/categories/1" \
  -H "Content-Type: application/json" \
  -d '{"name":"Electronics & Gadgets","description":"Updated description"}'

# Delete category
curl -X DELETE "https://idp-week6.glanze.space/api/v1/categories/1"
```

---

**Last Updated:** 2026-02-11
**API Version:** 1.0.0
