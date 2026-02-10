# 🔧 Cara Install Heroku CLI di Jenkins

## ⚠️ Penting

Heroku CLI **WAJIB** terinstall di Jenkins agent/Server untuk deployment bisa berjalan. Tanpa Heroku CLI, pipeline akan gagal di stage "Deploy to Heroku".

---

## 🚀 Metode Instalasi

Pilih salah satu metode di bawah ini yang sesuai dengan setup Jenkins Anda:

---

## **Metode 1: Via Snap (Ubuntu/Debian) - RECOMMENDED**

Cara termudah jika Jenkins jalan di Ubuntu/Debian.

### **SSH ke Jenkins Server:**
```bash
ssh jenkins@your-jenkins-server
# atau
ssh user@jenkins-server
```

### **Install Heroku CLI:**
```bash
sudo snap install heroku --classic
```

### **Verify Instalasi:**
```bash
heroku --version
# Output: heroku/9.x.x (atau versi lain)
```

---

## **Metode 2: Via Curl Script (All Linux)**

Universal untuk semua distro Linux.

### **SSH ke Jenkins Server:**
```bash
ssh jenkins@your-jenkins-server
```

### **Install Heroku CLI:**
```bash
curl https://cli-assets.heroku.com/install.sh | sh
```

### **Verify Instalasi:**
```bash
heroku --version
```

---

## **Metode 3: Via NPM**

Gunakan ini jika Node.js & npm sudah terinstall di Jenkins.

### **SSH ke Jenkins Server:**
```bash
ssh jenkins@your-jenkins-server
```

### **Install Heroku CLI:**
```bash
npm install -g heroku
```

### **Verify Instalasi:**
```bash
heroku --version
```

---

## **Metode 4: Download Binary Manual**

Gunakan ini jika tidak ada snap, curl, atau npm.

### **SSH ke Jenkins Server:**
```bash
ssh jenkins@your-jenkins-server
```

### **Download & Install:**
```bash
# Untuk Linux x64 (Intel/AMD)
wget https://github.com/heroku/cli/releases/download/v9.1.0/heroku-cli-linux-x64.tar.gz
tar -xzf heroku-cli-linux-x64.tar.gz
sudo mv heroku /usr/local/bin/

# Untuk Linux ARM64 (ARM/Apple Silicon)
wget https://github.com/heroku/cli/releases/download/v9.1.0/heroku-cli-linux-arm64.tar.gz
tar -xzf heroku-cli-linux-arm64.tar.gz
sudo mv heroku /usr/local/bin/
```

### **Verify Instalasi:**
```bash
heroku --version
```

---

## **Metode 5: Jika Jenkins di Docker**

Jika Jenkins jalan sebagai Docker container, tambahkan ke Dockerfile:

### **Option A: Base Image Ubuntu/Debian**
```dockerfile
FROM jenkins/jenkins:lts

# User root untuk instalasi
USER root

# Install Heroku CLI
RUN curl https://cli-assets.heroku.com/install.sh | sh

# Kembali ke user jenkins
USER jenkins
```

### **Option B: Via Snap**
```dockerfile
FROM jenkins/jenkins:lts

USER root

# Install snapd dulu
RUN apt-get update && \
    apt-get install -y snapd && \
    snap install heroku --classic

USER jenkins
```

### **Option C: Via NPM**
```dockerfile
FROM jenkins/jenkins:lts

USER root

# Install Node.js & npm dulu
RUN apt-get update && \
    apt-get install -y nodejs npm && \
    npm install -g heroku

USER jenkins
```

### **Build & Run:**
```bash
docker build -t jenkins-with-heroku .
docker run -p 8080:8080 jenkins-with-heroku
```

---

## **Metode 6: Jika Jenkins di Kubernetes**

Install via Init Container atau dalam pod spec:

### **Option A: Init Container**
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: jenkins
spec:
  initContainers:
  - name: install-heroku
    image: ubuntu:latest
    command:
    - sh
    - -c
    - |
      curl https://cli-assets.heroku.com/install.sh | sh
      cp /usr/local/bin/heroku /heroku-bin/
    volumeMounts:
    - name: heroku-bin
      mountPath: /heroku-bin
  containers:
  - name: jenkins
    image: jenkins/jenkins:lts
    volumeMounts:
    - name: heroku-bin
      mountPath: /usr/local/bin/heroku
  volumes:
  - name: heroku-bin
    emptyDir: {}
```

---

## ✅ Verifikasi Instalasi

Setelah install, verifikasi dengan:

### **Di Jenkins Server/Agent:**
```bash
heroku --version
# Output: heroku/9.x.x linux-x64/node-x.x.x

which heroku
# Output: /usr/local/bin/heroku (atau path lain)
```

### **Test Connection:**
```bash
heroku auth:whoami
# Akan minta login, atau error jika belum authenticated
```

---

## 🧪 Test di Jenkins Pipeline

Setelah install, jalankan pipeline lagi. Di stage "Check Heroku CLI" akan muncul:

```
✅ Heroku CLI already installed
heroku/9.x.x linux-x64/node-x.x.x
```

Jika masih muncul warning, berarti:
1. Heroku CLI terinstall di host, tapi Jenkins container tidak bisa akses
2. Perlu restart Jenkins service/agent
3. PATH environment tidak sesuai

---

## 📝 Troubleshooting

### **Problem 1: "command not found: heroku"**

**Solution:** Tambahkan ke PATH atau buat symlink
```bash
# Cari lokasi heroku
sudo find / -name heroku -type f 2>/dev/null

# Tambahkan ke PATH (temporary)
export PATH="/path/to/heroku:$PATH"

# Atau buat symlink
sudo ln -s /path/to/heroku /usr/local/bin/heroku
```

### **Problem 2: Jenkins Pipeline masih tidak bisa akses heroku**

**Solution:** Install heroku CLI sebagai user yang menjalankan Jenkins
```bash
# Cek jenkins user
ps aux | grep jenkins

# Install sebagai jenkins user
sudo -u jenkins curl https://cli-assets.heroku.com/install.sh | sh
```

### **Problem 3: Docker Jenkins - heroku tidak persistent**

**Solution:** Mount volume atau install di Docker image
```yaml
volumes:
  - heroku-cli:/usr/local/bin
```

---

## 📚 Referensi

- [Heroku CLI Documentation](https://devcenter.heroku.com/articles/heroku-cli)
- [Heroku CLI GitHub](https://github.com/heroku/cli)
- [Jenkins Docker Image](https://github.com/jenkinsci/docker)

---

## 🎯 Summary

| Metode | OS | Difficulty | Recommended |
|--------|-------|------------|-------------|
| Snap | Ubuntu/Debian | ⭐ Easy | ✅ YES |
| Curl | All Linux | ⭐⭐ Medium | ✅ YES |
| NPM | Any (with Node.js) | ⭐ Easy | ✅ |
| Binary | All Linux | ⭐⭐⭐ Hard | ❌ |
| Dockerfile | Docker | ⭐⭐ Medium | ✅ |

---

**Dokumentasi ini dibuat untuk project Week6 Practice1 - Spring Boot Deployment**

Last Updated: 2026-02-10
