# 🔐 Spring Security — Example 1.2: Database Authentication with JPA + MySQL

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql&logoColor=white)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.x-brightgreen?logo=spring&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red?logo=lombok&logoColor=white)
![Branch](https://img.shields.io/badge/branch-example1.2-orange?logo=git)

> **Branch:** `example1.2` | **Series:** [Spring Security Learning Series](https://github.com/VAISHNAV-1307/spring-security) | **Previous:** [`example1.1`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.1)

This branch upgrades from `example1.1`'s in-memory user store to **real database-backed authentication** using **Spring Data JPA**, **MySQL**, and **Lombok**. Users are now loaded from a MySQL database, making the security setup production-ready.

---

## 📌 Table of Contents

- [What's New in This Branch](#whats-new-in-this-branch)
- [What You'll Learn](#what-youll-learn)
- [Tech Stack](#tech-stack)
- [Project Dependencies](#project-dependencies)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Getting Started](#getting-started)
- [How It Works](#how-it-works)
- [Key Components](#key-components)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Key Takeaways](#key-takeaways)
- [Next Steps](#next-steps)

---

## 🆕 What's New in This Branch

| Feature | `example1.1` | `example1.2` |
|---------|-------------|-------------|
| User storage | In-memory (auto-config) | MySQL database via JPA |
| User persistence | ❌ Lost on restart | ✅ Persisted in DB |
| ORM | ❌ None | ✅ Spring Data JPA (Hibernate) |
| Database | ❌ None | ✅ MySQL 8.x |
| Boilerplate reduction | ❌ Manual getters/setters | ✅ Lombok annotations |
| `UserDetailsService` | Auto-configured | Custom implementation |
| Password encoder | Auto-configured | Explicit `BCryptPasswordEncoder` |

---

## 🎯 What You'll Learn

- ✅ How to implement a custom **`UserDetailsService`** that loads users from a database
- ✅ How to create a **`User` entity** mapped to a MySQL table with JPA
- ✅ How to use **Spring Data JPA repositories** for user lookup
- ✅ How to configure **`BCryptPasswordEncoder`** explicitly
- ✅ How to wire everything together in a custom **`SecurityConfig`**
- ✅ How to use **Lombok** to eliminate boilerplate in entity classes
- ✅ How to configure `application.properties` for MySQL + JPA

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Security | 6.x | Authentication & Authorization |
| Spring Web MVC | 6.x | REST / web endpoints |
| Spring Data JPA | 3.x | ORM / database access |
| Hibernate | 6.x (via JPA) | JPA implementation |
| MySQL | 8.x | Relational database for user storage |
| Lombok | Latest | Reduces boilerplate (`@Data`, `@Entity`, etc.) |
| Maven | 3.8+ | Build tool |

---

## 📦 Project Dependencies

From `pom.xml` (`com.example:SpringSecurityEx2:0.0.1-SNAPSHOT`):

```xml
<!-- JPA / Hibernate ORM -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring MVC + Embedded Tomcat -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- MySQL JDBC Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok - eliminates boilerplate code -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Test dependencies -->
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
SpringSecurityEx2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/SpringSecurityEx2/
│   │   │       ├── SpringSecurityEx2Application.java    # Entry point
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java              # Custom security config
│   │   │       ├── controller/
│   │   │       │   └── HomeController.java              # Web endpoints
│   │   │       ├── model/
│   │   │       │   └── User.java                        # JPA Entity (@Data, @Entity)
│   │   │       ├── repository/
│   │   │       │   └── UserRepository.java              # Spring Data JPA repo
│   │   │       └── service/
│   │   │           └── CustomUserDetailsService.java    # UserDetailsService impl
│   │   └── resources/
│   │       └── application.properties                   # DB + JPA config
│   └── test/
│       └── java/
│           └── com/example/SpringSecurityEx2/
│               └── SpringSecurityEx2ApplicationTests.java
├── pom.xml
└── README.md
```

---

## 🗄️ Database Setup

### 1. Create MySQL Database

```sql
CREATE DATABASE spring_security_db;
USE spring_security_db;
```

### 2. Create Users Table

```sql
CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);
```

### 3. Insert Sample Users (BCrypt encoded passwords)

```sql
-- Password: "password" (BCrypt encoded)
INSERT INTO users (username, password, role) VALUES
('user',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_USER'),
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN');
```

> ⚠️ **Never store plain-text passwords.** Always use BCrypt or another strong encoder.

---

## ⚙️ Configuration

Update `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# MySQL DataSource
spring.datasource.url=jdbc:mysql://localhost:3306/spring_security_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 25+ |
| Maven | 3.8+ |
| MySQL Server | 8.x |
| Git | Any |

### Clone & Switch Branch

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security
git checkout example1.2
```

### Configure Database

Edit `src/main/resources/application.properties` with your MySQL credentials (see [Configuration](#configuration) above), then create the database and insert sample users (see [Database Setup](#database-setup)).

### Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

App starts at → **http://localhost:8080**

---

## ⚙️ How It Works

The authentication flow now goes through the database:

```
  Browser Login Request (POST /login)
           │
           ▼
  UsernamePasswordAuthenticationFilter
           │
           ▼
  AuthenticationManager
           │
           ▼
  DaoAuthenticationProvider
     ├── CustomUserDetailsService.loadUserByUsername(username)
     │         │
     │         ▼
     │   UserRepository.findByUsername(username)  ← MySQL query
     │         │
     │         ▼
     │   Returns User entity → UserDetails object
     │
     └── BCryptPasswordEncoder.matches(raw, encoded)
               │
        ┌──────┴──────┐
      PASS           FAIL
        │               │
        ▼               ▼
   Authenticated    BadCredentialsException
   → redirect to    → redirect to /login?error
     original URL
```

---

## 🔑 Key Components

### User Entity (`User.java`)
```java
@Entity
@Table(name = "users")
@Data                    // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Lombok: no-args constructor (required by JPA)
@AllArgsConstructor      // Lombok: all-args constructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // BCrypt encoded

    private String role;       // e.g. "ROLE_USER", "ROLE_ADMIN"
}
```

### User Repository (`UserRepository.java`)
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

### Custom UserDetailsService (`CustomUserDetailsService.java`)
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().replace("ROLE_", ""))
            .build();
    }
}
```

### Security Configuration (`SecurityConfig.java`)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
```

---

## 📡 API Endpoints

| Method | URL | Access | Description |
|--------|-----|--------|-------------|
| GET | `/` | Authenticated | Home page |
| GET | `/public/**` | Public | No login required |
| GET | `/admin/**` | `ROLE_ADMIN` only | Admin area |
| POST | `/login` | Public | Login form submission |
| POST | `/logout` | Authenticated | Logout |

---

## 🧪 Running Tests

```bash
mvn test
```

Example test using `@WithMockUser`:

```java
@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityEx2ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void userCanAccessHome() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void userCannotAccessAdmin() throws Exception {
        mockMvc.perform(get("/admin/panel"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanAccessAdminPanel() throws Exception {
        mockMvc.perform(get("/admin/panel"))
               .andExpect(status().isOk());
    }
}
```

---

## 💡 Key Takeaways

| Concept | Summary |
|---------|---------|
| **`UserDetailsService`** | The bridge between your DB and Spring Security — implement this to load users from any source |
| **`DaoAuthenticationProvider`** | Spring Security's built-in provider that uses `UserDetailsService` + `PasswordEncoder` |
| **`BCryptPasswordEncoder`** | Always encode passwords with BCrypt before storing — never store plain text |
| **Lombok `@Data`** | Eliminates boilerplate getters/setters/equals/hashCode on entity classes |
| **JPA `@Entity`** | Maps a Java class to a database table — fields become columns |
| **`UserRepository`** | Extend `JpaRepository` and add `findByUsername()` — Spring Data generates the SQL automatically |

---

## ➡️ Next Steps

Move to the next branch to explore more advanced Spring Security features:

```bash
# Check the main branch for the full branch index
git checkout main
```

Or browse the [full branch index](https://github.com/VAISHNAV-1307/spring-security/tree/main#-branch-index).

---

<p align="center">
  ⬅️ <a href="https://github.com/VAISHNAV-1307/spring-security/tree/example1.1">example1.1</a>
  &nbsp;|&nbsp;
  <a href="https://github.com/VAISHNAV-1307/spring-security/tree/main">🏠 Main</a>
  &nbsp;|&nbsp;
  Made with ❤️ by <a href="https://github.com/VAISHNAV-1307">VAISHNAV-1307</a>
</p>