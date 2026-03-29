# 🔐 Spring Security — Example 1.6: Custom Login Page & Logout

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![Branch](https://img.shields.io/badge/branch-example1.6-orange?logo=git)

> **Branch:** `example1.6` | **Prev:** [`example1.5`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.5) | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security)

This branch replaces the auto-generated Spring Security login page with a **custom HTML login form** and demonstrates full **logout configuration** — including custom success/failure redirect URLs and clearing security context.

---

## 📌 Table of Contents
- [What's New](#whats-new)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Security Configuration](#security-configuration)
- [Custom Login Page](#custom-login-page)
- [Logout Configuration](#logout-configuration)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)

---

## 🆕 What's New

| Feature | `example1.5` | `example1.6` |
|---------|-------------|-------------|
| Login page | Spring auto-generated | ✅ Custom HTML form |
| Login success URL | Default (`/`) | ✅ Custom redirect |
| Login failure URL | Default | ✅ Custom error message |
| Logout URL | Default `/logout` | ✅ Custom path + redirect |
| Remember Me | ❌ | ✅ Configured |
| Security context | Default clear | ✅ Explicitly cleared on logout |

---

## 🎯 What You'll Learn

- ✅ How to provide a **custom login page** with Spring Security
- ✅ How to configure **login success and failure URLs**
- ✅ How to customize the **logout** endpoint and post-logout redirect
- ✅ How to implement a **Remember Me** feature
- ✅ How to clear **cookies and session** on logout
- ✅ How to pass error/success messages from the security layer to the view

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Web MVC | 6.x | Web layer |
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
SpringSecurityEx6/
├── src/main/java/com/example/SpringSecurityEx6/
│   ├── SpringSecurityEx6Application.java
│   ├── config/
│   │   └── SecurityConfig.java        # Custom login/logout config
│   └── controller/
│       ├── LoginController.java       # Handles /login GET
│       └── HomeController.java
├── src/main/resources/
│   ├── templates/
│   │   ├── login.html                 # Custom login form
│   │   ├── home.html                  # Post-login home page
│   │   └── logout-success.html        # Post-logout page
│   └── application.properties
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security
git checkout example1.6
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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                          // Custom login page URL
                .loginProcessingUrl("/login")                 // Form action POST URL
                .defaultSuccessUrl("/home", true)             // Redirect after login
                .failureUrl("/login?error=true")              // Redirect on failure
                .usernameParameter("username")                // Form field names
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/do-logout")                      // Custom logout URL
                .logoutSuccessUrl("/login?logout=true")       // Redirect after logout
                .invalidateHttpSession(true)                  // Kill session
                .deleteCookies("JSESSIONID")                  // Clear session cookie
                .clearAuthentication(true)                    // Clear security context
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("uniqueRememberMeKey")
                .tokenValiditySeconds(86400)                  // 1 day
            );

        return http.build();
    }
}
```

---

## 📄 Custom Login Page

```html
<!-- login.html -->
<!DOCTYPE html>
<html>
<body>
  <h2>Login</h2>

  <!-- Error message -->
  <div th:if="${param.error}" style="color:red">
    Invalid username or password.
  </div>

  <!-- Logout message -->
  <div th:if="${param.logout}" style="color:green">
    You have been logged out.
  </div>

  <form method="post" action="/login">
    <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>
    <label>Username: <input type="text" name="username"/></label><br/>
    <label>Password: <input type="password" name="password"/></label><br/>
    <label><input type="checkbox" name="remember-me"/> Remember me</label><br/>
    <button type="submit">Login</button>
  </form>
</body>
</html>
```

---

## 🔓 Logout Configuration

| Config Option | Value | Effect |
|--------------|-------|--------|
| `logoutUrl` | `/do-logout` | URL that triggers logout (POST) |
| `logoutSuccessUrl` | `/login?logout=true` | Redirect after successful logout |
| `invalidateHttpSession` | `true` | Destroys the HTTP session |
| `deleteCookies` | `JSESSIONID` | Removes session cookie from browser |
| `clearAuthentication` | `true` | Clears `SecurityContext` |

---

## 📡 API Endpoints

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/login` | None | Custom login page |
| POST | `/login` | None | Login form submit |
| GET | `/home` | Required | Home page |
| POST | `/do-logout` | Required | Logout |
| GET | `/login?logout=true` | None | Post-logout landing |
| GET | `/login?error=true` | None | Login error page |

---

## 🧪 Running Tests

```bash
mvn test
```

```java
@Test
void unauthenticatedUserIsRedirectedToCustomLoginPage() throws Exception {
    mockMvc.perform(get("/home"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrlPattern("**/login"));
}

@Test
@WithMockUser
void logoutClearsSessionAndRedirects() throws Exception {
    mockMvc.perform(post("/do-logout").with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/login?logout=true"));
}
```

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| `loginPage("/login")` | Tell Spring Security to use YOUR page instead of the auto-generated one |
| `loginProcessingUrl` | The URL Spring intercepts for the form POST — keep it the same as `loginPage` |
| `defaultSuccessUrl(..., true)` | `true` = always redirect here, even if user came from another URL |
| `failureUrl` | Pass `?error` as a query param so the view can show an error message |
| `deleteCookies("JSESSIONID")` | Critical for full logout — removes the session cookie from the browser |
| Remember Me | Stores a persistent token so users stay logged in across browser restarts |

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.5">example1.5</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp; <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.7">example1.7 ➡️</a>
</p>