# Prime Number Generator 🔢

[![Build](https://img.shields.io/github/actions/workflow/status/praveend3veloper/prime-number-generator/maven-publish.yml?branch=main)](https://github.com/praveend3veloper/prime-number-generator/actions)
[![Coverage Status](https://coveralls.io/repos/github/praveenD3veloper/prime-number-generator/badge.svg?branch=main)](https://coveralls.io/github/praveenD3veloper/prime-number-generator?branch=main)
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

### Test Coverage (manual)

Use tools like JaCoCo if integrated.

---

## 📡 API Reference

| Method | Endpoint | Description | Query Param |
|--------|----------|-------------|-------------|
| GET | `/primes/{range}` | Get all primes ≤ range | `algo` (optional: bruteforce, sieve, parallel) |

Example:

```bash
GET /primes/100?algo=parallel
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
- **CI/CD**: GitHub Actions (`.github/workflows/maven-publish.yml`)

---

## 🤝 Contributing

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

## 👤 Author

**Praveen D**
- GitHub: [@praveend3veloper](https://github.com/praveend3veloper)
- 📧 Email/contact info can go here if public

---

*Let me know if you'd like:*
- *A visual logo/banner*
- *Docker support added to README*
- *Shields for coverage or JDK compatibility*
- *Swagger/OpenAPI integration section*

*I can tailor this further depending on audience (tech recruiters, OSS contributors, etc.).*