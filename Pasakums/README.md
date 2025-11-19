# Pasakumu Aplikācija - Spring Boot Backend

**Autors:** Oskars  
**Projekta tips:** Eksāmena darbs  
**Tehnoloģijas:** Java 25, Spring Boot 3.5.7, PostgreSQL, Lombok  
**IDE:** VS Code

---

## 📋 Projekta Apraksts

Pasākumu pārvaldības sistēma ar lietotāju reģistrāciju, autentifikāciju un pasākumu organizēšanu. Backend API nodrošina pilnu CRUD funkcionalitāti lietotājiem un pasākumiem.

---

## 🏗️ Izstrādes Soļi (Step-by-Step Guide)

### **1. solis: Projekta Inicializācija**

```bash
# Izveidojām Spring Boot projektu ar Maven
# Konfigurējām pom.xml ar nepieciešamajām atkarībām
```

**Pievienotās atkarības:**

- `spring-boot-starter-web` - REST API funkcionalitāte
- `spring-boot-starter-data-jpa` - Datubāzes integrācija
- `postgresql` - PostgreSQL draiveris
- `lombok` - Koda saīsināšanai
- `spring-boot-starter-security` - Drošības slānis

### **2. solis: Datubāzes Konfigurācija**

**Fails:** `src/main/resources/application-dev.properties`

```properties
# PostgreSQL savienojuma konfigurācija
spring.datasource.url=jdbc:postgresql://localhost:5432/Pasakums
spring.datasource.username=postgres
spring.datasource.password=parole
server.port=8080
```

**Ko mācījāmies:**

- Spring Boot profilu izmantošana (dev, prod)
- Datubāzes savienojuma konfigurēšana
- PostgreSQL iestatīšana atsevišķā datubāzē

### **3. solis: Entity Slāņa Izveide**

**Pirmā pieeja:** Java Records (problēmas ar JPA setteriem)

```java
// Nestrādāja - JPA nevar setot ID vērtības Records objektiem
public record Lietotajs(@Id Long id, String lietotajvards, String parole) {}
```

**Otrā pieeja:** Parasti Java objekti

```java
// Strādāja, bet daudz koda (59 līnijas)
public class Lietotajs {
    private Long id;
    // + getteri, setteri, konstruktori...
}
```

**Galīgā pieeja:** Lombok annotations

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lietotajs {
    @Id @GeneratedValue private Long id;
    @Column(nullable = false, unique = true) private String lietotajvards;
    @Column(nullable = false) private String parole;
}
```

**Ko mācījāmies:**

- JPA entity mapping
- Primary key ģenerēšana
- Database constraints (nullable, unique)
- Lombok magic (@Data automātiski ģenerē getterus/setterus)

### **4. solis: Repository Slāņa Izveide**

**Fails:** `src/main/java/.../repository/LietotajsRepository.java`

```java
@Repository
public interface LietotajsRepository extends JpaRepository<Lietotajs, Long> {
    // Spring Data JPA automātiski ģenerē CRUD metodes
    Optional<Lietotajs> findByLietotajvards(String lietotajvards);
    boolean existsByLietotajvards(String lietotajvards);
}
```

**Ko mācījāmies:**

- Spring Data JPA magic
- Custom query methods (findBy...)
- Optional<> pattern null safety
- Repository pattern priekš datubāzes piekļuves

### **5. solis: Service Slāņa Izveide**

**Fails:** `src/main/java/.../service/LietotajsService.java`

```java
@Service
public class LietotajsService {
    private final LietotajsRepository repository;

    // Business logic + validation
    public Long createLietotajs(Lietotajs lietotajs) {
        if (repository.existsByLietotajvards(lietotajs.getLietotajvards())) {
            throw new RuntimeException("Lietotājvārds jau pastāv");
        }
        Lietotajs saved = repository.save(lietotajs);
        return saved.getId(); // Atgriežam tikai ID (nav JPA setter problēmu)
    }
}
```

**Ko mācījāmies:**

- Dependency Injection (constructor injection)
- Business logic atdalīšana no kontrolleriem
- Validation logic
- Error handling ar exceptions
- ID-only response pattern (izvairoties no JPA problēmām)

### **6. solis: Controller Slāņa Izveide**

**Fails:** `src/main/java/.../controller/LietotajsController.java`

```java
@RestController
@RequestMapping("/api/lietotaji")
@CrossOrigin(origins = "*")
public class LietotajsController {

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody Lietotajs lietotajs) {
        try {
            Long userId = service.createLietotajs(lietotajs);
            return ResponseEntity.ok(userId); // Atgriežam tikai ID
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
```

**Ko mācījāmies:**

- REST API endpoints (@GetMapping, @PostMapping)
- HTTP status codes (200 OK, 400 Bad Request)
- JSON serialization/deserialization
- CORS konfigurācija frontend integrācijai
- ResponseEntity pattern

### **7. solis: Security Konfigurācija**

**Fails:** `src/main/java/.../config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                   .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/api/**").permitAll()
                       .anyRequest().authenticated())
                   .build();
    }
}
```

**Ko mācījāmies:**

- Spring Security konfigurācija
- CSRF aizsardzības atslēgšana API priekš
- Endpoint aizsardzības noteikumi
- Development vs Production security

### **8. solis: Problēmu Risināšana**

**Galvenās problēmas un risinājumi:**

1. **Java Records + JPA setter problēma**

   ```
   Error: Could not set value of type [java.lang.Long]: 'id' (setter)
   Risinājums: Pārslēgšanās uz Lombok @Data annotations
   ```

2. **Controller routing konflikti**

   ```
   Problem: /{id} endpoint sajauc ar /register
   Risinājums: Specific paths pirms generic paths
   ```

3. **Database connection issues**
   ```
   Risinājums: Atsevišķa "Pasakums" datubāze, nevis default "postgres"
   ```

### **9. solis: Testēšana**

**Izmantotie rīki:**

- **Insomnia** - REST API testēšanai
- **pgAdmin** - Datubāzes datu pārbaudei
- **Browser** - GET endpoint testēšanai

**Testa scenāriji:**

1. `POST /api/lietotaji/register` → atgriež user ID
2. `GET /api/lietotaji` → atgriež lietotāju sarakstu
3. `POST /api/pasakumi` → izveido jaunu pasākumu
4. `POST /api/pasakumi/{id}/register` → reģistrē dalību pasākumā

---

## 📁 Projekta Struktūra

```
src/main/java/org/oskars/Pasakums/
├── PasakumsApplication.java          # Main class
├── config/
│   └── SecurityConfig.java           # Security konfigurācija
├── entity/
│   ├── Lietotajs.java                # User entity (Lombok)
│   └── Pasakums.java                 # Event entity (Lombok)
├── repository/
│   ├── LietotajsRepository.java      # User data access
│   └── PasakumsRepository.java       # Event data access
├── service/
│   ├── LietotajsService.java         # User business logic
│   └── PasakumsService.java          # Event business logic
└── controller/
    ├── LietotajsController.java      # User REST API
    └── PasakumsController.java       # Event REST API
```

---

## 🔄 Data Flow (Datu Plūsma)

```
1. HTTP Request (JSON)
   ↓
2. Controller (@RestController)
   ↓
3. Service (@Service) - Business Logic
   ↓
4. Repository (@Repository) - JPA
   ↓
5. Database (PostgreSQL)
   ↓
6. Entity (Lombok @Data)
   ↓
7. JSON Response (ResponseEntity)
```

---

## 🎯 API Endpoints

### Lietotāju API

- `GET /api/lietotaji` - Visu lietotāju saraksts
- `POST /api/lietotaji/register` - Jauna lietotāja reģistrācija
- `POST /api/lietotaji/login` - Lietotāja pieteikšanās
- `GET /api/lietotaji/{id}` - Lietotājs pēc ID
- `GET /api/lietotaji/check/{username}` - Pārbauda lietotājvārda esamību

### Pasākumu API

- `GET /api/pasakumi` - Visu pasākumu saraksts
- `POST /api/pasakumi` - Jauna pasākuma izveide
- `GET /api/pasakumi/{id}` - Pasākums pēc ID
- `POST /api/pasakumi/{id}/register` - Reģistrācija pasākumam
- `GET /api/pasakumi/search/nosaukums?q=` - Meklēšana pēc nosaukuma

---

## 💡 Galvenie Mācību Punkti

### 1. **Layered Architecture**

- **Controller** - HTTP requests/responses
- **Service** - Business logic un validation
- **Repository** - Database access
- **Entity** - Data models

### 2. **Spring Boot Annotations**

- `@RestController` - REST API endpoints
- `@Service` - Business logic components
- `@Repository` - Data access components
- `@Entity` - JPA database entities
- `@Data` - Lombok auto-generation

### 3. **Best Practices**

- Constructor injection (ne @Autowired)
- ID-only responses (JPA compatibility)
- Proper HTTP status codes
- CORS enablement priekš frontend
- Environment-specific configuration

### 4. **Problem Solving Skills**

- JPA entity design challenges
- Database connectivity issues
- API routing conflicts
- JSON serialization problems

---

## 🚀 Nākamie Soļi

1. **Frontend Development** - React/Vue.js integrācija
2. **Advanced Security** - JWT authentication
3. **Validation** - @Valid annotations
4. **Testing** - Unit un integration tests
5. **Documentation** - Swagger/OpenAPI
6. **Deployment** - Docker containerization

---

## 🛠️ Kā Palaist Projektu

1. **Database Setup:**

   ```sql
   CREATE DATABASE "Pasakums";
   ```

2. **Application Startup:**

   ```bash
   ./mvnw spring-boot:run
   ```

3. **Test API:**
   ```bash
   curl http://localhost:8080/api/lietotaji
   ```

---

**Secinājums:** Šis projekts demonstrē pilnu Spring Boot backend izstrādi no nulles līdz funkcionējošai API sistēmai ar datubāzi, iekļaujot reālās problēmas un to risinājumus, kas sastopami profesionālā izstrādē.
