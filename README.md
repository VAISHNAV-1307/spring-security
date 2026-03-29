# 🔐 Spring Security — Example 1.5: CSRF Protection & Session Management

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-example1.5-orange?logo=git)

> **Branch:** `example1.5` | **Prev:** [`example1.4`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.4) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

This branch dives into two important security mechanisms: **CSRF (Cross-Site Request Forgery) Protection** and **Session Management**. It also introduces a downshift to **Java 17** — the current LTS release widely used in production Spring Boot applications.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Security Configuration](#security-configuration)
- [CSRF Protection Explained](#csrf-protection-explained)
- [Session Management](#session-management)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `example1.4` | `example1.5` |
|---------|-------------|-------------|
| Java version | 25 | ✅ **17** (LTS) |
| CSRF config | Disabled for REST | ✅ Enabled & explained |
| Session policy | Default | ✅ Explicit `SessionCreationPolicy` |
| Concurrent sessions | Not shown | ✅ Configured |
| Session fixation | Default | ✅ Explicitly secured |

---

## 🎯 What You'll Learn

- ✅ What **CSRF attacks** are and how Spring Security prevents them
- ✅ When to **enable vs disable** CSRF protection
- ✅ How to configure **session creation policies** (`STATELESS`, `IF_REQUIRED`, `ALWAYS`)
- ✅ How to protect against **session fixation attacks**
- ✅ How to limit **concurrent sessions** per user
- ✅ Why **Java 17** is the standard for production Spring Boot apps

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | **17** (LTS) | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Web MVC | 6.x | REST / web endpoints |
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
SpringSecurityEx5/
├── src/main/java/com/example/SpringSecurityEx5/
│   ├── SpringSecurityEx5Application.java
│   ├── config/
│   │   └── SecurityConfig.java        # CSRF + session management config
│   ├── controller/
│   │   ├── HomeController.java
│   │   └── ApiController.java
│   └── model/
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
git checkout example1.5
mvn clean install
mvn spring-boot:run
```

App runs at → **http://localhost:8080**

---

## 🔒 Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF: enabled by default for browser apps, disable for stateless REST
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")    // Disable CSRF for REST API paths
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            // Session Management
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)                          // Only 1 session per user
                .maxSessionsPreventsLogin(false)             // New login kicks out old session
            )
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()          // Protect against session fixation
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 🛡️ CSRF Protection Explained

**What is CSRF?**
A CSRF attack tricks a logged-in user's browser into making an unwanted request to your server (e.g., transferring money, deleting data) by exploiting their existing session cookie.

```
Attacker's site → user's browser → POST /transfer?amount=1000 → Your server
                               (Using victim's session cookie automatically!)
```

**Spring Security's defence:** Every state-changing request (POST/PUT/DELETE) must include a CSRF token that only your own frontend knows.

| Scenario | CSRF Setting | Why |
|----------|-------------|-----|
| Browser-based web app | ✅ Enable (default) | Forms include CSRF token |
| REST API with JWT | ❌ Disable | Stateless — no cookies |
| REST API with sessions | ✅ Enable | Cookies are used |

---

## 🔄 Session Management

| Policy | Constant | Behaviour |
|--------|----------|-----------|
| Create if needed | `IF_REQUIRED` (default) | Session created when required |
| Always create | `ALWAYS` | Session always created on login |
| Never create | `NEVER` | Spring won't create sessions (uses existing) |
| Fully stateless | `STATELESS` | No session at all — for JWT/REST APIs |

---

## 📡 API Endpoints

| Method | URL | CSRF | Auth | Description |
|--------|-----|------|------|-------------|
| GET | `/public/hello` | N/A | None | Public |
| GET | `/home` | Required | Required | Home page |
| POST | `/api/data` | ❌ Skipped | Required | REST API endpoint |
| POST | `/form/submit` | ✅ Required | Required | Form submission |

---

## 🧪 Running Tests

```bash
mvn test
```

```java
@Test
@WithMockUser
void postWithoutCsrfToFormEndpointShouldFail() throws Exception {
    mockMvc.perform(post("/form/submit"))
           .andExpect(status().isForbidden());
}

@Test
@WithMockUser
void postWithCsrfShouldSucceed() throws Exception {
    mockMvc.perform(post("/form/submit").with(csrf()))
           .andExpect(status().isOk());
}

@Test
@WithMockUser
void postToApiWithoutCsrfShouldSucceed() throws Exception {
    mockMvc.perform(post("/api/data"))
           .andExpect(status().isOk());
}
```

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| **CSRF** | Protects browser-based apps from forged requests using hidden tokens |
| **`STATELESS`** | No sessions at all — the right choice for JWT-based REST APIs |
| **Session fixation** | Attack where attacker reuses a pre-login session ID — mitigated by migrating session on login |
| **Max sessions** | Limits concurrent logins per user — useful for preventing account sharing |
| **Java 17** | LTS release — preferred for production; Spring Boot 3.x requires Java 17+ |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.4">example1.4</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.6">example1.6 ➡️</a>
</p>