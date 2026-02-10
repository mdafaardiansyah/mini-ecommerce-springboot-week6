# 📚 Deployment Guide - Spring Boot Application to Heroku via Jenkins

This guide provides step-by-step instructions for deploying your Spring Boot application to Heroku using Jenkins CI/CD pipeline.

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Required Credentials](#required-credentials)
3. [Heroku Setup](#heroku-setup)
4. [Jenkins Setup](#jenkins-setup)
5. [Application Configuration](#application-configuration)
6. [Deployment Process](#deployment-process)
7. [Monitoring & Troubleshooting](#monitoring--troubleshooting)
8. [Best Practices](#best-practices)

---

## 1. Prerequisites

### Software Requirements

- **Java Development Kit (JDK)**: Version 17
- **Maven**: Version 3.9+
- **Git**: For version control
- **Heroku CLI**: For Heroku operations
- **Jenkins**: Version 2.400+ with pipeline support

### Account Requirements

- **Heroku Account**: [Sign up here](https://signup.heroku.com/)
- **GitHub Account**: For repository hosting
- **Jenkins Server**: Self-hosted or cloud-based

### Installation Commands

```bash
# Install Heroku CLI
# macOS
brew tap heroku/brew && brew install heroku

# Linux
curl https://cli-assets.heroku.com/install.sh | sh

# Verify Heroku CLI
heroku --version

# Login to Heroku (do this ONCE locally to get your API key)
heroku login

# Get your API key (save this securely!)
heroku auth:token
```

---

## 2. Required Credentials

You need to configure the following credentials in Jenkins:

### 🔑 Heroku Credentials

| Credential ID | Type | Description | How to Get |
|--------------|------|-------------|------------|
| `heroku-api-key` | Secret text | Your Heroku API Key | Run `heroku auth:token` locally |
| `heroku-app-name-dev` | Secret text | Development app name | Create app in Heroku (e.g., `week6-practice1-dev`) |
| `heroku-app-name-prod` | Secret text | Production app name | Create app in Heroku (e.g., `week6-practice1-prod`) |

### 🔔 Notification Credentials (Optional)

| Credential ID | Type | Description |
|--------------|------|-------------|
| `discord-notification` | Secret text | Discord webhook URL for build notifications |

### 📝 How to Add Credentials in Jenkins

1. Go to Jenkins Dashboard → **Manage Jenkins**
2. Click on **Credentials** → **System** → **Global credentials**
3. Click **Add Credentials**
4. Select **Secret text** for all credentials
5. Enter the ID and secret value
6. Click **Create**

**Example:**
```
ID: heroku-api-key
Secret: your-heroku-api-key-here
```

---

## 3. Heroku Setup

### Step 1: Create Heroku Applications

```bash
# Create Development App
heroku create week6-practice1-dev

# Create Production App
heroku create week6-practice1-prod

# Verify apps
heroku apps:info --app week6-practice1-dev
heroku apps:info --app week6-practice1-prod
```

### Step 2: Add Heroku PostgreSQL Add-on

```bash
# Add PostgreSQL to development
heroku addons:create heroku-postgresql:mini --app week6-practice1-dev

# Add PostgreSQL to production
heroku addons:create heroku-postgresql:mini --app week6-practice1-prod

# Get database URL
heroku config:get DATABASE_URL --app week6-practice1-dev
```

### Step 3: Configure Environment Variables

```bash
# Development Environment
heroku config:set SPRING_PROFILES_ACTIVE=dev --app week6-practice1-dev
heroku config:set JAVA_OPTS="-Xmx512m -Xms256m" --app week6-practice1-dev

# Production Environment
heroku config:set SPRING_PROFILES_ACTIVE=prod --app week6-practice1-prod
heroku config:set JAVA_OPTS="-Xmx512m -Xms256m" --app week6-practice1-prod
```

### Step 4: Set Buildpacks

```bash
heroku buildpacks:set https://github.com/heroku/heroku-buildpack-java --app week6-practice1-dev
heroku buildpacks:set https://github.com/heroku/heroku-buildpack-java --app week6-practice1-prod
```

---

## 4. Jenkins Setup

### Step 1: Install Required Plugins

Go to **Manage Jenkins** → **Plugins** → **Available Plugins** and install:

- **Pipeline Plugin**
- **Git Plugin**
- **Maven Integration Plugin**
- **Heroku CLI Plugin** (optional, can use CLI directly)
- **Discord Notification Plugin** (optional)
- **JUnit Plugin**

### Step 2: Configure Maven

1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. Scroll to **Maven**
3. Click **Maven installations...**
4. Add Maven:
   - Name: `Maven 3.9`
   - Version: Select `Maven 3.9.x` or install automatically

### Step 3: Configure Heroku CLI on Jenkins Agent

```bash
# SSH into your Jenkins server/agent
ssh jenkins@your-jenkins-server

# Install Heroku CLI
sudo curl https://cli-assets.heroku.com/install.sh | sh

# Verify installation
heroku --version
```

### Step 4: Create Jenkins Pipeline Job

1. Go to Jenkins Dashboard → **New Item**
2. Enter item name: `week6-practice1-deploy`
3. Select **Pipeline**
4. Click **OK**
5. In Pipeline configuration:
   - **Definition**: Pipeline script from SCM
   - **SCM**: Git
   - **Repository URL**: Your GitHub repository URL
   - **Script Path**: `deployment/jenkins/Jenkinsfile`
6. Click **Save**

---

## 5. Application Configuration

### Update application.yaml

Ensure your `src/main/resources/application.yaml` is configured correctly:

```yaml
spring:
  application:
    name: service-week6-miniproject
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}

  datasource:
    url: ${DATABASE_URL}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

server:
  port: ${PORT:8080}

# Actuator endpoints
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env
```

### Update application-prod.yaml

```yaml
spring:
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    root: WARN
    edts.week6_practice1: INFO
```

---

## 6. Deployment Process

### Option 1: Automatic Deployment (Recommended)

#### Deploy Development Branch

1. Push to `develop` branch
2. Jenkins automatically triggers pipeline
3. Choose `development` environment when prompted
4. Jenkins builds, tests, and deploys to Heroku dev app

```bash
git checkout develop
git add .
git commit -m "feat: new feature"
git push origin develop
```

#### Deploy Production Branch

1. Merge `develop` to `main`/`master`
2. Jenkins automatically triggers pipeline
3. Choose `production` environment when prompted
4. Jenkins builds, tests, and deploys to Heroku prod app

```bash
git checkout main
git merge develop
git push origin main
```

### Option 2: Manual Deployment

1. Go to Jenkins job: `week6-practice1-deploy`
2. Click **Build with Parameters**
3. Choose:
   - `DEPLOY_ENV`: `development` or `production`
   - `RUN_TESTS`: `true` or `false`
   - `RELEASE_TAG`: Optional release tag
4. Click **Build**

---

## 7. Monitoring & Troubleshooting

### Check Application Logs

```bash
# Development logs
heroku logs --tail --app week6-practice1-dev

# Production logs
heroku logs --tail --app week6-practice1-prod

# Specific number of lines
heroku logs -n 200 --app week6-practice1-dev
```

### Check Application Health

```bash
# Using curl
curl https://week6-practice1-dev.herokuapp.com/actuator/health

# Using Heroku CLI
heroku run curl localhost:$PORT/actuator/health --app week6-practice1-dev
```

### Restart Application

```bash
heroku ps:restart --app week6-practice1-dev
```

### Scale Application

```bash
# Scale to 2 dynos
heroku ps:scale web=2 --app week6-practice1-dev

# Check dyno status
heroku ps --app week6-practice1-dev
```

### Common Issues & Solutions

#### Issue 1: Build Failed - Maven Compilation Error

**Solution:**
```bash
# Test build locally first
mvn clean package

# Check Java version
java -version  # Should be Java 17
```

#### Issue 2: Application Failed to Start

**Solution:**
```bash
# Check logs
heroku logs --tail --app week6-practice1-dev

# Common fixes:
# 1. Check DATABASE_URL is set correctly
heroku config:get DATABASE_URL --app week6-practice1-dev

# 2. Check Java version matches system.properties
cat deployment/heroku/system.properties

# 3. Check memory settings
heroku config:get JAVA_OPTS --app week6-practice1-dev
```

#### Issue 3: Health Check Failed

**Solution:**
```bash
# Ensure actuator endpoints are enabled
curl https://week6-practice1-dev.herokuapp.com/actuator

# Check if port is correctly bound
heroku config --app week6-practice1-dev
```

#### Issue 4: Database Connection Error

**Solution:**
```bash
# Verify PostgreSQL addon is installed
heroku addons --app week6-practice1-dev

# Check DATABASE_URL format
heroku config:get DATABASE_URL --app week6-practice1-dev

# Test database connection
heroku pg:psql --app week6-practice1-dev
```

---

## 8. Best Practices

### ✅ Do's

1. **Always test locally before deploying**
   ```bash
   mvn clean package && mvn test
   ```

2. **Use environment-specific profiles**
   - `dev` for development
   - `test` for testing
   - `prod` for production

3. **Keep secrets out of code**
   - Use Heroku config vars for sensitive data
   - Never commit `.env` files or API keys to Git

4. **Monitor application health**
   - Set up alerts for dyno crashes
   - Monitor response times and error rates

5. **Use meaningful commit messages**
   - Follow conventional commits format
   - Examples: `feat:`, `fix:`, `docs:`, `refactor:`

### ❌ Don'ts

1. **Don't commit sensitive data**
   - API keys
   - Database passwords
   - Private certificates

2. **Don't skip tests in production**
   - Always run tests before deploying to production

3. **Don't use hard-coded URLs**
   - Use environment variables for all configuration

4. **Don't ignore build warnings**
   - Address Maven and dependency warnings

5. **Don't deploy untested code**
   - Always run tests locally first

---

## 📊 Deployment Architecture

```
┌─────────────────┐
│   GitHub Repo   │
│  (main/develop) │
└────────┬────────┘
         │
         │ Push
         ▼
┌─────────────────┐
│     Jenkins     │
│  - Checkout     │
│  - Build (Maven)│
│  - Test         │
│  - Deploy       │
└────────┬────────┘
         │
         │ Heroku CLI
         ▼
┌─────────────────────────────┐
│        Heroku               │
│  ┌─────────┐    ┌─────────┐│
│  │   Dev   │    │  Prod   ││
│  │   App   │    │   App   ││
│  └─────────┘    └─────────┘│
│                             │
│  Heroku Postgres            │
│  (Dev & Prod)               │
└─────────────────────────────┘
```

---

## 📞 Support & Resources

### Useful Links

- [Heroku Java Support](https://devcenter.heroku.com/articles/getting-started-with-java)
- [Spring Boot on Heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

### Quick Commands Reference

```bash
# Heroku Commands
heroku login                    # Login to Heroku
heroku apps                     # List all apps
heroku logs --tail              # View live logs
heroku config                   # View config vars
heroku ps:restart               # Restart app
heroku ps:scale web=1           # Scale dynos

# Jenkins Commands (Web UI)
- Build job manually
- View console output
- Check build history

# Git Commands
git push origin main            # Deploy to production
git push origin develop         # Deploy to development
```

---

## 🎯 Summary

This deployment setup provides:

✅ **Automated CI/CD pipeline** with Jenkins
✅ **Zero-downtime deployments** to Heroku
✅ **Environment separation** (dev/prod)
✅ **Health checks** and smoke tests
✅ **Notifications** via Discord
✅ **Database integration** with Heroku PostgreSQL
✅ **Monitoring** via Spring Boot Actuator

**Next Steps:**

1. ✅ Configure Heroku apps and database
2. ✅ Set up Jenkins credentials
3. ✅ Test deployment to development environment
4. ✅ Verify health checks and monitoring
5. ✅ Deploy to production when ready

---

**Document Version**: 1.0
**Last Updated**: 2026-02-10
**Author**: Deployment Team
