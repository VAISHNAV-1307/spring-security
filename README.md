# 🔐 Spring Security Learning Project

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?logo=springsecurity&logoColor=white)
![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven&logoColor=white)

> **Comprehensive learning project** covering Spring Security concepts from basics to advanced JWT implementations

This repository contains a **complete learning journey** through Spring Security, with each branch demonstrating specific security concepts and implementations. Perfect for developers who want to master Spring Security step by step.

---

## 📚 Branch Index

### 🟢 Basic Examples (1.1-1.2)

| Branch | Topic | Description |
|--------|-------|-------------|
| [`example1.1`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.1) | **Auto-Configuration Basics** | Simplest Spring Security setup - just add the dependency |
| [`example1.2`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.2) | **JPA User Details Service** | Custom user authentication with database integration |

### 🟡 Intermediate Examples (1.3-1.7)

| Branch | Topic | Description |
|--------|-------|-------------|
| [`example1.3`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.3) | **Custom Authentication** | Building custom authentication filters and providers |
| [`example1.4`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.4) | **API Key Authentication** | Implementing API key-based security |
| [`example1.5`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.5) | **Advanced Security Config** | Complex security configurations |
| [`example1.6`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.6) | **Method Security** | Securing individual methods with annotations |
| [`example1.7`](https://github.com/VAISHNAV-1307/spring-security/tree/example1.7) | **OAuth2 Integration** | Social login and OAuth2 implementation |

### 🔴 JWT Examples

| Branch | Topic | Description |
|--------|-------|-------------|
| [`jwt1`](https://github.com/VAISHNAV-1307/spring-security/tree/jwt1) | **JWT Basics** | Simple JWT token generation and validation |
| [`jwt2`](https://github.com/VAISHNAV-1307/spring-security/tree/jwt2) | **Complete JWT Auth** | Full JWT authentication system with user management |

---

## 🚀 Quick Start

### Prerequisites

```bash
java -version   # Java 25+
mvn -version    # Maven 3.8+
git --version
```

### Clone and Explore

```bash
git clone https://github.com/VAISHNAV-1307/spring-security.git
cd spring-security

# List all branches
git branch -a

# Switch to a specific example
git checkout example1.1  # Start with basics
```

### Run Any Example

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Test with default credentials (for basic examples)
# Username: user
# Password: Check console output for auto-generated password
```

---

## 📖 Learning Path

### 🎯 Recommended Order

1. **Start with Basics**
   ```bash
   git checkout example1.1  # Auto-configuration
   git checkout example1.2  # JPA authentication
   ```

2. **Move to Custom Security**
   ```bash
   git checkout example1.3  # Custom authentication
   git checkout example1.4  # API key security
   ```

3. **Advanced Concepts**
   ```bash
   git checkout example1.5  # Advanced config
   git checkout example1.6  # Method security
   git checkout example1.7  # OAuth2
   ```

4. **Master JWT**
   ```bash
   git checkout jwt1        # JWT basics
   git checkout jwt2        # Complete JWT system
   ```

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 25 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Security | 6.x | Security layer |
| Spring Web MVC | 6.x | REST / web endpoints |
| Spring Data JPA | 6.x | Database access |
| Maven | 3.8+ | Build tool |
| JUnit 5 | Latest | Testing framework |
| JWT (jjwt) | Latest | Token handling |

---

## 📁 Common Project Structure

```
spring-security/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── controllers/          # REST endpoints
│   │   │   ├── config/              # Security configurations
│   │   │   ├── services/            # Business logic
│   │   │   ├── entities/            # JPA entities
│   │   │   ├── repositories/        # Data access
│   │   │   └── security/            # Custom security components
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/              # Static resources (if any)
│   └── test/                        # Test classes
├── pom.xml                          # Maven configuration
└── README.md                        # Branch-specific documentation
```

---

## 🔑 Key Concepts Covered

### Authentication vs Authorization
- **Authentication**: Who are you? (Login, credentials)
- **Authorization**: What can you do? (Roles, permissions)

### Security Filter Chain
Every request passes through Spring Security's filter chain:
- SecurityContextPersistenceFilter
- UsernamePasswordAuthenticationFilter
- BasicAuthenticationFilter
- AuthorizationFilter
- And many more...

### Password Encoding
- **NoOpPasswordEncoder**: Plain text (example only - not for production!)
- **BCryptPasswordEncoder**: Industry standard for production

### JWT (JSON Web Tokens)
- **Stateless authentication**: No server-side session
- **Token-based**: Client stores the token
- **Claims**: User information encoded in token

---

## 🧪 Testing

### Basic Security Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedRequestShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/secure-endpoint"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void authenticatedRequestShouldReturn200() throws Exception {
        mockMvc.perform(get("/secure-endpoint"))
               .andExpect(status().isOk());
    }
}
```

### JWT Tests

```java
@Test
void validJwtShouldAllowAccess() throws Exception {
    String token = jwtService.generateToken(testUser);
    
    mockMvc.perform(get("/api/secure")
           .header("Authorization", "Bearer " + token))
           .andExpect(status().isOk());
}
```

---

## 📋 Branch Details

### example1.1 - Auto-Configuration Basics
- **Key Feature**: Zero-config security
- **What you learn**: Spring Boot auto-configuration magic
- **Default behavior**: All endpoints secured, auto-generated password

### example1.2 - JPA User Details Service  
- **Key Feature**: Database-backed authentication
- **What you learn**: Custom UserDetailsService implementation
- **Components**: User entity, UserRepository, SecurityUser wrapper

### example1.3 - Custom Authentication
- **Key Feature**: Custom authentication flow
- **What you learn**: Building custom filters, providers, and managers
- **Components**: CustomAuthentication, CustomAuthenticationFilter, CustomAuthenticationProvider

### example1.4 - API Key Authentication
- **Key Feature**: API key-based security
- **What you learn**: Non-standard authentication methods
- **Components**: ApiKeyFilter, ApiKeyProvider, custom authentication

### example1.5 - Advanced Security Config
- **Key Feature**: Complex security rules
- **What you learn**: Advanced SecurityFilterChain configuration
- **Components**: Multiple security configurations, custom endpoints

### example1.6 - Method Security
- **Key Feature**: Method-level security
- **What you learn**: @PreAuthorize, @PostAuthorize, @Secured annotations
- **Components**: @EnableMethodSecurity, custom security expressions

### example1.7 - OAuth2 Integration
- **Key Feature**: Social login integration
- **What you learn**: OAuth2 client configuration, social providers
- **Components**: OAuth2 client, custom user info handling

### jwt1 - JWT Basics
- **Key Feature**: Simple JWT implementation
- **What you learn**: JWT generation and validation basics
- **Components**: JwtService, token creation, basic validation

### jwt2 - Complete JWT Auth
- **Key Feature**: Production-ready JWT system
- **What you learn**: Full JWT authentication with user management
- **Components**: AuthenticationService, JwtAuthenticationFilter, user registration/login

---

## 🔧 Common Configuration Patterns

### Basic Security Config
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/custom-login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }
}
```

### JWT Security Config
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

---

## 🎯 Best Practices

### ✅ Do's
- Use BCrypt for password encoding in production
- Implement proper input validation
- Use HTTPS in production
- Follow principle of least privilege
- Write security tests

### ❌ Don'ts  
- Never use NoOpPasswordEncoder in production
- Don't hardcode credentials
- Don't disable CSRF without good reason
- Don't store sensitive data in JWT tokens
- Don't ignore security headers

---

<p align="center">
  Made with ❤️ for learning Spring Security by <a href="https://github.com/VAISHNAV-1307">VAISHNAV-1307</a>
</p>
