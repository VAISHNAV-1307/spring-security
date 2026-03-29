# 🔐 Spring Security — Example 1.7: Method-Level Authorization

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-example1.7-orange?logo=git)

> **Branch:** `example1.7` | **Prev:** [`example1.6`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.6) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

This branch introduces **Method-Level Security** — the ability to enforce authorization rules directly on service methods using annotations like `@PreAuthorize`, `@PostAuthorize`, `@Secured`, and `@RolesAllowed`. This is a major step toward fine-grained, domain-driven security.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Enabling Method Security](#enabling-method-security)
- [Annotation Reference](#annotation-reference)
- [Code Examples](#code-examples)
- [URL Security vs Method Security](#url-security-vs-method-security)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `example1.6` | `example1.7` |
|---------|-------------|-------------|
| Authorization level | URL patterns only | ✅ Method-level annotations |
| `@PreAuthorize` | ❌ | ✅ Check roles before method runs |
| `@PostAuthorize` | ❌ | ✅ Check return value after method runs |
| `@Secured` | ❌ | ✅ Simple role check on method |
| Spring Expression Language (SpEL) | ❌ | ✅ Used in `@PreAuthorize` |
| Spring Boot version | 3.5.6 | **3.5.7** |

---

## 🎯 What You'll Learn

- ✅ How to enable method security with **`@EnableMethodSecurity`**
- ✅ How `@PreAuthorize` uses **SpEL expressions** for pre-invocation checks
- ✅ How `@PostAuthorize` validates the **return value** after the method runs
- ✅ The difference between `@Secured` and `@PreAuthorize`
- ✅ How to use `authentication.name` and `hasRole()` inside SpEL
- ✅ When to use **URL security vs method security**

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | **3.5.7** | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Web MVC | 6.x | REST endpoints |
| Lombok | Latest | Boilerplate reduction |
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
SpringSecurityEx7/
├── src/main/java/com/example/SpringSecurityEx7/
│   ├── SpringSecurityEx7Application.java
│   ├── config/
│   │   └── SecurityConfig.java        # @EnableMethodSecurity
│   ├── controller/
│   │   └── ResourceController.java
│   ├── service/
│   │   └── ResourceService.java       # @PreAuthorize, @PostAuthorize on methods
│   └── model/
│       └── Resource.java              # Lombok-annotated
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
git checkout example1.7
mvn clean install
mvn spring-boot:run
```

App runs at → **http://localhost:8080**


---

## 📋 Annotation Reference

| Annotation | Evaluated | SpEL | Description |
|------------|-----------|------|-------------|
| `@PreAuthorize` | Before method | ✅ | Most powerful — supports complex SpEL expressions |
| `@PostAuthorize` | After method | ✅ | Can inspect the return value via `returnObject` |
| `@Secured` | Before method | ❌ | Simple — only accepts role strings |
| `@RolesAllowed` | Before method | ❌ | JSR-250 standard — same as `@Secured` |


---

## ⚖️ URL Security vs Method Security

| Aspect | URL Security | Method Security |
|--------|-------------|-----------------|
| Defined in | `SecurityConfig` (HTTP filter) | Service/component annotations |
| Granularity | By URL pattern | By method |
| SpEL support | Limited | ✅ Full SpEL |
| Ownership checks | ❌ Hard | ✅ Easy (`#param == authentication.name`) |
| Bypass risk | Controller only | ✅ Protects any caller (controller, service, etc.) |
| Best for | Coarse-grained access | Fine-grained, domain-level access |

> 💡 **Best practice:** Use URL security for broad access control + method security for fine-grained domain rules. Use both together.

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| `@EnableMethodSecurity` | Must be added to enable annotation-driven method security |
| `@PreAuthorize` | Runs SpEL expression before method — most flexible annotation |
| `@PostAuthorize` | Runs after method — use `returnObject` to inspect result |
| `#paramName` in SpEL | Binds to the method parameter name — enables ownership checks |
| `authentication` in SpEL | Built-in variable — access `.name`, `.authorities`, `.principal` |
| Method vs URL | URL = coarse-grained; Method = fine-grained — use both together |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.6">example1.6</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/jwt1">jwt1 ➡️</a>
</p>