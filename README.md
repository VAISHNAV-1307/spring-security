# 🔐 Spring Security — Example 1.3: Custom Security Configuration

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-example1.3-orange?logo=git)

> **Branch:** `example1.3` | **Prev:** [`example1.2`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.2) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

This branch moves beyond Spring Boot's auto-configuration and introduces a **custom `SecurityFilterChain`** bean with explicit in-memory users, roles, and URL-level access rules — using **Lombok** to keep model classes clean.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Security Configuration](#security-configuration)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `example1.1` | `example1.3` |
|---------|-------------|-------------|
| Security config | Auto-configured | ✅ Custom `SecurityConfig` class |
| Users | Single auto-generated user | ✅ Multiple in-memory users with roles |
| URL rules | All locked | ✅ Per-route role-based access |
| Password encoder | Auto | ✅ Explicit `BCryptPasswordEncoder` |
| Lombok | ❌ | ✅ Used for model/DTO classes |

---

## 🎯 What You'll Learn

- ✅ How to write a custom **`SecurityFilterChain`** bean
- ✅ How to define **multiple in-memory users** with different roles
- ✅ How to restrict URLs by role using **`requestMatchers`**
- ✅ How to configure **`BCryptPasswordEncoder`** as a Spring bean
- ✅ How **Lombok** reduces boilerplate in model classes
- ✅ The difference between `permitAll()`, `authenticated()`, and `hasRole()`

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Web MVC | 6.x | REST endpoints |
| Lombok | Latest | Boilerplate reduction (`@Data`, `@Builder`, etc.) |
| Maven | 3.8+ | Build tool |

---

## 📦 Project Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 📁 Project Structure

```
SpringSecurityEx3/
├── src/main/java/com/example/SpringSecurityEx3/
│   ├── SpringSecurityEx3Application.java
│   ├── config/
│   │   └── SecurityConfig.java         # Custom SecurityFilterChain + users
│   ├── controller/
│   │   ├── PublicController.java       # Open endpoints
│   │   ├── UserController.java         # USER role endpoints
│   │   └── AdminController.java        # ADMIN role endpoints
│   └── model/                          # Lombok-annotated models
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security
git checkout example1.3
mvn clean install
mvn spring-boot:run
```

App runs at → **http://localhost:8080**

---


## 📡 API Endpoints

| Method | URL | Access | Description |
|--------|-----|--------|-------------|
| GET | `/public/hello` | All | Public greeting |
| GET | `/user/profile` | USER, ADMIN | User profile |
| GET | `/admin/dashboard` | ADMIN only | Admin dashboard |
| POST | `/login` | All | Login |
| POST | `/logout` | Authenticated | Logout |


---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| `SecurityFilterChain` | Define as a `@Bean` to override Spring Boot's defaults |
| `requestMatchers` | Pattern-match URLs and assign access rules |
| `hasRole` / `hasAnyRole` | Role-based authorization — Spring auto-prepends `ROLE_` |
| `BCryptPasswordEncoder` | Always encode passwords; never store plain text |
| Lombok | `@Data`, `@Builder`, `@AllArgsConstructor` remove boilerplate |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.2">example1.2</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.4">example1.4 ➡️</a>
</p>