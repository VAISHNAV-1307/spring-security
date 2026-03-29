# 🔐 Spring Security — JWT 2: Full JWT Implementation with JPA + MySQL

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-7.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JJWT-0.11.5-purple?logo=jsonwebtokens&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql&logoColor=white)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.x-brightgreen?logo=spring)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-jwt2-orange?logo=git)

> **Branch:** `jwt2` | **Prev:** [`jwt1`](https://github.com/VAISHNAV-1307/spring-security/tree/jwt1) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

The **capstone branch** of this series. This branch delivers a **complete, production-ready JWT authentication system** — users stored in MySQL, passwords hashed with BCrypt, JWT tokens generated and validated with the **JJWT 0.11.5** library, all running on **Spring Boot 4.0.2**.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Getting Started](#getting-started)
- [Security Architecture](#security-architecture)
- [Key Components](#key-components)
- [JWT Token Lifecycle](#jwt-token-lifecycle)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `jwt1` | `jwt2` |
|---------|--------|--------|
| Spring Boot | 4.0.1 | ✅ **4.0.2** |
| JWT library | Not included | ✅ **JJWT 0.11.5** (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) |
| User storage | In-memory | ✅ **MySQL via JPA** |
| Token generation | Stub | ✅ Real HS256-signed JWT |
| Token validation | Stub | ✅ Signature + expiry verification |
| Token claims | N/A | ✅ `sub`, `roles`, `iat`, `exp` |
| Refresh tokens | ❌ | ✅ Foundation included |

---

## 🎯 What You'll Learn

- ✅ How to generate JWTs using the **JJWT library** (HS256 signing)
- ✅ How to validate JWT **signature and expiry**
- ✅ How to extract **claims** (username, roles) from a token
- ✅ How to combine **JPA + MySQL** user storage with JWT auth
- ✅ How to build a complete **login endpoint** that returns a JWT
- ✅ How to protect REST endpoints with **bearer token authentication**
- ✅ How to handle **token expiry** and **invalid token** errors gracefully

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | **4.0.2** | Application framework |
| Spring Security | 7.x | Security layer |
| Spring WebMVC | Latest | REST API |
| Spring Data JPA | Latest | Database ORM |
| Hibernate | 6.x+ | JPA implementation |
| MySQL | 8.x | User persistence |
| **JJWT** | **0.11.5** | JWT generation & validation |
| Lombok | Latest | Boilerplate reduction |
| Maven | 3.8+ | Build tool |

---

## 📦 Project Dependencies

```xml
<!-- JPA + Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring WebMVC -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- MySQL Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- JJWT — JWT generation, parsing, validation -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
</dependency>
```

---

## 📁 Project Structure

```
SpringSecurityEx-2.2/
├── src/main/java/com/
│   ├── SpringSecurityEx22Application.java
│   ├── config/
│   │   └── SecurityConfig.java              # Stateless, adds JwtFilter
│   ├── filter/
│   │   └── JwtAuthenticationFilter.java     # Validates Bearer token per request
│   ├── service/
│   │   ├── JwtService.java                  # generateToken, validateToken, extractClaims
│   │   ├── AuthService.java                 # Login logic
│   │   └── CustomUserDetailsService.java    # Loads user from MySQL
│   ├── controller/
│   │   ├── AuthController.java              # POST /auth/login, /auth/register
│   │   └── ApiController.java              # Protected endpoints
│   ├── model/
│   │   ├── User.java                        # @Entity with Lombok
│   │   ├── LoginRequest.java                # Lombok DTO
│   │   └── AuthResponse.java               # { token, expiry }
│   └── repository/
│       └── UserRepository.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

---

## 🗄️ Database Setup

```sql
CREATE DATABASE spring_security_jwt;
USE spring_security_jwt;

CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

-- BCrypt-encoded "password"
INSERT INTO users (username, password, role) VALUES
('user',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_USER'),
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN');
```

---

## ⚙️ Configuration

```properties
# Server
server.port=8080

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/spring_security_jwt
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
jwt.secret=your-256-bit-secret-key-here-minimum-32-characters
jwt.expiration=86400000
```

---

## 🚀 Getting Started

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security
git checkout jwt2
```

1. Create MySQL database and insert sample users (see [Database Setup](#database-setup))
2. Update `application.properties` with your MySQL credentials and JWT secret
3. Build and run:

```bash
mvn clean install
mvn spring-boot:run
```

App runs at → **http://localhost:8080**

---

## 🏗️ Security Architecture

```
  POST /auth/login { username, password }
           │
           ▼
  AuthController → AuthService
           │
           ▼
  CustomUserDetailsService.loadUserByUsername()
           │
           ▼
  UserRepository.findByUsername() → MySQL
           │
           ▼
  BCryptPasswordEncoder.matches()
           │
    ┌──────┴──────┐
  PASS          FAIL → 401
    │
    ▼
  JwtService.generateToken(username, roles)
    ├─ Sets subject: username
    ├─ Sets claims: roles
    ├─ Sets expiry: now + 24h
    └─ Signs with HS256 + secret key
           │
           ▼
  Returns { token: "eyJ..." } → Client

─────────────────────────────────────────────────

  GET /api/resource
  Authorization: Bearer eyJ...
           │
           ▼
  JwtAuthenticationFilter.doFilterInternal()
    ├─ Extract token from header
    ├─ JwtService.validateToken()
    │   ├─ Verify HS256 signature
    │   └─ Check expiry
    ├─ JwtService.extractUsername()
    └─ Set SecurityContextHolder
           │
           ▼
  Controller processes request → 200 OK
```

---

## 🪙 JWT Token Lifecycle

```
1. LOGIN      → Client sends credentials → Server returns JWT
2. STORE      → Client stores JWT (memory, localStorage)
3. REQUEST    → Client sends JWT in Authorization header
4. VALIDATE   → Server verifies signature + expiry
5. AUTHORIZE  → Server extracts roles, allows/denies access
6. EXPIRY     → Token expires → Client must login again
7. LOGOUT     → Client discards token (server-side blacklist optional)
```

---

## 📡 API Endpoints

| Method | URL | Auth | Body | Description |
|--------|-----|------|------|-------------|
| POST | `/auth/login` | None | `{ username, password }` | Login → returns JWT |
| POST | `/auth/register` | None | `{ username, password, role }` | Register new user |
| GET | `/api/hello` | Bearer JWT | — | Protected greeting |
| GET | `/api/profile` | Bearer JWT | — | Current user profile |
| GET | `/api/admin` | Bearer JWT (ADMIN) | — | Admin-only endpoint |

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| **JJWT library** | `jjwt-api` (interfaces) + `jjwt-impl` (implementation) + `jjwt-jackson` (JSON) |
| **HS256** | HMAC-SHA256 symmetric signing — one secret key to sign and verify |
| **Token claims** | `sub` = username, `exp` = expiry, custom claims for roles |
| **Stateless** | Server holds NO session — entire auth state is inside the JWT |
| **`OncePerRequestFilter`** | Ensures JWT filter runs exactly once per HTTP request |
| **Secret key security** | Store in env variables / secrets vault — never in source code |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/jwt1">jwt1</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; 🎉 Series Complete!
</p>