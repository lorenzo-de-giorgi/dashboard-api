# Dashboard API

Progetto Spring Boot: API minimale per gestione utenti e autenticazione JWT.

Questo repository contiene:

- Codice sorgente Spring Boot in `src/main/java/com/example/dashboardapi`
- Configurazioni e migration SQL in `src/main/resources`
- Documentazione dettagliata in `docs/` (usa la `SUMMARY.md` per navigare)

---

## Quick start

Prerequisiti: Java 17+ (qui testato con Java 25), Maven.

1. Configura il DB in `src/main/resources/application.yml` o `application.properties`.
2. Costruisci/avvia l'applicazione:

```cmd
mvnw.cmd -DskipTests clean package
mvnw.cmd -DskipTests spring-boot:run
```

L'API sarà disponibile su `http://localhost:8080`.

---

## Prefisso API e routing

- Tutte le rotte definite dai controller vengono automaticamente prefissate con `/api/v1`, tranne le eccezioni seguenti:
  - rotte che iniziano già con `/auth` (es. `POST /auth/login`, `POST /auth/register`)
  - rotte che iniziano già con `/api`
- La regola è implementata in `src/main/java/com/example/dashboardapi/config/ApiPrefixConfig.java`.

Se desideri cambiare il prefisso o escludere altri percorsi, modifica `ApiPrefixConfig`.

---

## Endpoint principali (stato attuale)

- `POST /auth/register` — registra un nuovo utente.

  - Body: `LoginRequest` (username, password)
  - Risposta: `201 Created` (entità salvata)

- `POST /auth/login` — login

  - Body: `LoginRequest`
  - Risposta: `200 OK` con `AuthResponse` contenente il token JWT

- `GET /api/v1/users/{username}` — informazioni pubbliche utente
  - Risposta: `GetResponse<UserDto>` (wrapper uniforme per GET)

Nota: altri controller che creerai riceveranno automaticamente il prefisso `/api/v1` a meno che il mapping inizi già con `/auth` o `/api`.

---

## Wrapper di risposta

Per uniformare le risposte l'app usa alcuni DTO wrapper:

- `GetResponse<T>` — per tutte le risposte GET; contiene `success`, `data`, `message`, `timestamp`, `resultsSize`.
- `PostResponse<T>` — per risposte POST (creazione).
- `UpdateResponse<T>` — per risposte PUT (aggiornamento).
- `DeleteResponse` — per risposte DELETE.

Questi DTO si trovano in `src/main/java/com/example/dashboardapi/dto/`.

---

## Come aggiungere una nuova rotta / controller

1. Crea i DTO di request/response e gli entity necessari.
2. Aggiungi un controller sotto `com.example.dashboardapi.controller` e annotalo con `@RestController`.
3. Definisci le route come `@GetMapping`, `@PostMapping`, ecc. Non è necessario anteporre `/api/v1`.
4. Usa `BaseRestController` per helper CRUD se opportuno.

Esempio rapido:

```java
@RestController
@RequestMapping("/projects") // verrà esposto come /api/v1/projects
public class ProjectController extends BaseRestController {
    // ...
}
```

---

## Note Java e concetti utili

Per dettagli tecnici su Java (generics, Optional, Stream API, annotazioni Spring), consulta `docs/java-basics.md`.

Brevi note:

- Preferisci constructor injection nei controller/servizi per testabilità.
- Usa DTO per non esporre campi sensibili (es. password).
- Centralizza la logica di business nei servizi (`@Service`).

---

## Documentazione completa

La documentazione completa è sotto la cartella `docs/`. File principali:

- `docs/index.md` — introduzione
- `docs/setup.md` — setup e comandi
- `docs/architecture.md` — architettura (include la sezione sul prefisso `/api/v1`)
- `docs/BaseRestController.md` — dettagli sul base controller e esempi
- `docs/dtos.md` — spiegazione dei DTO wrapper
- `docs/api-reference.md` — elenco degli endpoint attuali e esempi
- `docs/java-basics.md` — appendice Java

Puoi aprire `docs/SUMMARY.md` per una vista ad albero della documentazione.

---

## Test

Esegui i test con:

```cmd
mvnw.cmd test
```

Per test di integrazione usa `@SpringBootTest` + `MockMvc` o `TestRestTemplate`.

---

Se vuoi, posso generare una documentazione HTML (MkDocs) con tema e server di preview, o aggiungere esempi di controller completi e test di integrazione.
