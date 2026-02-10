# 🔍 Heroku Deployment Troubleshooting Guide

## ❌ Smoke Test Failed: HTTP 000000

Jika smoke test gagal dengan `HTTP_CODE=000000`, berarti aplikasi tidak bisa diakses. Berikut langkah troubleshooting:

---

## 📋 Step-by-Step Troubleshooting

### **Step 1: Cek Status Heroku App**

```bash
# Cek apakah app jalan
heroku ps --app week6-practice1-dev

# Expected output (kalau app jalan):
# === web (Free): web.1: up 2025/02/10 08:00:00 +0700 (~ 1m ago)

# Expected output (kalau app mati):
# No dynos running
```

**Jika app mati, lanjut ke Step 2.**

---

### **Step 2: Cek Heroku Logs**

```bash
# Lihat live logs
heroku logs --tail --app week6-practice1-dev

# Atau lihat 100 baris terakhir
heroku logs -n 100 --app week6-practice1-dev
```

**Cari error seperti:**
- `Error R10 (Boot timeout) → Web process failed to bind to Port`
- `Error: Could not find or load main class`
- `Connection refused`
- `Failed to start application`

---

### **Step 3: Cek Buildpack**

```bash
heroku buildpacks --app week6-practice1-dev

# Expected:
# heroku-buildpack-java is set

# Jika kosong:
heroku buildpacks:set https://github.com/heroku/heroku-buildpack-java --app week6-practice1-dev
```

---

### **Step 4: Cek Environment Variables**

```bash
# Lihat semua config vars
heroku config --app week6-practice1-dev

# Pastikan ini ada:
# ✅ JAVA_OPTS
# ✅ SPRING_PROFILES_ACTIVE (dev atau prod)
# ✅ DB_URL
# ✅ DB_USERNAME
# ✅ DB_PASSWORD
# ✅ DRIVER_MYSQL
```

---

### **Step 5: Cek Deploy Status**

```bash
# Cek release aktif
heroku releases --app week6-practice1-dev

# Expected output:
# v1.0.0  Config vars and settings
```

**Jika tidak ada release:** Deploy mungkin gagal. Coba push ulang secara manual:

```bash
# Push manual untuk test
git push https://heroku:${HEROKU_API_KEY}@git.heroku.com/week6-practice1-dev.git HEAD:refs/heads/main --force
```

---

### **Step 6: Restart Aplikasi**

```bash
# Restart dyno
heroku ps:restart --app week6-practice1-dev

# Tunggu 30 detik
sleep 30

# Cek lagi
heroku ps --app week6-practice1-dev
```

---

### **Step 7: Test Manual Health Check**

```bash
# Dapatkan URL app
APP_URL=$(heroku apps:info --app week6-practice1-dev --json | grep -o '"web_url":"[^"]*"' | cut -d'"' -f4)

# Test health endpoint
curl https://week6-practice1-dev.herokuapp.com/actuator/health

# Atau pakai app URL yang didapat
curl ${APP_URL}/actuator/health
```

---

## 🔥 Common Issues & Solutions

### **Issue 1: Build Failed - Maven Error**

**Error:** `Could not find or load main class`

**Solution:**
1. Cek `pom.xml` - pastikan `<packaging>jar</packaging>`
2. Build manual untuk test: `mvn clean package`
3. Pastikan JAR file dibuat di `target/`

---

### **Issue 2: Port Binding Error**

**Error:** `Error R10 (Boot timeout) → Web process failed to bind to Port`

**Solution:**
- Pastikan `server.port=${PORT}` di `application.yaml`
- Heroku inject PORT environment variable
- Aplikasi HARUS listen di `$PORT`, bukan port hardcode

---

### **Issue 3: Database Connection Error**

**Error:** `Connection refused` atau `Could not open connection`

**Solution:**
1. Cek DATABASE_URL di Heroku config:
   ```bash
   heroku config:get DB_URL --app week6-practice1-dev
   ```

2. Pastikan format:
   ```
   jdbc:mysql://host:port/database?ssl=true
   ```

3. Pastikan database eksternal bisa diakses dari Heroku:
   - Firewall allow Heroku IPs
   - Network connectivity OK

---

### **Issue 4: Out of Memory (OOM)**

**Error:** `java.lang.OutOfMemoryError` atau `Killed`

**Solution:**
```bash
# Increase Java memory
heroku config:set JAVA_OPTS="-Xmx768m -Xms256m" --app week6-practice1-dev

# Restart app
heroku ps:restart --app week6-practice1-dev
```

---

### **Issue 5: Application Start Timeout**

**Error:** `Error R10 (Boot timeout) → Web process failed to bind to Port within 60 seconds`

**Possible causes:**
1. App terlalu lama start
2. Database connection lambat
3. Terlalu banyak initialization

**Solution:**
```yaml
# Di application.yaml, tambahkan:
server:
  port: ${PORT:8080}
  compression:
    enabled: false  # Disable untuk lebih cepat start
```

---

## 🚀 Quick Fixes

### **Fix 1: Full Deploy Ulang**

```bash
# 1. Force push ulang
git push https://heroku:${HEROKU_API_KEY}@git.heroku.com/week6-practice1-dev.git HEAD:refs/heads/main --force

# 2. Restart
heroku ps:restart --app week6-practice1-dev

# 3. Tunggu 1 menit
sleep 60

# 4. Test
curl https://week6-practice1-dev.herokuapp.com/actuator/health
```

---

### **Fix 2: Scale Up Dyno**

```bash
# Scale ke Standard-1x (more resources)
heroku ps:scale web=standard-1x --app week6-practice1-dev
```

---

### **Fix 3: Rebuild App**

```bash
# Clear build cache dan rebuild
heroku plugins:unset heroku/nodejs --app week6-practice1-dev
heroku buildpacks:clear --app week6-practice1-dev

# Push ulang
git push https://heroku:${HEROKU_API_KEY}@git.heroku.com/week6-practice1-dev.git HEAD:refs/heads/main --force
```

---

## 📊 Checklist Sebelum Deploy

Pastikan ini sudah OK:

- [ ] **Maven build sukses**
  ```bash
  mvn clean package
  ls -lh target/Week6_Practice1-0.0.1-SNAPSHOT.jar
  ```

- [ ] **Spring Boot bisa jalan lokal**
  ```bash
  mvn spring-boot:run
  # Buka http://localhost:8080/actuator/health
  ```

- [ ] **Port configuration benar**
  ```yaml
  server:
    port: ${PORT:8080}  # MUST use $PORT for Heroku!
  ```

- [ ] **Database credentials ada di Jenkins**
  - db-url
  - db-username
  - db-password
  - db-driver

- [ ] **Heroku API Key valid**
  ```bash
  heroku auth:whoami
  # Harus tampil username Heroku Anda
  ```

---

## 🔗 Useful Commands

```bash
# Cek semua
heroku apps:info --app week6-practice1-dev
heroku ps --app week6-practice1-dev
heroku config --app week6-practice1-dev
heroku logs --tail --app week6-practice1-dev

# Restart
heroku ps:restart --app week6-practice1-dev

# Scale
heroku ps:scale web=1 --app week6-practice1-dev

# Open browser
heroku open --app week6-practice1-dev
```

---

## 📞 Butuh Bantuan?

Jika masih gagal, jalankan ini dan kirim output:

```bash
# Full diagnostics
echo "=== App Info ==="
heroku apps:info --app week6-practice1-dev

echo ""
echo "=== Dyno Status ==="
heroku ps --app week6-practice1-dev

echo ""
echo "=== Recent Logs ==="
heroku logs -n 50 --app week6-practice1-dev

echo ""
echo "=== Config Vars ==="
heroku config --app week6-practice1-dev
```

---

**Last Updated:** 2026-02-10
