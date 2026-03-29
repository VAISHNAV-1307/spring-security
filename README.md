# 🔐 Spring Security — Example 1.1: Auto-Configuration Basics

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven&logoColor=white)
![Branch](https://img.shields.io/badge/branch-example1.1-orange?logo=git)

> **Branch:** `example1.1` | **Part of:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

This branch demonstrates the **simplest way to add Spring Security** to a Spring Boot application — just by adding the dependency. No custom configuration is needed to get a fully secured application up and running.

---

## 📌 Table of Contents

- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [How It Works](#how-it-works)
- [Default Security Behaviour](#default-security-behaviour)
- [Spring Security Filter Chain](#spring-security-filter-chain)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)
- [Next Steps](#next-steps)

---

## 🎯 What You'll Learn

By studying this branch, you will understand:

- ✅ How Spring Boot **auto-configures** Spring Security with zero setup
- ✅ What the **Spring Security Filter Chain** is and how it intercepts requests
- ✅ How the **default login page** and auto-generated credentials work
- ✅ How to write **security-aware integration tests** using `spring-security-test`
- ✅ The difference between **Authentication** (who are you?) and **Authorization** (what can you do?)

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Security | 6.x (managed by Boot) | Security layer |
| Spring Web MVC | 6.x | REST / web endpoints |
| Maven | 3.8+ | Build tool |
| JUnit 5 + Spring Test | Latest | Unit & integration testing |
| spring-security-test | Latest | Security-specific test utilities |

---

## 📦 Project Dependencies

From `pom.xml` (`com.example:SpringSecurityEx1:0.0.1-SNAPSHOT`):

```xml
<!-- Adds the entire Spring Security framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Enables Spring MVC + embedded Tomcat web server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- JUnit 5, Mockito, AssertJ for testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- @WithMockUser, MockMvc security support -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 📁 Project Structure

```
SpringSecurityEx1/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/SpringSecurityEx1/
│   │   │       └── SpringSecurityEx1Application.java   # @SpringBootApplication entry point
│   │   └── resources/
│   │       └── application.properties                  # App configuration
│   └── test/
│       └── java/
│           └── com/example/SpringSecurityEx1/
│               └── SpringSecurityEx1ApplicationTests.java
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Make sure the following are installed:

```bash
java -version   # Must be Java 25+
mvn -version    # Must be Maven 3.8+
git --version
```

### Clone & Switch Branch

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security
git checkout example1.1
```

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The application starts at **http://localhost:8080**

---

## ⚙️ How It Works

The magic is entirely in the dependency. The moment `spring-boot-starter-security` is on the classpath, Spring Boot's **auto-configuration** kicks in and:

1. **Locks down all endpoints** — every URL requires authentication
2. **Creates a default user** with username `user`
3. **Generates a random password** printed to the console at startup
4. **Serves a built-in login page** at `/login`
5. **Enables CSRF protection** by default
6. **Creates a logout endpoint** at `/logout`

No `@Configuration` class. No `SecurityFilterChain` bean. Just the dependency.

---

## 🔑 Default Security Behaviour

| Feature | Default |
|---------|---------|
| Login page URL | `/login` (auto-generated) |
| Logout URL | `/logout` |
| Default username | `user` |
| Default password | Random — check console output |
| All routes | 🔒 Protected (require login) |
| CSRF protection | ✅ Enabled |
| Session management | Cookie-based (stateful) |
| Password encoding | BCrypt |

**Find the auto-generated password in your startup logs:**

```
Using generated security password: a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

> ⚠️ This password changes every time the application restarts.

---

## 🔄 Spring Security Filter Chain

Every HTTP request passes through this pipeline before reaching your controllers:

```
  Browser / HTTP Client
          │
          ▼
┌─────────────────────────────────────────────┐
│          Spring Security Filter Chain        │
│                                             │
│  1. SecurityContextPersistenceFilter        │
│     └─ Loads existing security context      │
│                                             │
│  2. UsernamePasswordAuthenticationFilter    │
│     └─ Handles POST /login form submission  │
│                                             │
│  3. DefaultLoginPageGeneratingFilter        │
│     └─ Serves the built-in /login page      │
│                                             │
│  4. BasicAuthenticationFilter               │
│     └─ Handles HTTP Basic Auth header       │
│                                             │
│  5. ExceptionTranslationFilter              │
│     └─ Converts exceptions → 401/403        │
│                                             │
│  6. AuthorizationFilter                     │
│     └─ Checks roles/permissions             │
└─────────────────────────────────────────────┘
          │
          ▼  (if authenticated & authorized)
   DispatcherServlet → Your Controller
```

**What happens on an unauthenticated request:**

```
GET /any-url
    → AuthorizationFilter detects: no authentication
    → ExceptionTranslationFilter catches AccessDeniedException
    → Redirects to /login
    → User logs in with credentials
    → Redirected back to original URL (/any-url)
```

---

## 🧪 Running Tests

```bash
mvn test
```

With `spring-security-test`, you can write tests like:

```java
@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityEx1ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedRequestShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void authenticatedRequestShouldReturn200() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanAccessAllRoutes() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }
}
```

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| **Auto-configuration** | Adding `spring-boot-starter-security` is enough to secure the whole app |
| **Filter Chain** | Security is implemented as a chain of servlet filters, not inside controllers |
| **Default credentials** | `user` / (random password from logs) — change this in production! |
| **CSRF** | Enabled by default — forms need a CSRF token |
| **Session** | Spring Security manages an HTTP session after login |
| **Testing** | Use `@WithMockUser` to simulate authenticated users in tests |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">Back to Main</a> &nbsp;|&nbsp;
  Made with ❤️ by <a href="https://github.com/VAISHNAV-1307">VAISHNAV-1307</a>
</p>