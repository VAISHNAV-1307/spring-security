# 🔐 Spring Security — Example 1.4: HTTP Basic Auth & Role-Based Authorization

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-example1.4-orange?logo=git)

> **Branch:** `example1.4` | **Prev:** [`example1.3`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.3) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

This branch explores **HTTP Basic Authentication** as an alternative to form-based login, and deepens understanding of **role-based authorization** using `hasRole()`, `hasAnyRole()`, and `hasAuthority()` with Spring Security 6.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Security Configuration](#security-configuration)
- [HTTP Basic Auth vs Form Login](#http-basic-auth-vs-form-login)
- [API Endpoints](#api-endpoints)
- [Testing with curl](#testing-with-curl)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `example1.3` | `example1.4` |
|---------|-------------|-------------|
| Login mechanism | Form login page | ✅ HTTP Basic Auth (header-based) |
| Auth header | ❌ | ✅ `Authorization: Basic <base64>` |
| REST-friendly | ❌ (browser redirect) | ✅ (returns 401, not redirect) |
| `hasAuthority()` | Not shown | ✅ Demonstrated |
| Fine-grained roles | Basic | ✅ Multi-role patterns |

---

## 🎯 What You'll Learn

- ✅ How to enable **HTTP Basic Authentication** in Spring Security 6
- ✅ The difference between **form login** and **HTTP Basic**
- ✅ How to use `hasRole()`, `hasAnyRole()`, and `hasAuthority()`
- ✅ How Spring Security handles **401 Unauthorized** vs **403 Forbidden**
- ✅ How to test secured REST APIs using **curl** and Postman
- ✅ Why HTTP Basic is suited for REST APIs and not browser apps

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
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
SpringSecurityEx4/
├── src/main/java/com/example/SpringSecurityEx4/
│   ├── SpringSecurityEx4Application.java
│   ├── config/
│   │   └── SecurityConfig.java         # HTTP Basic + role authorization
│   ├── controller/
│   │   ├── PublicController.java
│   │   ├── UserController.java
│   │   └── AdminController.java
│   └── model/                          # Lombok DTOs
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
git checkout example1.4
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
            .csrf(csrf -> csrf.disable())              // Disable CSRF for REST APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());     // Enable HTTP Basic Auth
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
            .username("user").password(encoder.encode("password")).roles("USER").build();
        UserDetails admin = User.builder()
            .username("admin").password(encoder.encode("admin123")).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## ⚖️ HTTP Basic Auth vs Form Login

| Aspect | Form Login | HTTP Basic Auth |
|--------|-----------|-----------------|
| Client type | Browser | REST client / API |
| Login UI | HTML login page | None (header-based) |
| Credential delivery | Form POST body | `Authorization: Basic <base64>` |
| Unauthenticated response | 302 redirect to `/login` | **401 Unauthorized** |
| Session | Stateful (cookie) | Stateless (per-request) |
| Use case | Web apps | REST APIs, Postman, curl |

---

## 📡 API Endpoints

| Method | URL | Role | Description |
|--------|-----|------|-------------|
| GET | `/public/hello` | All | Public endpoint |
| GET | `/user/info` | USER, ADMIN | User info |
| GET | `/admin/users` | ADMIN | List all users |
| GET | `/admin/stats` | ADMIN | System stats |

---

## 🧪 Testing with curl

```bash
# Access public endpoint
curl http://localhost:8080/public/hello

# Access user endpoint as USER
curl -u user:password http://localhost:8080/user/info

# Access admin endpoint as ADMIN
curl -u admin:admin123 http://localhost:8080/admin/users

# Access admin as USER → 403 Forbidden
curl -u user:password http://localhost:8080/admin/users

# No credentials → 401 Unauthorized
curl http://localhost:8080/user/info
```

---

## 🧪 Running Tests

```bash
mvn test
```

```java
@Test
void noCredentialsShouldReturn401() throws Exception {
    mockMvc.perform(get("/user/info"))
           .andExpect(status().isUnauthorized());
}

@Test
@WithMockUser(roles = "USER")
void userRoleCannotAccessAdmin() throws Exception {
    mockMvc.perform(get("/admin/users"))
           .andExpect(status().isForbidden());
}
```

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| `.httpBasic()` | Enables HTTP Basic Auth — credentials sent as Base64 header each request |
| `csrf.disable()` | Required for stateless REST APIs — CSRF tokens don't apply |
| **401 vs 403** | 401 = not authenticated (who are you?), 403 = not authorized (you can't do that) |
| `hasRole("ADMIN")` | Checks for `ROLE_ADMIN` authority (Spring adds `ROLE_` prefix automatically) |
| `hasAuthority("ROLE_ADMIN")` | Same check but you must provide the full authority string |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.3">example1.3</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.5">example1.5 ➡️</a>
</p>