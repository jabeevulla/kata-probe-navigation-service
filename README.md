# 🚀 Ocean Probe Navigation Service
## 📌 Overview 
This service provide APIs Register a probe and control the probe navigation using set of commands.

---

## 📦 Features 
✅ **Register A Probe with navigation controller service** : Register and create Probe device identity.

✅ **Ocean floor representation with obstacles as 2D grid** : Ocean floor represented as 2D grid.

✅ **Move the probe in directions** : Move forwards and backwards.

✅ **Change directions** : Turn left and right.

✅ **Avoid obstacles** : Avoid obstacles in the grid.

✅ **Stay on the grid**

✅ **Print navigation trail** : Print a summary of the co-ordinates visited.

---

## 📚 API Documentation 
- Swagger UI: --TODO--

---

## 🚀 Quick Start Guide
### **1️⃣ Prerequisites**
Ensure you have the following installed:
- [JDK 17+](https://adoptium.net/)
- [Maven 3+](https://maven.apache.org/)

### **2️⃣ Clone the Repository**
```sh 
git clone git@github.com:jabeevulla/kata-probe-navigation-service.git
cd kata-probe-navigation-service

```
### **4️⃣ Run the Application**
```sh
mvn clean install
mvn spring-boot:run
```

### **5️⃣ Test the API**
```sh
TODO - Add CURL
```

---
### **🛠️ Project Structure**
```
├── HELP.md
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── codekata
│   │   │           └── oceanprobe
│   │   │               └── probenavigationservice
│   │   │                   ├── KataProbeNavigationServiceApplication.java
│   │   │                   ├── controller
│   │   │                   ├── dto
│   │   │                   ├── entity
│   │   │                   ├── exception
│   │   │                   ├── repository
│   │   │                   └── service
│   │   └── resources
│   │       ├── application.properties
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── codekata
│                   └── oceanprobe
│                       └── probenavigationservice
│                           └── KataProbeNavigationServiceApplicationTests.java

```

---

### **✅ Running Tests**
Run **unit & integration** tests:

```sh
mvn test
```

---
### **📦 Deployment**
#### **TODO**: 

---

## 🛠️ System design
### **TODO**
