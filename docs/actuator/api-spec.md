# Actuator API Specification

## Overview

Spring Boot Actuator provides production-ready features to help you monitor and manage your application. These endpoints allow you to observe the running application, gather metrics, understand traffic, and understand the health of the application.

**Base URL:** `https://idp-week6.glanze.space`

**Actuator Base Path:** `/actuator`

**Content-Type:** `application/json` or `application/vnd.spring-boot.actuator.v3+json`

---

## Available Endpoints

The following Actuator endpoints are enabled and exposed:

| Endpoint       | Method | Description                                    | Access Level |
|----------------|--------|------------------------------------------------|--------------|
| `/health`      | GET    | Application health status                      | Public       |
| `/info`        | GET    | Application information                        | Public       |
| `/metrics`     | GET    | Metrics information                            | Public       |
| `/env`         | GET    | Environment properties                         | Public       |

---

## Endpoints

### 1. Health Check

Check the health status of the application.

**Endpoint:** `GET /actuator/health`

**Description:**
Returns the health status of the application including database connectivity, disk space, and other health indicators.

**Request Example:**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/health"
```

**Response (200 OK - Application Healthy):**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 250000000000,
        "threshold": 10485760,
        "path": "/app/.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  },
  "groups": ["liveness", "readiness"]
}
```

**Response (503 Service Unavailable - Application Unhealthy):**
```json
{
  "status": "DOWN",
  "components": {
    "db": {
      "status": "DOWN",
      "details": {
        "error": "java.sql.SQLException: Connection refused"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 250000000000,
        "threshold": 10485760,
        "exists": true
      }
    }
  }
}
```

**Health Status Values:**

| Status | Description                       |
|--------|-----------------------------------|
| UP     | Application is healthy            |
| DOWN   | Application is unhealthy          |
| OUT_OF_SERVICE | Component is out of service |
| UNKNOWN | Health status cannot be determined |

---

### 2. Application Information

Retrieve general application information.

**Endpoint:** `GET /actuator/info`

**Description:**
Returns application metadata including build info, Git information, Java version, and OS details.

**Request Example:**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/info"
```

**Response (200 OK):**
```json
{
  "app": {
    "name": "service-week6-miniproject",
    "description": "Mini E-Commerce Spring Boot Application"
  },
  "build": {
    "artifact": "Week6_Practice1",
    "group": "edts",
    "name": "Week6_Practice1",
    "version": "0.0.1-SNAPSHOT",
    "time": "2026-02-10T00:00:00.000Z"
  },
  "git": {
    "branch": "master",
    "commit": {
      "id": "abc123def",
      "time": "2026-02-10T12:00:00Z"
    }
  },
  "java": {
    "version": "17.0.9",
    "vendor": "Eclipse Adoptium",
    "runtime": {
      "name": "OpenJDK Runtime Environment",
      "version": "17.0.9+9"
    },
    "jvm": {
      "name": "OpenJDK 64-Bit Server VM",
      "vendor": "Eclipse Adoptium",
      "version": "17.0.9+9"
    }
  },
  "os": {
    "name": "Linux",
    "version": "5.15.0-1070-aws",
    "arch": "x86_64"
  }
}
```

**Info Fields:**

| Field    | Type   | Description                        |
|----------|--------|------------------------------------|
| app      | Object | Application name and description   |
| build    | Object | Build information (artifact, version) |
| git      | Object | Git branch and commit information  |
| java     | Object | Java version and runtime details   |
| os       | Object | Operating system information       |

---

### 3. Metrics

Retrieve metrics information about the application.

**Endpoint:** `GET /actuator/metrics`

**Description:**
Returns a list of available metric names. Use this endpoint to discover which metrics are available, then query specific metrics using `/actuator/metrics/{metric.name}`.

**Request Example:**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/metrics"
```

**Response (200 OK):**
```json
{
  "names": [
    "jvm.memory.max",
    "jvm.memory.used",
    "jvm.gc.pause",
    "jvm.thread.count",
    "system.cpu.usage",
    "process.cpu.usage",
    "http.server.requests",
    "tomcat.sessions.active.current",
    "logback.events",
    "database.connections",
    "hikaricp.connections.active",
    "hikaricp.connections.idle",
    "hikaricp.connections.max",
    "hikaricp.connections.min"
  ]
}
```

---

### 3.1. Get Specific Metric

Retrieve detailed information about a specific metric.

**Endpoint:** `GET /actuator/metrics/{metric.name}`

**Path Parameters:**

| Parameter    | Type   | Description              |
|--------------|--------|--------------------------|
| metric.name  | String | Name of the metric       |

**Available Metrics:**

| Metric Name                      | Description                              |
|----------------------------------|------------------------------------------|
| `jvm.memory.max`                 | Maximum JVM memory                      |
| `jvm.memory.used`                | Used JVM memory                         |
| `jvm.gc.pause`                   | GC pause time                           |
| `jvm.thread.count`               | Current thread count                    |
| `system.cpu.usage`               | System CPU usage                        |
| `process.cpu.usage`              | Process CPU usage                       |
| `http.server.requests`           | HTTP request metrics                    |
| `tomcat.sessions.active.current` | Active Tomcat sessions                 |
| `hikaricp.connections.active`    | Active database connections             |
| `hikaricp.connections.idle`      | Idle database connections               |
| `hikaricp.connections.max`       | Max database connections                |
| `hikaricp.connections.min`       | Min database connections                |

**Request Example (JVM Memory):**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/metrics/jvm.memory.used"
```

**Response (200 OK):**
```json
{
  "name": "jvm.memory.used",
  "description": "The amount of used memory",
  "baseUnit": "bytes",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 250000000
    }
  ],
  "availableTags": [
    {
      "tag": "area",
      "values": ["heap", "nonheap"]
    },
    {
      "tag": "id",
      "values": ["G1 Eden Space", "G1 Old Gen", "G1 Survivor Space"]
    }
  ]
}
```

**Request Example (HTTP Server Requests):**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/metrics/http.server.requests?tag=uri:/api/v1/products"
```

**Response (200 OK):**
```json
{
  "name": "http.server.requests",
  "description": "HTTP server request metrics",
  "baseUnit": "seconds",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 1500
    },
    {
      "statistic": "TOTAL_TIME",
      "value": 45.234
    },
    {
      "statistic": "MAX",
      "value": 0.5
    }
  ],
  "availableTags": [
    {
      "tag": "exception",
      "values": ["None", "IllegalArgumentException"]
    },
    {
      "tag": "method",
      "values": ["GET", "POST", "PUT", "DELETE"]
    },
    {
      "tag": "outcome",
      "values": ["SUCCESS", "CLIENT_ERROR", "SERVER_ERROR"]
    },
    {
      "tag": "status",
      "values": ["200", "201", "400", "404", "500"]
    },
    {
      "tag": "uri",
      "values": ["/api/v1/products", "/api/v1/categories", "/actuator/health"]
    }
  ]
}
```

**Request Example (Database Connections):**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/metrics/hikaricp.connections.active"
```

**Response (200 OK):**
```json
{
  "name": "hikaricp.connections.active",
  "description": "The number of active connections",
  "baseUnit": "connections",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 5
    }
  ],
  "availableTags": [
    {
      "tag": "pool",
      "values": ["HikariPool-1"]
    }
  ]
}
```

---

### 4. Environment Properties

Retrieve environment properties and configuration.

**Endpoint:** `GET /actuator/env`

**Description:**
Returns the complete environment configuration including system properties, environment variables, and application properties.

**Request Example:**
```bash
curl -X GET "https://idp-week6.glanze.space/actuator/env"
```

**Response (200 OK - Truncated):**
```json
{
  "activeProfiles": ["prod"],
  "propertySources": [
    {
      "name": "server.ports",
      "properties": {
        "local.server.port": {
          "value": "8080"
        }
      }
    },
    {
      "name": "spring.data.mongodb.config",
      "properties": {}
    },
    {
      "name": "systemEnvironment",
      "properties": {
        "PATH": {
          "value": "/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
          "origin": "System Environment"
        },
        "JAVA_HOME": {
          "value": "/usr/lib/jvm/java-17-openjdk",
          "origin": "System Environment"
        },
        "PORT": {
          "value": "8080",
          "origin": "System Environment"
        }
      }
    },
    {
      "name": "applicationConfig: [file:./application.yaml]",
      "properties": {
        "spring.application.name": {
          "value": "service-week6-miniproject",
          "origin": "application.yaml"
        },
        "spring.jackson.time-zone": {
          "value": "UTC",
          "origin": "application.yaml"
        },
        "server.port": {
          "value": "8080",
          "origin": "application.yaml"
        },
        "spring.datasource.url": {
          "value": "jdbc:mysql://localhost:3306/ecommerce_db",
          "origin": "application.yaml"
        }
      }
    },
    {
      "name": "Config resource 'class path resource [application-prod.yaml]' via location 'optional:classpath:/'",
      "properties": {
        "spring.profiles.active": {
          "value": "prod",
          "origin": "application-prod.yaml"
        }
      }
    }
  ]
}
```

---

### 4.1. Get Specific Property

Retrieve a specific environment property.

**Endpoint:** `GET /actuator/env/{property.name}`

**Path Parameters:**

| Parameter      | Type   | Description              |
|----------------|--------|--------------------------|
| property.name  | String | Property name (use dots as separators) |

**Request Example:**
```bash
# Get server port
curl -X GET "https://idp-week6.glanze.space/actuator/env/server.port"

# Get database URL
curl -X GET "https://idp-week6.glanze.space/actuator/env/spring.datasource.url"

# Get application name
curl -X GET "https://idp-week6.glanze.space/actuator/env/spring.application.name"
```

**Response (200 OK):**
```json
{
  "property": {
    "name": "server.port",
    "value": "8080",
    "origin": "application.yaml"
  }
}
```

---

## Use Cases

### 1. Health Monitoring

**Use in monitoring systems:**
```bash
# Simple health check
curl -f http://localhost:8080/actuator/health || exit 1

# JSON health check with status
HEALTH=$(curl -s http://localhost:8080/actuator/health | jq -r '.status')
if [ "$HEALTH" != "UP" ]; then
  echo "Application is unhealthy: $HEALTH"
  exit 1
fi
```

**Kubernetes Liveness/Readiness Probes:**
```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 10

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
```

---

### 2. Performance Monitoring

**Monitor JVM memory usage:**
```bash
# Get used memory
MEMORY_USED=$(curl -s http://localhost:8080/actuator/metrics/jvm.memory.used \
  | jq -r '.measurements[0].value')

echo "Used Memory: $MEMORY_USED bytes"
```

**Monitor database connections:**
```bash
# Get active connections
ACTIVE_CONN=$(curl -s http://localhost:8080/actuator/metrics/hikaricp.connections.active \
  | jq -r '.measurements[0].value')

# Get max connections
MAX_CONN=$(curl -s http://localhost:8080/actuator/metrics/hikaricp.connections.max \
  | jq -r '.measurements[0].value')

echo "Database Connections: $ACTIVE_CONN/$MAX_CONN"
```

**Monitor HTTP request metrics:**
```bash
# Get total request count
REQUEST_COUNT=$(curl -s "http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/api/v1/products" \
  | jq -r '.measurements[] | select(.statistic == "COUNT") | .value')

echo "Total /api/v1/products Requests: $REQUEST_COUNT"
```

---

### 3. Application Info

**Get application version:**
```bash
VERSION=$(curl -s http://localhost:8080/actuator/info | jq -r '.build.version')
echo "Application Version: $VERSION"
```

**Get Git commit:**
```bash
GIT_COMMIT=$(curl -s http://localhost:8080/actuator/info | jq -r '.git.commit.id')
echo "Git Commit: $GIT_COMMIT"
```

---

### 4. Environment Debugging

**Check active profiles:**
```bash
curl -s http://localhost:8080/actuator/env | jq -r '.activeProfiles'
```

**Verify database connection string:**
```bash
curl -s "http://localhost:8080/actuator/env/spring.datasource.url" | jq '.property.value'
```

**Check Java version:**
```bash
curl -s http://localhost:8080/actuator/info | jq -r '.java.version'
```

---

## Security Considerations

### ⚠️ Production Security

**WARNING:** In production, you should:

1. **Restrict Actuator Access:**
   ```yaml
   management:
     endpoints:
       web:
         exposure:
           include: health,info
     endpoint:
       health:
         show-details: when-authorized
   ```

2. **Use Spring Security:**
   ```java
   @Configuration
   public class ActuatorSecurityConfig {
       @Bean
       public SecurityFilterChain filterChain(HttpSecurity http) {
           http.requestMatcher(EndpointRequest.toAnyEndpoint())
               .authorizeRequests(auth -> auth
                   .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                   .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
               );
           return http.build();
       }
   }
   ```

3. **Hide Sensitive Information:**
   ```yaml
   management:
     endpoint:
       env:
         keys-to-sanitize: password,secret,api_key,*credentials
   ```

---

## Monitoring Integration

### Prometheus Integration (Optional)

To integrate with Prometheus monitoring system, add the dependency:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Then access metrics at: `/actuator/prometheus`

### Grafana Dashboard

Import metrics into Grafana for visualization:

1. Add Prometheus as data source
2. Import Spring Boot dashboard
3. Monitor JVM, HTTP, and database metrics

---

## cURL Examples

```bash
# Health check
curl -X GET "https://idp-week6.glanze.space/actuator/health"

# Application info
curl -X GET "https://idp-week6.glanze.space/actuator/info"

# List all metrics
curl -X GET "https://idp-week6.glanze.space/actuator/metrics"

# Get specific metric (JVM memory)
curl -X GET "https://idp-week6.glanze.space/actuator/metrics/jvm.memory.used"

# Get HTTP request metrics
curl -X GET "https://idp-week6.glanze.space/actuator/metrics/http.server.requests"

# Get database connection metrics
curl -X GET "https://idp-week6.glanze.space/actuator/metrics/hikaricp.connections.active"

# List environment properties
curl -X GET "https://idp-week6.glanze.space/actuator/env"

# Get specific property
curl -X GET "https://idp-week6.glanze.space/actuator/env/server.port"
```

---

## Response Codes

| Code | Status           | Description                              |
|------|------------------|------------------------------------------|
| 200  | OK               | Request successful                        |
| 404  | NOT_FOUND        | Endpoint or metric not found              |
| 500  | INTERNAL_ERROR   | Server error while gathering metrics      |

---

## Best Practices

1. **Health Checks:**
   - Use `/actuator/health` in load balancers
   - Monitor health status in alerting systems
   - Configure liveness and readiness probes

2. **Metrics:**
   - Monitor key metrics regularly (memory, CPU, connections)
   - Set up alerts for threshold breaches
   - Use metrics for capacity planning

3. **Security:**
   - Restrict access to sensitive endpoints
   - Use HTTPS in production
   - Implement proper authentication

4. **Performance:**
   - Don't poll metrics too frequently (recommend 30-60 seconds)
   - Use specific metric queries instead of full list
   - Cache metrics where appropriate

---

## Troubleshooting

### Health Check Returns DOWN

**Problem:** `/actuator/health` returns `{"status":"DOWN"}`

**Solutions:**
1. Check specific components: `/actuator/health` → Look at `components` field
2. Verify database connectivity: Check `db.status`
3. Check disk space: Verify `diskSpace.status`
4. Review application logs for errors

### Metrics Not Available

**Problem:** `/actuator/metrics` returns empty list

**Solutions:**
1. Verify Micrometer is configured correctly
2. Check if metrics are enabled in `application.yaml`
3. Review dependencies: Ensure `spring-boot-starter-actuator` is included

### Environment Returns 404

**Problem:** `/actuator/env` returns 404

**Solutions:**
1. Verify endpoint is exposed: Check `management.endpoints.web.exposure.include`
2. Check if proper permissions are configured

---

**Last Updated:** 2026-02-11
**Spring Boot Actuator Version:** 4.x
**API Version:** 1.0.0
