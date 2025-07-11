# Prime Number Generator 🔢

[![Build](https://img.shields.io/github/actions/workflow/status/praveend3veloper/prime-number-generator/maven-publish.yml?branch=main)](https://github.com/praveend3veloper/prime-number-generator/actions)
[![Coverage Status](https://coveralls.io/repos/github/praveenD3veloper/prime-number-generator/badge.svg)](https://coveralls.io/github/praveenD3veloper/prime-number-generator)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=praveenD3veloper_prime-number-generator&metric=alert_status)](https://sonarcloud.io/dashboard?id=praveenD3veloper_prime-number-generator)
[![License](https://img.shields.io/github/license/praveend3veloper/prime-number-generator)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Code Style: Sun Checks](https://img.shields.io/badge/code%20style-sun-yellow)](sun_checks.xml)

> ⚡ A high-performance, extensible REST API for generating prime numbers using multiple algorithms (Brute Force, Sieve of Eratosthenes, and its parallel variant).

---

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Usage](#usage)
- [Build & Run](#build--run)
- [Testing](#testing)
- [API Reference](#api-reference)
- [Extending Algorithms](#extending-algorithms)
- [Contributing](#contributing)
- [License](#license)

---

## ✨ Features

- 🔢 Generate all prime numbers up to a given range.
- 🔁 Multiple algorithm strategies:
  - Brute Force
  - Sieve of Eratosthenes
  - Parallel Sieve (multithreaded)
- ⚙️ Strategy pattern for easy extension.
- 📦 Maven project with CI via GitHub Actions.
- 📜 Detailed custom error handling and validation.
- ✅ Unit, Integration, and Controller tests.
- ☕ Java 21 & Maven wrapper based.

---

## 🏛 Architecture

```text
com.prav.prime
├── controller              # REST endpoints
├── core
│   ├── PrimeGenerator      # Common interface
│   ├── StrategyConfig      # Spring config
│   ├── StrategySelector    # Chooses algorithm
│   └── algorithm
│       ├── BruteForce
│       ├── SieveOfEratosthenes
│       └── SieveOfEratosthenesParallel
├── service
│   └── PrimeNumberService  # Business logic
├── model
│   └── response.Result     # Output model
└── exception
    ├── CustomError
    └── CustomExceptionHandler
```

---

## 🚀 Usage

Run the app locally:

```bash
./mvnw spring-boot:run
```

Sample API:

```bash
GET /primes/30?algo=sieve
```

Response:

```json
{
  "initial": 30,
  "primes": [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
}
```

---

## 🛠️ Build & Run

### Build

```bash
./mvnw clean install -Dcheckstyle.config.location=sun_checks.xml
```

### Package JAR

```bash
./mvnw package
```

JAR will be located in `target/prime-number-generator-<version>.jar`

### Run

```bash
java -jar target/prime-number-generator-*.jar
```

---

## 🧪 Testing

### Run All Tests

```bash
./mvnw test
```

## 🧪 Test Coverage

Use tools like JaCoCo if integrated. For comprehensive coverage reporting and analysis, consider:

- **☁️ SonarQube Cloud**: Provides detailed code quality metrics, test coverage analysis, and security vulnerability detection with seamless CI/CD integration
- **📊 Coveralls**: Offers coverage tracking and reporting with pull request integration, supporting multiple languages and build systems
- **☕ JaCoCo**: Java-specific coverage tool that generates detailed reports and integrates well with Maven/Gradle builds

**Generate JaCoCo Report Locally:**
```bash
mvn clean test jacoco:report
```

**Report Location:** `target/site/jacoco/index.html`

These tools can be configured to automatically generate coverage reports and provide insights into code quality trends over time.


---

## 📡 API Reference

| Method | Endpoint | Description | Query Param                                                                            |
|--------|----------|-------------|----------------------------------------------------------------------------------------|
| GET | `/primes/{range}` | Get all primes ≤ range | `algorithm` (optional: bruteForce, sieveOfEratosthenes, sieveOfEratosthenesInParallel) |

Example:

```bash
GET /primes/100?algorithm=parallel
```
## 🧮 Algorithm Information

The Prime Number Generator service supports multiple algorithms with varying performance characteristics:

### 🐌 Brute Force Algorithm
**Description:** The most basic and inefficient approach that checks divisibility for every number up to the target.

**Usage:** Add `?algorithm=bruteForce` to your request
```bash
# Local development
curl http://localhost:8080/primes/100?algorithm=bruteForce

# Production service
curl https://github-action-deploy-foun5bafea-nw.a.run.app/primes/100?algorithm=bruteForce
```

### 🎯 Sieve of Eratosthenes (Default)
**Description:** A more efficient algorithm that eliminates multiples of known primes. This is the **default implementation** used when no algorithm is specified or an invalid algorithm is provided.

**Usage:**
```bash
# Explicit algorithm selection
curl http://localhost:8080/primes/100?algorithm=SieveOfEratosthenes

# Default behavior (no algorithm parameter)
curl http://localhost:8080/primes/100
```

### ⚡ Sieve of Eratosthenes in Parallel
**Description:** An optimized concurrent implementation that leverages Java's multithreading capabilities to achieve superior performance for larger number ranges.

**Usage:** Add `?algorithm=SieveOfEratosthenesInParallel` to your request
```bash
# Local development
curl http://localhost:8080/primes/100?algorithm=SieveOfEratosthenesInParallel

# Production service
curl https://github-action-deploy-foun5bafea-nw.a.run.app/primes/100?algorithm=SieveOfEratosthenesInParallel
```

### 📊 Performance Comparison

| Algorithm | Performance | Use Case |
|-----------|-------------|----------|
| 🐌 Brute Force | Slowest | Educational/Testing |
| 🎯 Sieve of Eratosthenes | Good | General purpose |
| ⚡ Parallel Sieve | Fastest | Large number ranges |

> **💡 Tip:** For optimal performance with large ranges (>10,000), use the parallel implementation.

---

## 🐳 Docker Setup

### 📦 Build the Docker Image

First, ensure your Spring Boot application is compiled:

```bash
docker build -t prime-number-generator .
```

## 🚀 Run the Container Locally
### Run the container with port mapping:
```bash
docker run -p 8080:8080 prime-number-generator
```
## 🔍 Verify the Application
### Once the container is running, test the application:
```bash
# Check if the application is responding
curl http://localhost:8080/actuator/health
```
---

## ☁️ Cloud Run Deployment

This repository has a **GitHub Actions workflow** configured for **continuous deployment**. Every merge to the main branch and pull request automatically triggers:

- 🔨 **Build** - Compiles and packages the application
- 🐳 **Containerize** - Creates Docker image using distroless Java 21
- 🚀 **Deploy** - Pushes to Google Cloud Run service

⚡ Cold Start Notice

📢 Important: Cloud Run is a serverless platform that scales to zero when idle. The service may experience a cold start (2-3 seconds delay) on the first request after an idle period. Subsequent requests will be fast and responsive.
### 🌐 Service Endpoint

**Base URL:** `https://prime-generator-6v2nx24hua-nw.a.run.app`

**API Endpoint:** `/primes/{range}`

### 📝 Example Usage

```bash
# Get prime numbers up to 10
curl https://https://prime-generator-6v2nx24hua-nw.a.run.app/primes/10?algorithm=bruteForce
```
---

## 🧩 Extending Algorithms

To add a new algorithm:

1. Implement `PrimeGenerator` interface.
2. Annotate with `@Component`.
3. Update `StrategyConfig` and `StrategySelector` if needed.

---

## 🧰 Development Notes

- **Java**: 21+
- **Build Tool**: Maven (Wrapper included)
- **Code Style**: Sun Java conventions via `sun_checks.xml`
- **CI/CD**: GitHub Actions (`.github/workflows/maven-publish.yml`) (`.github/workflows/gcp-deploy.yml`)
- **Infra**: Google Cloud
 
---

## 🤝 Contributing

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the GPL License.

---

## 👤 Author
- GitHub: [@praveend3veloper](https://github.com/praveend3veloper)
- 📧 Email/contact info can go here if public

---
