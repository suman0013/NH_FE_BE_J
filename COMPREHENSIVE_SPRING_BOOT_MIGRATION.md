# Comprehensive Spring Boot Migration Plan

## Overview

Complete migration plan for Namhatta Management System from Node.js/Express to Spring Boot, based on comprehensive analysis of all backend components. This plan ensures 100% API compatibility and functionality preservation.

## Technology Stack

### Target Spring Boot Configuration
- **Java Version**: Java 17 (LTS)
- **Spring Boot**: 3.5.0
- **Database**: Spring Data JPA with PostgreSQL
- **Security**: Spring Security 6.x with JWT
- **Documentation**: Swagger/OpenAPI 3.0.3 with springdoc-openapi
- **Code Generation**: Lombok (@Builder, @Getter, @Setter, @Data)
- **Validation**: Custom generic validation utilities (NO javax.validation)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## Complete Node.js Backend Analysis

### 1. Authentication System Components
**Files Analyzed**: `server/auth/jwt.ts`, `server/auth/middleware.ts`, `server/auth/session.ts`, `server/auth/password.ts`, `server/auth/routes.ts`

**Key Features:**
- JWT token generation with 1-hour expiration
- HTTP-only cookie authentication
- Single login enforcement via session tokens
- Rate limiting (5 attempts per 15 minutes)
- Token blacklisting for secure logout
- Role-based authorization (ADMIN, OFFICE, DISTRICT_SUPERVISOR)
- District-based data filtering for supervisors
- BCrypt password hashing
- Development bypass functionality

### 2. Database Schema Analysis
**File**: `shared/schema.ts` - 12 PostgreSQL tables:

1. **devotees** - Core devotee information with normalized addresses
2. **namhattas** - Spiritual centers with approval workflow
3. **devotionalStatuses** - 7-level spiritual hierarchy
4. **shraddhakutirs** - Regional administrative units
5. **statusHistory** - Devotee status change tracking
6. **namhattaUpdates** - Activity updates with JSONB arrays
7. **leaders** - Hierarchical leadership structure
8. **addresses** - Normalized address data (country→village)
9. **devoteeAddresses** - Junction table with landmarks
10. **namhattaAddresses** - Junction table with landmarks
11. **users** - Authentication users with roles
12. **userDistricts** - Many-to-many user-district mapping
13. **userSessions** - Single login session enforcement
14. **jwtBlacklist** - Token invalidation for logout

### 3. API Endpoints Analysis
**Files**: `server/routes.ts`, `server/auth/routes.ts` - **37 REST endpoints**:

**Authentication Endpoints (5)**:
- POST `/api/auth/login` - User authentication
- POST `/api/auth/logout` - Secure logout with token blacklisting
- GET `/api/auth/verify` - Token validation
- GET `/api/auth/user-districts` - Get user's assigned districts
- GET `/api/dev/users` - Development user listing

**Geographic Endpoints (7)**:
- GET `/api/countries` - Country list
- GET `/api/states` - States by country
- GET `/api/districts` - Districts by state
- GET `/api/sub-districts` - Sub-districts by district/pincode
- GET `/api/villages` - Villages by sub-district/pincode
- GET `/api/pincodes/search` - Paginated pincode search
- GET `/api/address-by-pincode` - Address lookup by pincode

**Map Visualization Endpoints (5)**:
- GET `/api/map/countries` - Namhatta counts by country
- GET `/api/map/states` - Namhatta counts by state
- GET `/api/map/districts` - Namhatta counts by district
- GET `/api/map/sub-districts` - Namhatta counts by sub-district
- GET `/api/map/villages` - Namhatta counts by village

**Dashboard Endpoints (2)**:
- GET `/api/dashboard` - Summary statistics
- GET `/api/status-distribution` - Devotional status analytics

**Devotee Management Endpoints (6)**:
- GET `/api/devotees` - Paginated list with filtering
- GET `/api/devotees/:id` - Individual devotee details
- POST `/api/devotees` - Create new devotee
- POST `/api/devotees/:namhattaId` - Create devotee for specific namhatta
- PUT `/api/devotees/:id` - Update devotee
- POST `/api/devotees/:id/upgrade-status` - Status upgrade with history
- GET `/api/devotees/:id/status-history` - Status change history
- GET `/api/devotees/:id/addresses` - Devotee address information

**Namhatta Management Endpoints (10)**:
- GET `/api/namhattas` - Paginated list with filtering
- GET `/api/namhattas/:id` - Individual namhatta details
- POST `/api/namhattas` - Create new namhatta
- PUT `/api/namhattas/:id` - Update namhatta
- GET `/api/namhattas/pending` - Pending approval list
- GET `/api/namhattas/check-code/:code` - Code uniqueness check
- POST `/api/namhattas/:id/approve` - Approve namhatta
- POST `/api/namhattas/:id/reject` - Reject namhatta
- GET `/api/namhattas/:id/devotees` - Namhatta's devotees
- GET `/api/namhattas/:id/updates` - Namhatta activity updates
- GET `/api/namhattas/:id/devotee-status-count` - Status distribution
- GET `/api/namhattas/:id/status-history` - Status change history

**Support Endpoints (4)**:
- GET `/api/statuses` - Devotional status list
- POST `/api/statuses` - Create new status
- POST `/api/statuses/:id/rename` - Rename status
- GET `/api/shraddhakutirs` - Shraddhakutir list
- POST `/api/shraddhakutirs` - Create shraddhakutir
- POST `/api/updates` - Create namhatta update
- GET `/api/updates/all` - All updates (optimized)

**System Endpoints (2)**:
- GET `/api/health` - Health check
- GET `/api/about` - Application information

### 4. Storage Layer Analysis
**Files**: `server/storage-db.ts`, `server/storage-auth.ts`, `server/storage-fresh.ts`

**Key Operations:**
- Complex filtering with district-based access control
- Address normalization and reuse
- Pagination with sorting (name, createdAt, updatedAt)
- Geographic hierarchy queries
- Statistical aggregations for dashboard
- JSONB field handling for courses and locations

## Migration Plan

### Phase 1: Project Setup & Infrastructure

#### Task 1.1: Create Spring Boot Project
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 1 hour
**Prerequisites**: None

**Sub-tasks:**
- [ ] Create Maven project with Spring Boot 3.5.0
- [ ] Configure Java 17 in pom.xml
- [ ] Add core dependencies: spring-boot-starter-web, spring-boot-starter-data-jpa
- [ ] Add security dependencies: spring-boot-starter-security
- [ ] Add documentation: springdoc-openapi-starter-webmvc-ui
- [ ] Add utilities: lombok, jackson-databind
- [ ] Set up project structure: entity, dto, repository, service, controller packages

**Dependencies (pom.xml)**:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <spring-boot.version>3.5.0</spring-boot.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.3</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.3</version>
    </dependency>
</dependencies>
```

#### Task 1.2: Database Configuration
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 30 minutes
**Prerequisites**: Task 1.1 completed

**Sub-tasks:**
- [ ] Configure PostgreSQL connection
- [ ] Set up application.yml with database settings
- [ ] Configure JPA properties
- [ ] Test database connectivity

**Configuration (application.yml)**:
```yaml
spring:
  application:
    name: namhatta-management-system
  datasource:
    url: ${DATABASE_URL:postgresql://neondb_owner:npg_5MIwCD4YhSdP@ep-calm-silence-a15zko7l-pooler.ap-southeast-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: ${GITHUB_CLIENT_ID:}
            client-secret: ${GITHUB_CLIENT_SECRET:}

server:
  port: 8080

logging:
  level:
    com.namhatta: DEBUG
    org.springframework.security: DEBUG

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    
jwt:
  secret: ${JWT_SECRET:namhatta-jwt-secret-key-2025-secure-random-string-for-development}
  expiration: 3600000
```

### Phase 2: JPA Entities & Database Mapping

#### Task 2.1: Create Core JPA Entities
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 3 hours
**Prerequisites**: Task 1.2 completed

**Sub-tasks:**
- [ ] Create all 14 JPA entity classes
- [ ] Configure relationships and foreign keys
- [ ] Add proper annotations (@Entity, @Table, @Column)
- [ ] Use Lombok for getters/setters
- [ ] Test entity mappings

**Key Entities:**
```java
@Entity
@Table(name = "devotees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Devotee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "legal_name", nullable = false)
    private String legalName;
    
    private String name;
    private String dob;
    private String email;
    private String phone;
    
    @Column(name = "father_name")
    private String fatherName;
    
    @Column(name = "mother_name")
    private String motherName;
    
    @Column(name = "husband_name")
    private String husbandName;
    
    private String gender;
    
    @Column(name = "blood_group")
    private String bloodGroup;
    
    @Column(name = "marital_status")
    private String maritalStatus;
    
    @Column(name = "devotional_status_id")
    private Long devotionalStatusId;
    
    @Column(name = "namhatta_id")
    private Long namhattaId;
    
    @Column(name = "initiated_name")
    private String initiatedName;
    
    @Column(name = "harinam_date")
    private String harinamDate;
    
    @Column(name = "pancharatrik_date")
    private String pancharatrikDate;
    
    private String education;
    private String occupation;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "devotional_courses")
    private List<DevotionalCourse> devotionalCourses;
    
    @Column(name = "shraddhakutir_id")
    private Long shraddhakutirId;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

@Entity
@Table(name = "namhattas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Namhatta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "meeting_day")
    private String meetingDay;
    
    @Column(name = "meeting_time")
    private String meetingTime;
    
    @Column(name = "mala_senapoti")
    private String malaSenapoti;
    
    @Column(name = "maha_chakra_senapoti")
    private String mahaChakraSenapoti;
    
    @Column(name = "chakra_senapoti")
    private String chakraSenapoti;
    
    @Column(name = "upa_chakra_senapoti")
    private String upaChakraSenapoti;
    
    private String secretary;
    
    @Builder.Default
    private String status = "PENDING_APPROVAL";
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

#### Task 2.2: Create Repository Interfaces
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 2 hours
**Prerequisites**: Task 2.1 completed

**Sub-tasks:**
- [ ] Create JPA repositories for all entities
- [ ] Add custom query methods
- [ ] Implement complex filtering queries
- [ ] Add pagination support

### Phase 3: Custom Validation Utilities

#### Task 3.1: Generic Validation Framework
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 2.5 hours
**Prerequisites**: Task 2.2 completed

**Sub-tasks:**
- [ ] Create ValidationResult class
- [ ] Create FieldValidator interface
- [ ] Create ValidationUtils with common validations
- [ ] Create custom validators for business rules
- [ ] Test validation framework

**Custom Validation Framework:**
```java
@Data
@Builder
public class ValidationResult {
    @Builder.Default
    private boolean valid = true;
    
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    
    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }
    
    public static ValidationResult success() {
        return ValidationResult.builder().build();
    }
    
    public static ValidationResult failure(String error) {
        return ValidationResult.builder()
            .valid(false)
            .errors(List.of(error))
            .build();
    }
}

@Component
public class ValidationUtils {
    
    public ValidationResult validateRequired(String fieldName, Object value) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            return ValidationResult.failure(fieldName + " is required");
        }
        return ValidationResult.success();
    }
    
    public ValidationResult validateEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ValidationResult.failure("Invalid email format");
        }
        return ValidationResult.success();
    }
    
    public ValidationResult validatePhone(String phone) {
        if (phone != null && !phone.matches("^[+]?[0-9\\-\\s()]+$")) {
            return ValidationResult.failure("Invalid phone format");
        }
        return ValidationResult.success();
    }
    
    public ValidationResult validateDevotee(DevoteeDto devotee) {
        ValidationResult result = ValidationResult.success();
        
        // Required field validations
        result = combineResults(result, validateRequired("Legal name", devotee.getLegalName()));
        result = combineResults(result, validateRequired("Date of birth", devotee.getDob()));
        result = combineResults(result, validateRequired("Present address", devotee.getPresentAddress()));
        result = combineResults(result, validateRequired("Permanent address", devotee.getPermanentAddress()));
        
        // Format validations
        if (devotee.getEmail() != null) {
            result = combineResults(result, validateEmail(devotee.getEmail()));
        }
        if (devotee.getPhone() != null) {
            result = combineResults(result, validatePhone(devotee.getPhone()));
        }
        
        return result;
    }
    
    private ValidationResult combineResults(ValidationResult result1, ValidationResult result2) {
        if (result1.isValid() && result2.isValid()) {
            return ValidationResult.success();
        }
        
        List<String> allErrors = new ArrayList<>();
        if (!result1.isValid()) allErrors.addAll(result1.getErrors());
        if (!result2.isValid()) allErrors.addAll(result2.getErrors());
        
        return ValidationResult.builder()
            .valid(false)
            .errors(allErrors)
            .build();
    }
}
```

### Phase 4: Data Transfer Objects with Lombok

#### Task 4.1: Create Request/Response DTOs
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 3 hours
**Prerequisites**: Task 3.1 completed

**Sub-tasks:**
- [ ] Create DTOs matching OpenAPI specification exactly
- [ ] Use Lombok @Data, @Builder annotations
- [ ] Add JsonProperty annotations for field mapping
- [ ] Create conversion utilities between entities and DTOs

**Critical DTOs:**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevoteeDto {
    private Long id;
    
    @JsonProperty("legalName")
    private String legalName;
    
    private String name;
    private String dob;
    private String email;
    private String phone;
    private String fatherName;
    private String motherName;
    private String husbandName;
    private String gender;
    private String bloodGroup;
    private String maritalStatus;
    private String education;
    private String occupation;
    
    @JsonProperty("devotionalStatusId")
    private Long devotionalStatusId;
    
    @JsonProperty("namhattaId")
    private Long namhattaId;
    
    @JsonProperty("shraddhakutirId")
    private Long shraddhakutirId;
    
    @JsonProperty("initiatedName")
    private String initiatedName;
    
    @JsonProperty("harinamInitiationDate")
    private String harinamDate;
    
    @JsonProperty("pancharatrikInitiationDate")
    private String pancharatrikDate;
    
    @JsonProperty("devotionalCourses")
    private List<DevotionalCourseDto> devotionalCourses;
    
    @JsonProperty("presentAddress")
    private AddressDto presentAddress;
    
    @JsonProperty("permanentAddress")
    private AddressDto permanentAddress;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NamhattaDto {
    private Long id;
    private String code;
    private String name;
    private String meetingDay;
    private String meetingTime;
    private String malaSenapoti;
    private String mahaChakraSenapoti;
    private String chakraSenapoti;
    private String upaChakraSenapoti;
    private String secretary;
    private String status;
    
    @JsonProperty("nagarKirtan")
    private Integer nagarKirtan;
    
    @JsonProperty("bookDistribution")
    private Integer bookDistribution;
    
    private Integer chanting;
    private Integer arati;
    
    @JsonProperty("bhagwatPath")
    private Integer bhagwatPath;
    
    private AddressDto address;
    
    @JsonProperty("devoteeCount")
    private Integer devoteeCount;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Phase 5: Spring Security & JWT Authentication

#### Task 5.1: JWT Token Provider
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 2 hours
**Prerequisites**: Task 4.1 completed

**Sub-tasks:**
- [ ] Create JwtTokenProvider matching Node.js implementation
- [ ] Implement token generation/validation
- [ ] Add session token support for single login
- [ ] Create token blacklisting service

**JWT Implementation:**
```java
@Component
@Slf4j
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    
    public String generateToken(UserPrincipal userPrincipal, String sessionToken, List<String> districts) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        
        return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .claim("userId", userPrincipal.getId())
            .claim("role", userPrincipal.getRole())
            .claim("sessionToken", sessionToken)
            .claim("districts", districts)
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}
```

### Phase 6: Service Layer Implementation

#### Task 6.1: Core Business Services
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 4 hours
**Prerequisites**: Task 5.1 completed

**Sub-tasks:**
- [ ] Create DevoteeService with all CRUD operations
- [ ] Create NamhattaService with approval workflow
- [ ] Create GeographicService for location data
- [ ] Create DashboardService for analytics
- [ ] Implement district-based filtering

### Phase 7: REST Controllers & API Layer

#### Task 7.1: Authentication Controller
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 2.5 hours
**Prerequisites**: Task 6.1 completed

**Sub-tasks:**
- [ ] Create AuthController with login/logout/verify endpoints
- [ ] Implement HTTP-only cookie handling
- [ ] Add rate limiting
- [ ] Test authentication flow

#### Task 7.2: Entity Controllers
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 4 hours
**Prerequisites**: Task 7.1 completed

**Sub-tasks:**
- [ ] Create DevoteeController with all endpoints
- [ ] Create NamhattaController with all endpoints
- [ ] Create GeographicController
- [ ] Create DashboardController
- [ ] Implement proper error handling

### Phase 8: Swagger Integration & Documentation

#### Task 8.1: OpenAPI Configuration
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 1.5 hours
**Prerequisites**: Task 7.2 completed

**Sub-tasks:**
- [ ] Configure springdoc-openapi
- [ ] Add API documentation annotations
- [ ] Generate Swagger UI
- [ ] Verify API documentation matches specification

### Phase 9: Testing & Validation

#### Task 9.1: API Compatibility Testing
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 3 hours
**Prerequisites**: Task 8.1 completed

**Sub-tasks:**
- [ ] Test all 37 endpoints
- [ ] Verify response formats match Node.js exactly
- [ ] Test authentication and authorization
- [ ] Validate error responses

### Phase 10: Frontend Integration

#### Task 10.1: Frontend Configuration
**Status**: ☐ Not Started | ☐ In Progress | ☐ Completed
**Estimated Time**: 1 hour
**Prerequisites**: Task 9.1 completed

**Sub-tasks:**
- [ ] Update VITE_API_BASE_URL to Spring Boot
- [ ] Test frontend integration
- [ ] Verify all functionality works
- [ ] Document migration completion

## Success Criteria

### Technical Validation
- [ ] All 37 API endpoints function identically to Node.js
- [ ] Authentication system works with existing JWT_SECRET
- [ ] Database queries return identical results
- [ ] Error responses match Node.js format exactly
- [ ] Performance is equal or better than Node.js

### Functional Validation
- [ ] Frontend works 100% with Spring Boot backend
- [ ] All user roles function correctly
- [ ] District filtering works for supervisors
- [ ] CRUD operations preserve data integrity
- [ ] File uploads and geographic data work correctly

**Total Tasks**: 20 tasks across 10 phases
**Estimated Total Time**: 32 hours
**Critical Path**: Authentication → Entities → Services → Controllers → Testing

## Migration Notes

### Key Differences from Node.js
1. **Validation**: Custom utilities instead of Zod schemas
2. **ORM**: JPA/Hibernate instead of Drizzle
3. **Dependency Injection**: Spring instead of manual imports
4. **Configuration**: YAML instead of environment variables
5. **Build**: Maven instead of npm

### Risk Mitigation
1. Keep Node.js version running during migration
2. Use feature flags for gradual rollout
3. Maintain identical database schema
4. Test each endpoint individually
5. Monitor performance metrics