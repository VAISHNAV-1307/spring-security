# 🔐 Spring Security — JWT 1: JWT Authentication Foundation

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-7.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-purple?logo=jsonwebtokens&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-jwt1-orange?logo=git)

> **Branch:** `jwt1` | **Prev:** [`example1.7`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.7) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

A major milestone — this branch introduces **JWT (JSON Web Token) authentication**, the industry standard for securing stateless REST APIs. It also upgrades to **Spring Boot 4.0.1**, the latest major version. This branch lays the conceptual and structural foundation before full JWT implementation in `jwt2`.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [What is JWT?](#what-is-jwt)
- [JWT vs Session Authentication](#jwt-vs-session-authentication)
- [Security Configuration](#security-configuration)
- [JWT Token Structure](#jwt-token-structure)
- [Authentication Flow](#authentication-flow)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `example1.7` | `jwt1` |
|---------|-------------|--------|
| Spring Boot | 3.5.7 | ✅ **4.0.1** (major version upgrade) |
| Auth mechanism | Session + cookie | ✅ JWT tokens |
| State | Stateful | ✅ **Stateless** |
| Token storage | Server-side session | ✅ Client-side JWT |
| `spring-boot-starter-webmvc` | `spring-boot-starter-web` | ✅ Explicit WebMVC starter |
| API focus | Web + REST mix | ✅ Pure REST API |

---

## 🎯 What You'll Learn

- ✅ What **JWT** is and how it works (header.payload.signature)
- ✅ Why JWT is preferred for **stateless REST APIs**
- ✅ How to configure Spring Security for **stateless session policy**
- ✅ How to create a **JWT filter** that intercepts and validates tokens
- ✅ How to set up the structural foundation for JWT-based auth
- ✅ What changed in **Spring Boot 4.x** vs 3.x

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | **4.0.1** | Application framework (latest major version) |
| Spring Security | 7.x | Authentication & Authorization |
| Spring WebMVC | Latest | REST endpoints |
| Lombok | Latest | Boilerplate reduction |
| Maven | 3.8+ | Build tool |

---

## 📦 Project Dependencies

```xml
<!-- Spring WebMVC (explicit starter in Boot 4.x) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

> **Note:** This branch sets up the **JWT infrastructure** without the `jjwt` library. The full JWT implementation with token generation and validation is in `jwt2`.

---

## 📁 Project Structure

```
SpringSecurityEx-2.1/
├── src/main/java/com/example/
│   ├── SpringSecurityEx21Application.java
│   ├── config/
│   │   └── SecurityConfig.java          # Stateless security config
│   ├── filter/
│   │   └── JwtAuthenticationFilter.java # Custom JWT filter (OncePerRequestFilter)
│   ├── controller/
│   │   ├── AuthController.java          # POST /auth/login → returns token
│   │   └── ApiController.java           # Protected REST endpoints
│   └── model/
│       ├── LoginRequest.java            # Lombok DTO
│       └── AuthResponse.java            # Lombok DTO (contains token)
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security
git checkout jwt1
mvn clean install
mvn spring-boot:run
```

App runs at → **http://localhost:8080**

---

## 🪙 What is JWT?

A **JSON Web Token** is a compact, self-contained string that encodes user identity and claims. It has three Base64URL-encoded parts separated by dots:

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl19.abc123
│                   │  │                                                  │  │     │
└── Header          ┘  └── Payload (claims)                              ┘  └─ Signature ─┘
    {alg: "HS256"}         {sub: "user", roles: ["ROLE_USER"], exp: ...}     HMACSHA256(...)
```

**Header** — Algorithm used to sign the token  
**Payload** — Claims: user identity, roles, expiry, custom data  
**Signature** — Verifies the token hasn't been tampered with

---

## ⚖️ JWT vs Session Authentication

| Aspect | Session Auth | JWT Auth |
|--------|-------------|---------|
| State | Stateful (server stores session) | ✅ Stateless (no server state) |
| Storage | Server memory / DB | Client (localStorage, memory) |
| Scalability | Hard (sticky sessions) | ✅ Easy (any server validates) |
| Token sent via | Cookie (automatic) | `Authorization: Bearer <token>` |
| Expiry | Server-controlled | Embedded in token (`exp` claim) |
| Logout | Delete session | Blacklist token or wait for expiry |
| Best for | Browser web apps | ✅ REST APIs, microservices, mobile |

---

## 🔄 Authentication Flow

```
Client                          Server
  │                               │
  │  POST /auth/login             │
  │  { username, password }  ───▶ │
  │                               │  Validate credentials
  │                               │  Generate JWT token
  │  { token: "eyJ..." }     ◀─── │
  │                               │
  │  GET /api/data                │
  │  Authorization: Bearer eyJ... │
  │                          ───▶ │
  │                               │  JwtAuthenticationFilter intercepts
  │                               │  Validates token signature
  │                               │  Extracts user + roles
  │                               │  Sets SecurityContext
  │  200 OK { data }         ◀─── │  Controller processes request
```

---

## 📡 API Endpoints

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/auth/login` | None | Submit credentials → receive JWT |
| GET | `/api/hello` | Bearer token | Protected endpoint |
| GET | `/api/profile` | Bearer token | User profile |

**Login Request:**
```json
POST /auth/login
{ "username": "user", "password": "password" }
```

**Login Response:**
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

**Authenticated Request:**
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." http://localhost:8080/api/hello
```

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| **JWT** | Self-contained token — no server-side session needed |
| **`STATELESS`** | Tell Spring Security not to create or use HTTP sessions |
| **`OncePerRequestFilter`** | Custom filter base class — executed once per request |
| **`addFilterBefore`** | Inserts your JWT filter into the security filter chain |
| **`Authorization: Bearer`** | Standard HTTP header for passing JWT tokens |
| **Spring Boot 4.x** | New major version — uses `spring-boot-starter-webmvc` explicitly |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.7">example1.7</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/jwt2">jwt2 ➡️</a>
</p>