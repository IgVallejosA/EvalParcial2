# GUIÓN DE DEFENSA TÉCNICA — EVALUACIÓN PARCIAL 2
## Sistema de Gestión de Esports · Arquitectura de Microservicios
### DSY1103 Desarrollo FullStack 1 · DuocUC · 15 minutos

---

> **Cómo usar este guión:** Cada sección indica el indicador de rúbrica que cubre y su peso. Las secciones de **cambio en vivo** son las más importantes (38% de la nota). Para cada cambio se indica exactamente qué archivo abrir, qué línea modificar y cómo verificarlo en Postman.

---

## APERTURA (30 segundos)

> "El proyecto es un sistema de gestión de esports organizado como arquitectura de microservicios en Spring Boot. Implementamos 10 microservicios independientes, cada uno con su propia base de datos MySQL, siguiendo el patrón Controller-Service-Repository."

---

## BLOQUE 1 — Modelado de datos y JPA (IE 2.1.3 · 4%)

### Qué decir

> "Cada microservicio modela sus propias entidades JPA de forma independiente. Usamos @Entity, @Id con @GeneratedValue, y anotaciones de relación como @OneToMany y @ManyToOne. La base de datos la gestiona Flyway mediante migraciones SQL versionadas."

### Ejemplo concreto: entidad `Juego` (ms-juegos)

Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/model/Juego.java`

```java
@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "genero", nullable = false, length = 20)
    private String genero;

    // Relación 1:N — un Juego tiene muchos ModoCompetitivo
    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModoCompetitivo> modos = new ArrayList<>();
}
```

### Qué justificar al docente

- **Normalización:** Cada microservicio tiene su propia BD. `ms-juegos` guarda juegos y modos; `ms-transferencias` desnormaliza el nombre del jugador/equipo a propósito para no depender del microservicio en lecturas.
- **Clave foránea vs referencia remota:** Dentro de un microservicio usamos `@ManyToOne` real. Entre microservicios solo guardamos el `id` como Long (no FK cruzada), para mantener independencia.
- **CascadeType.ALL + orphanRemoval:** Al eliminar un `Juego`, se eliminan automáticamente sus `ModoCompetitivo` hijos.

---

## BLOQUE 2 — Patrón CSR y capa de servicio (IE 1.2.1 · 3% grupal + IE 2.2.4 · 6%)

### Qué decir

> "Cada microservicio separa responsabilidades en tres capas: Controller recibe la petición HTTP y delega. Service contiene toda la lógica de negocio. Repository accede a la BD mediante JpaRepository. Los DTOs separan la capa de entrada/salida del modelo de persistencia."

### Flujo a señalar en código

Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/controller/JuegoController.java`

```java
@PostMapping
public ResponseEntity<JuegoResponseDTO> crear(@Valid @RequestBody JuegoRequestDTO dto) {
    log.debug("POST /api/v1/juegos - {}", dto.getNombre());
    return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.crearJuego(dto));
    // El controller SOLO delega. Toda la lógica está en el service.
}
```

Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/service/JuegoService.java`

```java
@Transactional
public JuegoResponseDTO crearJuego(JuegoRequestDTO dto) {
    log.info("Creando juego: {}", dto.getNombre());
    try {
        // REGLA DE NEGOCIO: no pueden existir dos juegos con el mismo nombre
        if (juegoRepository.existsByNombre(dto.getNombre())) {
            log.warn("Juego duplicado rechazado: {}", dto.getNombre());
            throw new ReglaNegocioException("Ya existe un juego registrado con el nombre: " + dto.getNombre());
        }
        Juego juego = Juego.builder()
                .nombre(dto.getNombre())
                .activo(true)
                .build();
        Juego guardado = juegoRepository.save(juego);
        log.info("Juego creado - ID: {}, Nombre: {}", guardado.getId(), guardado.getNombre());
        return mapearJuegoAResponse(guardado);
    } catch (ReglaNegocioException e) {
        throw e;  // se relanza para que el GlobalExceptionHandler la capture
    } catch (Exception e) {
        log.error("Error inesperado al crear juego: {}", e.getMessage(), e);
        throw new ReglaNegocioException("Error interno al crear el juego: " + e.getMessage());
    }
}
```

### Qué justificar al docente

- El Service usa `@Transactional` para garantizar atomicidad.
- El try/catch diferenciado relanza las excepciones de negocio sin envolverlas, y captura cualquier otra cosa con un mensaje genérico.
- El Repository solo declara la interfaz; Spring Data JPA genera la implementación en tiempo de ejecución.

---

## BLOQUE 3 — Validaciones Bean Validation (IE 2.2.2 · 3% grupal)

### Qué mostrar

Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/dto/JuegoRequestDTO.java`

```java
public class JuegoRequestDTO {

    @NotBlank(message = "El nombre del juego es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "MOBA|FPS|RTS|FIGHTING|BR|CARDS|SPORTS",
             message = "Género inválido. Valores permitidos: MOBA, FPS, RTS, FIGHTING, BR, CARDS, SPORTS")
    private String genero;

    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    @PastOrPresent(message = "La fecha de lanzamiento no puede ser futura")
    private LocalDate fechaLanzamiento;

    @PositiveOrZero(message = "El prize pool no puede ser negativo")
    private Double prizePoolTotalUsd;
}
```

### Qué decir

> "Los DTOs usan anotaciones JSR 380 para validar antes de que el dato llegue al service. En el controller ponemos `@Valid` para activar esa validación. Si falla, el GlobalExceptionHandler captura la excepción `MethodArgumentNotValidException` y retorna 400 con los errores por campo."

---

## BLOQUE 4 — Manejo de excepciones y logs (IE 2.3.1 · 3% + IE 2.3.2 · 2% + IE 2.3.3 · 7%)

### Qué mostrar: GlobalExceptionHandler

Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/exception/GlobalExceptionHandler.java`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {
        log.warn("Recurso no encontrado: {} | URI: {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())   // 404
                        .error("Recurso no encontrado")
                        .mensaje(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> handleReglaNegocio(
            ReglaNegocioException ex, HttpServletRequest request) {
        log.warn("Violación regla de negocio: {} | URI: {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(     // 409
                ErrorResponse.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error("Conflicto con regla de negocio")
                        .mensaje(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errores.put(fe.getField(), fe.getDefaultMessage());
        }
        log.warn("Validación fallida: {} | URI: {}", errores, request.getRequestURI());
        return ResponseEntity.badRequest().body(    // 400
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Validación fallida")
                        .erroresValidacion(errores)
                        .build());
    }
}
```

### Cómo interpretar un log en tiempo real

Cuando el docente pida interpretar un error en consola, buscar el patrón:

```
2025-05-15 10:30:12 [http-nio-8084-exec-1] WARN  c.e.m.e.GlobalExceptionHandler - Recurso no encontrado: Juego no encontrado con ID: 99 | URI: /api/v1/juegos/99
```

- `WARN` → excepción controlada de negocio (no es un crash)
- `c.e.m.e.GlobalExceptionHandler` → capturado en el handler centralizado
- `URI: /api/v1/juegos/99` → el endpoint que provocó el error

---

## BLOQUE 5 — Comunicación entre microservicios (IE 2.4.1 · 3% + IE 2.4.2 · 2% + IE 2.4.3 · 5%)

### Qué mostrar: JugadorClient con WebClient

Archivo: `ms-transferencias/src/main/java/com/esports/mstransferencias/client/JugadorClient.java`

```java
@Component
@RequiredArgsConstructor
public class JugadorClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private final WebClient webClientJugadores;

    public JugadorRemotoDTO obtenerJugadorPorId(Long id) {
        try {
            return webClientJugadores.get()
                    .uri("/{id}", id)
                    .retrieve()
                    // Si ms-jugadores responde 4xx → RecursoNoEncontradoException
                    .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new RecursoNoEncontradoException("Jugador " + id + " no existe en ms-jugadores")))
                    // Si ms-jugadores responde 5xx → ComunicacionMicroservicioException
                    .onStatus(HttpStatusCode::is5xxServerError, r ->
                        Mono.error(new ComunicacionMicroservicioException("ms-jugadores no disponible")))
                    .bodyToMono(JugadorRemotoDTO.class)
                    .timeout(TIMEOUT)   // timeout de 5 segundos para no bloquear el hilo
                    .block();
        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Falla ms-jugadores: {}", e.getMessage());
            throw new ComunicacionMicroservicioException("No se pudo comunicar con ms-jugadores");
        }
    }
}
```

Archivo: `ms-transferencias/src/main/resources/application.properties`

```properties
microservicios.ms-jugadores.url=http://localhost:8081/api/v1/jugadores
microservicios.ms-equipos.url=http://localhost:8082/api/v1/equipos
```

### Qué justificar al docente

> "Cuando se registra una transferencia, ms-transferencias (puerto 8090) llama a ms-jugadores (8081) para verificar que el jugador existe y obtener su nickname. Si el jugador no existe, ms-jugadores retorna 404 y el client convierte eso en una excepción que sube por el GlobalExceptionHandler. Si ms-jugadores está caído, el timeout de 5 segundos evita que la petición quede colgada indefinidamente."

---

## BLOQUE 6 — Aporte personal (IE 2.5.3 · 5%)

> Adaptar este bloque con la realidad del trabajo. Ejemplo:

> "Yo implementé los microservicios ms-juegos y ms-transferencias. En ms-juegos hice el modelado de la entidad Juego con su relación 1:N a ModoCompetitivo, las validaciones del RequestDTO, el GlobalExceptionHandler y los logs con SLF4J. En ms-transferencias diseñé la lógica de validación de tipos de transferencia y la comunicación con WebClient hacia ms-jugadores y ms-equipos. Mis commits están en GitHub desde la semana 2 hasta la semana 9."

---
---

# CAMBIOS EN VIVO — SECCIÓN CRÍTICA (38% de la nota)

> **Practica cada cambio 3 veces antes de la defensa.** El flujo siempre es: abrir archivo → hacer cambio → guardar → compilar con Maven → probar en Postman.

---

## CAMBIO A — Agregar/Modificar validación en DTO (IE 2.2.5 · 12%)

### Escenario típico que puede pedir el docente:
> *"Agrega una validación para que el prize pool no supere los $1.000.000 USD"*

**Archivo a abrir:**
`ms-juegos/src/main/java/com/esports/msjuegos/dto/JuegoRequestDTO.java`

**Código ANTES (línea 34):**
```java
@PositiveOrZero(message = "El prize pool no puede ser negativo")
private Double prizePoolTotalUsd;
```

**Código DESPUÉS:**
```java
@PositiveOrZero(message = "El prize pool no puede ser negativo")
@DecimalMax(value = "1000000.0", message = "El prize pool no puede superar $1,000,000 USD")
private Double prizePoolTotalUsd;
```

**Verificar en Postman:**
```
POST http://localhost:8084/api/v1/juegos
{
  "nombre": "TestGame",
  "genero": "FPS",
  "desarrolladora": "Test Dev",
  "fechaLanzamiento": "2020-01-01",
  "plataforma": "PC",
  "prizePoolTotalUsd": 9999999
}
```
Resultado esperado: `400 Bad Request` con mensaje `"El prize pool no puede superar $1,000,000 USD"`

---

### Escenario alternativo:
> *"Agrega una regla en el service que impida crear un juego con fecha de lanzamiento anterior al año 1990"*

**Archivo a abrir:**
`ms-juegos/src/main/java/com/esports/msjuegos/service/JuegoService.java`

**Agregar DENTRO del método `crearJuego`, justo antes de la línea `Juego juego = Juego.builder()...`:**
```java
// NUEVA REGLA DE NEGOCIO
if (dto.getFechaLanzamiento().getYear() < 1990) {
    throw new ReglaNegocioException("No se aceptan juegos anteriores a 1990");
}
```

**Verificar en Postman:**
```
POST http://localhost:8084/api/v1/juegos
{
  "nombre": "OldGame",
  "genero": "FPS",
  "desarrolladora": "Retro Dev",
  "fechaLanzamiento": "1985-06-15",
  "plataforma": "PC",
  "prizePoolTotalUsd": 0
}
```
Resultado esperado: `409 Conflict` con mensaje `"No se aceptan juegos anteriores a 1990"`

---

## CAMBIO B — Modificar logs, excepciones y códigos HTTP (IE 2.3.4 · 13%)

### Escenario típico:
> *"Cambia el código HTTP de ReglaNegocioException de 409 a 422 Unprocessable Entity"*

**Archivo a abrir:**
`ms-juegos/src/main/java/com/esports/msjuegos/exception/GlobalExceptionHandler.java`

**Código ANTES (línea ~38-47):**
```java
@ExceptionHandler(ReglaNegocioException.class)
public ResponseEntity<ErrorResponse> handleReglaNegocio(
        ReglaNegocioException ex, HttpServletRequest request) {
    log.warn("Violación regla de negocio: {} | URI: {}", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.CONFLICT.value())
                    .error("Conflicto con regla de negocio")
                    .mensaje(ex.getMessage())
                    .path(request.getRequestURI())
                    .build());
}
```

**Código DESPUÉS:**
```java
@ExceptionHandler(ReglaNegocioException.class)
public ResponseEntity<ErrorResponse> handleReglaNegocio(
        ReglaNegocioException ex, HttpServletRequest request) {
    log.warn("Regla de negocio incumplida: {} | URI: {}", ex.getMessage(), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
            ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY.value())   // 422 en vez de 409
                    .error("Entidad no procesable")
                    .mensaje(ex.getMessage())
                    .path(request.getRequestURI())
                    .build());
}
```

**Verificar en Postman:**
```
POST http://localhost:8084/api/v1/juegos
{
  "nombre": "League of Legends",   <- nombre que ya existe en la BD
  "genero": "MOBA",
  "desarrolladora": "Riot Games",
  "fechaLanzamiento": "2009-10-27",
  "plataforma": "PC",
  "prizePoolTotalUsd": 500000
}
```
Resultado esperado: Status `422 Unprocessable Entity` (antes era 409)

---

### Escenario alternativo:
> *"Agrega un log de nivel ERROR con el stack trace en el handler de excepción genérica"*

**Archivo:** `ms-juegos/src/main/java/com/esports/msjuegos/exception/GlobalExceptionHandler.java`

**Código ANTES (handler genérico, ~línea 69):**
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenerico(Exception ex, HttpServletRequest request) {
    log.error("Error no controlado: {} | URI: {}", ex.getMessage(), request.getRequestURI(), ex);
    ...
}
```

**Código DESPUÉS (agregar también el tipo de excepción):**
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenerico(Exception ex, HttpServletRequest request) {
    log.error("Error no controlado [{}]: {} | URI: {}",
              ex.getClass().getSimpleName(), ex.getMessage(), request.getRequestURI(), ex);
    ...
}
```

**Cómo verificar:** Volver a ejecutar la petición que generaba error y observar en consola que ahora aparece el nombre de la clase de excepción.

---

## CAMBIO C — Modificar comunicación entre microservicios (IE 2.4.4 · 12%)

### Escenario típico:
> *"Cambia el timeout de la comunicación con ms-jugadores de 5 a 10 segundos"*

**Archivo a abrir:**
`ms-transferencias/src/main/java/com/esports/mstransferencias/client/JugadorClient.java`

**Código ANTES (línea 20):**
```java
private static final Duration TIMEOUT = Duration.ofSeconds(5);
```

**Código DESPUÉS:**
```java
private static final Duration TIMEOUT = Duration.ofSeconds(10);
```

**Agregar también un log para que sea visible el cambio:**
```java
public JugadorRemotoDTO obtenerJugadorPorId(Long id) {
    log.info("Consultando ms-jugadores para ID: {} (timeout: {}s)", id, TIMEOUT.getSeconds());
    try {
        return webClientJugadores.get()
            ...
```

**Verificar en Postman:**
```
POST http://localhost:8090/api/v1/transferencias
{
  "idJugador": 1,
  "idEquipoDestino": 1,
  "tipo": "FICHAJE_INICIAL",
  "fechaTransferencia": "2025-05-15",
  "montoUsd": 50000,
  "duracionContratoMeses": 12
}
```
Observar en consola del ms-transferencias: `Consultando ms-jugadores para ID: 1 (timeout: 10s)`

---

### Escenario alternativo:
> *"Agrega manejo de error cuando ms-equipos retorne un equipo inactivo con un mensaje más descriptivo"*

**Archivo a abrir:**
`ms-transferencias/src/main/java/com/esports/mstransferencias/service/TransferenciaService.java`

**Código ANTES (línea ~49):**
```java
if (Boolean.FALSE.equals(destino.getActivo())) {
    throw new ReglaNegocioException(
            "No se puede transferir a equipo inactivo: " + destino.getNombre());
}
```

**Código DESPUÉS:**
```java
if (Boolean.FALSE.equals(destino.getActivo())) {
    log.warn("Intento de transferencia a equipo inactivo - ID: {}, Nombre: {}",
             dto.getIdEquipoDestino(), destino.getNombre());
    throw new ReglaNegocioException(
            "El equipo destino '" + destino.getNombre() + "' (ID: " + dto.getIdEquipoDestino() +
            ") está inactivo. No se pueden registrar transferencias a equipos inactivos.");
}
```

**Verificar en Postman:** Intentar una transferencia a un equipo inactivo y ver el mensaje más detallado en la respuesta 409.

---

## CAMBIO D — Modificación de entidad en vivo (IE 2.1.4 · 6%)

### Escenario típico:
> *"Agrega un campo 'descripcion' opcional al juego"*

**PASO 1 — Modificar la entidad:**
Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/model/Juego.java`

```java
// Agregar este campo (después de plataforma, por ejemplo):
@Column(name = "descripcion", length = 500)
private String descripcion;
```

**PASO 2 — Modificar el DTO de request:**
Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/dto/JuegoRequestDTO.java`

```java
// Agregar al final de los campos (sin validación, es opcional):
@Size(max = 500, message = "La descripción no puede superar 500 caracteres")
private String descripcion;
```

**PASO 3 — Modificar el DTO de response:**
Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/dto/JuegoResponseDTO.java`

```java
// Agregar al final de los campos:
private String descripcion;
```

**PASO 4 — Modificar el service:**
Archivo: `ms-juegos/src/main/java/com/esports/msjuegos/service/JuegoService.java`

En el método `crearJuego`, dentro del `Juego.builder()`:
```java
Juego juego = Juego.builder()
        .nombre(dto.getNombre())
        .genero(dto.getGenero())
        .desarrolladora(dto.getDesarrolladora())
        .fechaLanzamiento(dto.getFechaLanzamiento())
        .plataforma(dto.getPlataforma())
        .descripcion(dto.getDescripcion())   // NUEVA LÍNEA
        .prizePoolTotalUsd(...)
        .activo(true)
        .build();
```

En el método `mapearJuegoAResponse`:
```java
return JuegoResponseDTO.builder()
        ...
        .descripcion(j.getDescripcion())   // NUEVA LÍNEA
        .build();
```

**PASO 5 — Agregar migración Flyway:**
Crear archivo: `ms-juegos/src/main/resources/db/migration/V3__add_descripcion_to_juegos.sql`
```sql
ALTER TABLE juegos ADD COLUMN descripcion VARCHAR(500) NULL;
```

> **IMPORTANTE:** La versión del script debe ser mayor a la última que existe. Revisar primero los archivos V1__ y V2__ que ya existen.

**PASO 6 — Compilar y verificar:**

En Postman:
```
POST http://localhost:8084/api/v1/juegos
{
  "nombre": "Valorant",
  "genero": "FPS",
  "desarrolladora": "Riot Games",
  "fechaLanzamiento": "2020-06-02",
  "plataforma": "PC",
  "prizePoolTotalUsd": 1000000,
  "descripcion": "Shooter táctico 5v5 con habilidades"
}
```
El response debe incluir el campo `descripcion`.

---

## TABLA DE TIEMPOS SUGERIDA

| Bloque | Contenido | Tiempo |
|--------|-----------|--------|
| Apertura | Presentar el proyecto | 30 seg |
| Bloque 1 | Modelado JPA (IE 2.1.3) | 1.5 min |
| Bloque 2 | CSR + Service (IE 1.2.1 + IE 2.2.4) | 2 min |
| Bloque 3 | Bean Validation (IE 2.2.2) | 1 min |
| Bloque 4 | Excepciones + Logs (IE 2.3.1/2/3) | 2 min |
| Bloque 5 | Comunicación WebClient (IE 2.4.3) | 1.5 min |
| Bloque 6 | Aporte personal (IE 2.5.3) | 1 min |
| **Cambios en vivo** | **IE 2.2.5 + IE 2.3.4 + IE 2.4.4** | **hasta 5 min** |

> El docente puede interrumpir en cualquier momento y pedir un cambio en vivo. Estar preparado desde el minuto 1.

---

## MAPA DE PUERTOS (referencia rápida)

| Microservicio | Puerto | Base de datos |
|---------------|--------|---------------|
| ms-jugadores | 8081 | ms_jugadores |
| ms-equipos | 8082 | ms_equipos |
| ms-juegos | 8084 | ms_juegos |
| ms-partidas | 8085 | ms_partidas |
| ms-estadisticas | 8086 | ms_estadisticas |
| ms-patrocinadores | 8087 | ms_patrocinadores |
| ms-streamers | 8088 | ms_streamers |
| ms-rankings | 8089 | ms_rankings |
| ms-transferencias | 8090 | ms_transferencias |
| ms-torneos | 8084* | ms_torneos |

> ⚠️ ms-torneos y ms-juegos tienen el mismo puerto 8084 en sus `application.properties`. Revisar y corregir antes de la defensa (sugerencia: ms-torneos → 8083).

---

## PREGUNTAS FRECUENTES DEL DOCENTE Y RESPUESTAS

**¿Por qué usar JpaRepository en vez de escribir las queries manualmente?**
> "JpaRepository provee métodos CRUD predefinidos (save, findById, findAll, deleteById) sin necesidad de escribir SQL. Además permite definir queries derivadas del nombre del método, como `findByNombre` o `existsByNombre`. Esto reduce código y errores."

**¿Para qué sirve `@Transactional`?**
> "Garantiza que todas las operaciones de BD dentro del método se ejecuten como una sola unidad atómica. Si cualquier parte falla, hace rollback automático. `readOnly = true` le indica a Hibernate que no va a haber escrituras, lo que optimiza el rendimiento."

**¿Qué diferencia hay entre `@ControllerAdvice` y `@RestControllerAdvice`?**
> "`@RestControllerAdvice` es la combinación de `@ControllerAdvice` + `@ResponseBody`. Hace que todos los métodos del handler serialicen automáticamente el objeto de retorno como JSON, que es lo que necesitamos en una API REST."

**¿Por qué usar DTOs en lugar de devolver la entidad directamente?**
> "Las entidades son objetos de persistencia acoplados a Hibernate (con proxies lazy, etc.). Exponerlas directamente puede causar serialización infinita en relaciones bidireccionales, exponer campos sensibles o acoplar el contrato de la API al modelo de BD. Los DTOs permiten controlar exactamente qué datos se exponen."

**¿Qué pasa si ms-jugadores está caído cuando llega una transferencia?**
> "El `WebClient` tiene un timeout de 5 segundos. Si no responde en ese tiempo, lanza una excepción que el `JugadorClient` convierte en `ComunicacionMicroservicioException`. El `GlobalExceptionHandler` de ms-transferencias la captura y retorna `503 Service Unavailable` con un mensaje claro."

**¿Qué es Flyway y por qué lo usamos?**
> "Flyway es una herramienta de migración de base de datos. En vez de usar `ddl-auto=create` que recrearia la BD en cada arranque, usamos `ddl-auto=validate` para que Hibernate verifique que el esquema ya existe, y Flyway aplica los scripts SQL versionados (V1__, V2__, etc.) en orden para crear y modificar las tablas de forma controlada."
