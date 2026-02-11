# Product API Specification

## Overview

Product API provides endpoints for managing products in the e-commerce system, including category associations and advanced search capabilities.

**Base URL:** `https://idp-week6.glanze.space/api/v1`

**Content-Type:** `application/json`

---

## Endpoints

### 1. Get All Products

Retrieve a paginated list of all products with optional filtering.

**Endpoint:** `GET /products`

**Query Parameters:**

| Parameter | Type   | Default | Description                          |
|-----------|--------|---------|--------------------------------------|
| page      | int    | 0       | Page number (0-indexed)              |
| size      | int    | 10      | Number of items per page             |
| sortBy    | String | name    | Field to sort by (name, price, id, createdAt) |
| sortDir   | String | asc     | Sort direction: asc or desc          |
| categoryId| Long   | null    | Filter by category ID                |

**Request Example:**
```bash
GET /api/v1/products?page=0&size=10&sortBy=name&sortDir=asc&categoryId=1
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
        "name": "Laptop Gaming ASUS ROG",
        "description": "High-performance gaming laptop with RTX 4060",
        "price": 25000000,
        "stock": 15,
        "categoryId": 1,
        "categoryName": "Electronics",
        "sku": "ASUS-ROG-4060-2024",
        "createdAt": "2026-02-10T12:00:00Z",
        "updatedAt": "2026-02-10T12:00:00Z"
      },
      {
        "id": 2,
        "name": "Wireless Mouse Logitech",
        "description": "Ergonomic wireless mouse with precision tracking",
        "price": 350000,
        "stock": 50,
        "categoryId": 1,
        "categoryName": "Electronics",
        "sku": "LOGI-MOUSE-WL-001",
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

### 2. Get Product by ID

Retrieve a specific product by its ID.

**Endpoint:** `GET /products/{id}`

**Path Parameters:**

| Parameter | Type | Description         |
|-----------|------|---------------------|
| id        | Long | Unique product ID   |

**Request Example:**
```bash
GET /api/v1/products/1
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Operation completed successfully",
  "data": {
    "id": 1,
    "name": "Laptop Gaming ASUS ROG",
    "description": "High-performance gaming laptop with RTX 4060",
    "price": 25000000,
    "stock": 15,
    "categoryId": 1,
    "categoryName": "Electronics",
    "sku": "ASUS-ROG-4060-2024",
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
  "message": "Product with ID 999 not found",
  "data": null,
  "timestamp": "2026-02-10T12:00:00Z"
}
```

---

### 3. Create Product

Create a new product.

**Endpoint:** `POST /products`

**Request Body:**

| Field      | Type   | Required | Description                               |
|------------|--------|----------|-------------------------------------------|
| name       | String | Yes      | Product name (3-200 characters)          |
| description| String | No       | Product description                       |
| price      | BigDecimal | Yes  | Product price (must be positive)          |
| stock      | Integer | No       | Stock quantity (default: 0)               |
| categoryId | Long   | Yes      | Category ID (must exist)                  |
| sku        | String | No       | Stock Keeping Unit (unique, 3-50 chars)   |

**Request Example:**
```bash
POST /api/v1/products
Content-Type: application/json

{
  "name": "Mechanical Keyboard RGB",
  "description": "Premium mechanical keyboard with RGB backlighting",
  "price": 850000,
  "stock": 25,
  "categoryId": 1,
  "sku": "KEYB-MECH-RGB-001"
}
```

**Response (201 Created):**
```json
{
  "code": 201,
  "status": "CREATED",
  "message": "Product created successfully",
  "data": {
    "id": 3,
    "name": "Mechanical Keyboard RGB",
    "description": "Premium mechanical keyboard with RGB backlighting",
    "price": 850000,
    "stock": 25,
    "categoryId": 1,
    "categoryName": "Electronics",
    "sku": "KEYB-MECH-RGB-001",
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
    "price": "Price must be positive",
    "categoryId": "Category not found"
  },
  "timestamp": "2026-02-10T12:10:00Z"
}
```

---

### 4. Update Product

Update an existing product.

**Endpoint:** `PUT /products/{id}`

**Path Parameters:**

| Parameter | Type | Description         |
|-----------|------|---------------------|
| id        | Long | Unique product ID   |

**Request Body:**

| Field      | Type   | Required | Description                               |
|------------|--------|----------|-------------------------------------------|
| name       | String | Yes      | Product name (3-200 characters)          |
| description| String | No       | Product description                       |
| price      | BigDecimal | Yes  | Product price (must be positive)          |
| stock      | Integer | No       | Stock quantity                            |
| categoryId | Long   | Yes      | Category ID (must exist)                  |
| sku        | String | No       | Stock Keeping Unit (unique, 3-50 chars)   |

**Request Example:**
```bash
PUT /api/v1/products/1
Content-Type: application/json

{
  "name": "Laptop Gaming ASUS ROG Updated",
  "description": "High-performance gaming laptop with RTX 4060 and 32GB RAM",
  "price": 26500000,
  "stock": 12,
  "categoryId": 1,
  "sku": "ASUS-ROG-4060-32GB"
}
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Product updated successfully",
  "data": {
    "id": 1,
    "name": "Laptop Gaming ASUS ROG Updated",
    "description": "High-performance gaming laptop with RTX 4060 and 32GB RAM",
    "price": 26500000,
    "stock": 12,
    "categoryId": 1,
    "categoryName": "Electronics",
    "sku": "ASUS-ROG-4060-32GB",
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
  "message": "Product with ID 999 not found",
  "data": null,
  "timestamp": "2026-02-10T12:15:00Z"
}
```

---

### 5. Delete Product

Delete a product by ID (soft delete).

**Endpoint:** `DELETE /products/{id}`

**Path Parameters:**

| Parameter | Type | Description         |
|-----------|------|---------------------|
| id        | Long | Unique product ID   |

**Request Example:**
```bash
DELETE /api/v1/products/1
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Product deleted successfully",
  "data": {
    "id": 1,
    "name": "Laptop Gaming ASUS ROG",
    "description": "High-performance gaming laptop with RTX 4060",
    "price": 25000000,
    "stock": 15,
    "categoryId": 1,
    "categoryName": "Electronics",
    "sku": "ASUS-ROG-4060-2024",
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
  "message": "Product with ID 999 not found",
  "data": null,
  "timestamp": "2026-02-10T12:20:00Z"
}
```

---

### 6. Search Products

Search products by name with optional category filter.

**Endpoint:** `GET /products/search`

**Query Parameters:**

| Parameter | Type   | Default | Description                      |
|-----------|--------|---------|----------------------------------|
| name      | String | -       | Product name to search for       |
| categoryId| Long   | null    | Filter by category ID            |
| page      | int    | 0       | Page number (0-indexed)          |
| size      | int    | 10      | Number of items per page         |

**Request Example:**
```bash
GET /api/v1/products/search?name=laptop&categoryId=1&page=0&size=10
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
        "name": "Laptop Gaming ASUS ROG",
        "description": "High-performance gaming laptop with RTX 4060",
        "price": 25000000,
        "stock": 15,
        "categoryId": 1,
        "categoryName": "Electronics",
        "sku": "ASUS-ROG-4060-2024",
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

### 7. Get Products by Category

Retrieve all products belonging to a specific category.

**Endpoint:** `GET /products/category/{categoryId}`

**Path Parameters:**

| Parameter  | Type | Description           |
|------------|------|-----------------------|
| categoryId | Long | Category ID to filter |

**Query Parameters:**

| Parameter | Type   | Default | Description                          |
|-----------|--------|---------|--------------------------------------|
| page      | int    | 0       | Page number (0-indexed)              |
| size      | int    | 10      | Number of items per page             |
| sortBy    | String | name    | Field to sort by                     |
| sortDir   | String | asc     | Sort direction: asc or desc          |

**Request Example:**
```bash
GET /api/v1/products/category/1?page=0&size=10&sortBy=name&sortDir=asc
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
        "name": "Laptop Gaming ASUS ROG",
        "description": "High-performance gaming laptop with RTX 4060",
        "price": 25000000,
        "stock": 15,
        "categoryId": 1,
        "categoryName": "Electronics",
        "sku": "ASUS-ROG-4060-2024",
        "createdAt": "2026-02-10T12:00:00Z",
        "updatedAt": "2026-02-10T12:00:00Z"
      },
      {
        "id": 2,
        "name": "Wireless Mouse Logitech",
        "description": "Ergonomic wireless mouse with precision tracking",
        "price": 350000,
        "stock": 50,
        "categoryId": 1,
        "categoryName": "Electronics",
        "sku": "LOGI-MOUSE-WL-001",
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

### 8. Get Products by Price Range

Retrieve products within a specific price range.

**Endpoint:** `GET /products/price-range`

**Query Parameters:**

| Parameter | Type   | Default | Description                      |
|-----------|--------|---------|----------------------------------|
| minPrice  | BigDecimal | - | Minimum price (inclusive)      |
| maxPrice  | BigDecimal | - | Maximum price (inclusive)      |
| page      | int    | 0       | Page number (0-indexed)          |
| size      | int    | 10      | Number of items per page         |

**Request Example:**
```bash
GET /api/v1/products/price-range?minPrice=100000&maxPrice=1000000&page=0&size=10
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
        "id": 2,
        "name": "Wireless Mouse Logitech",
        "description": "Ergonomic wireless mouse with precision tracking",
        "price": 350000,
        "stock": 50,
        "categoryId": 1,
        "categoryName": "Electronics",
        "sku": "LOGI-MOUSE-WL-001",
        "createdAt": "2026-02-10T12:05:00Z",
        "updatedAt": "2026-02-10T12:05:00Z"
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

### Product Object

| Field      | Type            | Description                              |
|------------|-----------------|------------------------------------------|
| id         | Long            | Unique product identifier                |
| name       | String          | Product name (3-200 characters)         |
| description| String (nullable)| Product description                      |
| price      | BigDecimal      | Product price (must be positive)         |
| stock      | Integer         | Available stock quantity                 |
| categoryId | Long            | Category ID (foreign key)                |
| categoryName | String        | Category name (read-only)                |
| sku        | String (nullable)| Stock Keeping Unit (unique)            |
| createdAt  | String (ISO 8601)| Creation timestamp in UTC                |
| updatedAt  | String (ISO 8601)| Last update timestamp in UTC             |

### Validation Rules

| Field   | Rule                                      |
|---------|-------------------------------------------|
| name    | Required, 3-200 characters                |
| price   | Required, must be positive                |
| stock   | Optional, must be non-negative            |
| categoryId | Required, must reference existing category |
| sku     | Optional, 3-50 characters, must be unique |

---

## Error Codes

| Code | Status           | Description                              |
|------|------------------|------------------------------------------|
| 200  | OK               | Request successful                        |
| 201  | CREATED          | Resource created successfully             |
| 400  | BAD_REQUEST      | Invalid request data or validation failed |
| 404  | NOT_FOUND        | Resource not found                        |
| 500  | INTERNAL_ERROR   | Unexpected server error                   |

---

## Notes

- **Soft Delete:** Deleted products are marked as deleted but not removed from the database
- **Category Association:** Products must belong to a valid category
- **Price Format:** Prices are in BigDecimal for precision (e.g., 25000000 = Rp 25,000,000)
- **SKU uniqueness:** SKU codes must be unique across all products
- **Pagination:** All list endpoints support pagination
- **Sorting:** Use `sortBy` and `sortDir` parameters for custom sorting
- **Search:** Search is case-insensitive and supports partial matching
- **Timestamps:** All timestamps are in UTC (ISO 8601 format with 'Z' suffix)

---

## Try It Out

**Swagger UI:** `https://idp-week6.glanze.space/swagger-ui/index.html`

**Example cURL Commands:**

```bash
# Get all products
curl -X GET "https://idp-week6.glanze.space/api/v1/products?page=0&size=10"

# Get products by category
curl -X GET "https://idp-week6.glanze.space/api/v1/products/category/1?page=0&size=10"

# Get product by ID
curl -X GET "https://idp-week6.glanze.space/api/v1/products/1"

# Search products
curl -X GET "https://idp-week6.glanze.space/api/v1/products/search?name=laptop&page=0&size=10"

# Get products by price range
curl -X GET "https://idp-week6.glanze.space/api/v1/products/price-range?minPrice=100000&maxPrice=5000000"

# Create new product
curl -X POST "https://idp-week6.glanze.space/api/v1/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Mechanical Keyboard RGB",
    "description":"Premium mechanical keyboard",
    "price":850000,
    "stock":25,
    "categoryId":1,
    "sku":"KEYB-MECH-RGB-001"
  }'

# Update product
curl -X PUT "https://idp-week6.glanze.space/api/v1/products/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Laptop Gaming Updated",
    "description":"Updated description",
    "price":26500000,
    "stock":12,
    "categoryId":1,
    "sku":"ASUS-ROG-4060-32GB"
  }'

# Delete product
curl -X DELETE "https://idp-week6.glanze.space/api/v1/products/1"
```

---

**Last Updated:** 2026-02-11
**API Version:** 1.0.0
