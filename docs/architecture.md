# Architettura del progetto

Panoramica veloce

- Applicazione Spring Boot a struttura classica `src/main/java` con package `com.example.dashboardapi`.
- Moduli principali: `config`, `controller`, `dto`, `model`, `repository`, `security`.

Layer principali

- Controller: ricevono richieste HTTP e rispondono con `ResponseEntity` o DTO. Esempio: `AuthController`.
- Service (non presenti in tutti i casi): layer consigliato per logica di business. Separa controller e repository.
- Repository: estendono `JpaRepository` per l'accesso al DB.
- Model/Entity: classi JPA mappate alle tabelle del DB.
- DTO: oggetti di trasferimento usati per esporre dati senza esportare le entity direttamente.

Convenzioni adottate

- Non esporre password o campi sensibili via DTO.
- Usare `GetResponse<T>` come wrapper per tutte le risposte GET.
- Usare `BaseRestController` per helper CRUD (create/update/delete).

Flusso di una richiesta (es. GET `/users`)

1. DispatcherServlet instrada la richiesta al controller.
2. Controller invoca repository (o service) per ottenere dati.
3. Controller mappa entity -> DTO e ritorna `GetResponse<List<UserDto>>`.
4. Jackson serializza il DTO in JSON per la risposta.

Note su persistenza

- Le migration SQL sono in `src/main/resources/db/migration`.
- Il progetto usa Spring Data JPA con Hibernate come provider.

File importanti

- `DashboardApiApplication.java` — entrypoint Spring Boot.
- `SecurityConfig.java`, `JwtFilter.java`, `JwtUtil.java` — gestione sicurezza e JWT.
- `UserRepository.java` — repository JPA per `User`.
