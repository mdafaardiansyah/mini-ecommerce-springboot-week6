# API Documentation

## Overview

This directory contains comprehensive API documentation for the Mini E-Commerce Spring Boot application.

---

## 📚 Documentation Structure

```
Docs/
├── README.md                    # This file
├── category/
│   └── api-spec.md             # Category API specification
└── product/
    └── api-spec.md             # Product API specification
```

---

## 🌐 Base URL

**Production:** `https://idp-week6.glanze.space/api/v1`

**Development:** `http://localhost:8080/api/v1`

---

## 🔑 Authentication

Currently, the API does not require authentication. This will be implemented in future versions.

---

## 📖 API Specifications

### Category API

**File:** [`category/api-spec.md`](category/api-spec.md)

**Endpoints:**
- `GET /categories` - Get all categories with pagination
- `GET /categories/{id}` - Get category by ID
- `POST /categories` - Create new category
- `PUT /categories/{id}` - Update category
- `DELETE /categories/{id}` - Delete category (soft delete)
- `GET /categories/search` - Search categories by name

**Features:**
- Pagination support
- Sorting by multiple fields
- Soft delete implementation
- Search functionality

---

### Product API

**File:** [`product/api-spec.md`](product/api-spec.md)

**Endpoints:**
- `GET /products` - Get all products with pagination and filtering
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create new product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product (soft delete)
- `GET /products/search` - Search products by name
- `GET /products/category/{categoryId}` - Get products by category
- `GET /products/price-range` - Get products by price range

**Features:**
- Pagination support
- Sorting by multiple fields
- Category filtering
- Price range filtering
- Search functionality
- Soft delete implementation

---

## 🚀 Quick Start

### Using cURL

```bash
# Get all categories
curl -X GET "https://idp-week6.glanze.space/api/v1/categories?page=0&size=10"

# Get all products
curl -X GET "https://idp-week6.glanze.space/api/v1/products?page=0&size=10"

# Get product by ID
curl -X GET "https://idp-week6.glanze.space/api/v1/products/1"
```

### Using Swagger UI

Open your browser and navigate to:
```
https://idp-week6.glanze.space/swagger-ui/index.html
```

Swagger UI provides:
- Interactive API documentation
- Try-it-out functionality for all endpoints
- Request/response examples
- Schema definitions

---

## 📊 Response Format

All API responses follow a consistent structure:

```json
{
  "code": 200,
  "status": "OK",
  "message": "Operation completed successfully",
  "data": {
    // Response payload here
  },
  "timestamp": "2026-02-10T12:00:00Z"
}
```

**Fields:**
- `code`: HTTP status code
- `status`: Status description
- `message`: Human-readable message
- `data`: Response payload (can be null)
- `timestamp`: UTC timestamp in ISO 8601 format

---

## 🔍 Pagination

All list endpoints support pagination:

| Parameter | Type   | Default | Description                  |
|-----------|--------|---------|------------------------------|
| page      | int    | 0       | Page number (0-indexed)      |
| size      | int    | 10      | Number of items per page     |
| sortBy    | String | name/id | Field to sort by            |
| sortDir   | String | asc     | Sort direction (asc/desc)    |

**Response Structure:**
```json
{
  "data": {
    "content": [...],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "totalElements": 100,
      "totalPages": 10,
      "first": true,
      "last": false
    }
  }
}
```

---

## ❌ Error Handling

All errors follow a consistent format:

```json
{
  "code": 404,
  "status": "NOT_FOUND",
  "message": "Resource not found",
  "data": null,
  "timestamp": "2026-02-10T12:00:00Z"
}
```

**Common HTTP Status Codes:**

| Code | Status           | Description                    |
|------|------------------|--------------------------------|
| 200  | OK               | Request successful              |
| 201  | CREATED          | Resource created               |
| 400  | BAD_REQUEST      | Invalid request data           |
| 404  | NOT_FOUND        | Resource not found             |
| 500  | INTERNAL_ERROR   | Server error                   |

---

## 🔒 Security Features

The API implements several security features:

- **CORS:** Cross-Origin Resource Sharing with whitelist
- **Security Headers:** HSTS, CSP, X-Frame-Options, etc.
- **Input Validation:** Request validation on all endpoints
- **SQL Injection Prevention:** Parameterized queries
- **Soft Delete:** Prevents accidental data loss

See [`PRODUCTION_READY_API.md`](../PRODUCTION_READY_API.md) for details.

---

## 🧪 Testing

### Unit Tests

Run unit tests:
```bash
mvn test
```

### Integration Tests

Run integration tests:
```bash
mvn verify
```

### Manual Testing

Use Swagger UI for manual testing:
```
https://idp-week6.glanze.space/swagger-ui/index.html
```

---

## 📝 Data Models

### Category

```typescript
{
  id: number,
  name: string,          // 3-100 characters
  description?: string,  // optional
  createdAt: string,     // ISO 8601
  updatedAt: string      // ISO 8601
}
```

### Product

```typescript
{
  id: number,
  name: string,          // 3-200 characters
  description?: string,  // optional
  price: number,         // positive
  stock: number,         // non-negative
  categoryId: number,
  categoryName: string,  // read-only
  sku?: string,          // optional, unique, 3-50 chars
  createdAt: string,     // ISO 8601
  updatedAt: string      // ISO 8601
}
```

---

## 🌍 Environment Variables

The application uses different configurations per environment:

| Environment | URL                              | Description        |
|-------------|----------------------------------|--------------------|
| Development | http://localhost:8080           | Local development  |
| Production  | https://idp-week6.glanze.space  | Live production    |

---

## 📞 Support

For issues, questions, or contributions:

- **GitHub Issues:** [Project Repository](https://github.com/mdafaardiansyah/mini-ecommerce-springboot-week6/issues)
- **Email:** ardidafa21@gmail.com
- **Author:** Dafa Ardiansyah

---

## 📚 Additional Documentation

- [PRODUCTION_READY_API.md](../PRODUCTION_READY_API.md) - Production-ready improvements guide
- [DEPLOYMENT_GUIDE.md](../DEPLOYMENT_GUIDE.md) - Jenkins + Heroku deployment guide
- [HEROKU_TROUBLESHOOTING.md](../HEROKU_TROUBLESHOOTING.md) - Troubleshooting guide

---

**Last Updated:** 2026-02-11
**API Version:** 1.0.0
