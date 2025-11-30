**BaseRestController — Documentazione Dettagliata**

Questo documento spiega come usare la classe `BaseRestController` presente nel progetto, come definire le rotte nei controller che la estendono, come creare risposte unificate per le chiamate GET con `GetResponse<T>`, e alcune buone pratiche.

**Scopo**: fornire helper riutilizzabili per le operazioni CRUD comuni (create, update, delete) riducendo boilerplate nei singoli controller.

**Posizione**: `src/main/java/com/example/dashboardapi/controller/BaseRestController.java`

**Contenuto principale della classe**

- **create(entity, repository)**: salva l'entità e restituisce `ResponseEntity<S>` con status `201 Created` e body l'entità salvata.
- **update(id, entity, repository)**: verifica l'esistenza tramite `repository.existsById(id)`, se non esiste restituisce `404 Not Found`, altrimenti salva e ritorna `200 OK` con l'entità aggiornata.
- **delete(id, repository)**: verifica l'esistenza e se esiste esegue `repository.deleteById(id)` e ritorna `204 No Content`; altrimenti `404 Not Found`.

**Quando estendere `BaseRestController`**

- Estendi `BaseRestController` in controller che espongono operazioni CRUD dirette su un repository JPA.
- Non vincola la gestione delle rotte: i metodi pubblici del controller restano i tuoi endpoint normali. `BaseRestController` fornisce solo helper protetti da chiamare all'interno dei metodi annotati.

Esempio: `UserController` (bozza)

```java
package com.example.dashboardapi.controller;

import com.example.dashboardapi.model.User;
import com.example.dashboardapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController extends BaseRestController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User u) {
        return create(u, userRepo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User u) {
        u.setId(id);
        return update(id, u, userRepo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return delete(id, userRepo);
    }

    @GetMapping
    public com.example.dashboardapi.dto.GetResponse<List<com.example.dashboardapi.dto.UserDto>> listUsers() {
        List<com.example.dashboardapi.dto.UserDto> list = userRepo.findAll().stream()
            .map(u -> new com.example.dashboardapi.dto.UserDto(u.getId(), u.getUsername(), u.getRole()))
            .collect(Collectors.toList());
        return new com.example.dashboardapi.dto.GetResponse<>(true, list, "OK");
    }
}
```

**Definizione delle rotte**

- Le rotte si definiscono normalmente con le annotazioni `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` sul controller concreto che estende `BaseRestController`.
- `BaseRestController` non impone convenzioni sulle rotte; adotta le convenzioni REST che preferisci (es. `/resources`, `/resources/{id}`).

**Uso del `GetResponse<T>` per risposte GET unificate**

- `GetResponse<T>` è il DTO usato per normalizzare le risposte GET. Contiene i campi principali:
  - **`success`**: boolean
  - **`data`**: `T` (oggetto singolo o lista)
  - **`message`**: stringa di descrizione (es. "OK", messaggi di errore)
  - **`timestamp`**: valore in millisecondi
  - **`resultsSize`**: numero di elementi (0 per `null`, size per collection, 1 per oggetto singolo non-null)

Esempio di endpoint GET che restituisce una lista:

```java
@GetMapping
public GetResponse<List<UserDto>> listUsers() {
    List<UserDto> dto = repo.findAll().stream()
        .map(u -> new UserDto(...))
        .collect(Collectors.toList());
    return new GetResponse<>(true, dto, "OK");
}
```

Esempio di endpoint GET che restituisce singolo oggetto:

```java
@GetMapping("/{id}")
public GetResponse<UserDto> getUser(@PathVariable Long id) {
    UserDto dto = repo.findById(id)
        .map(u -> new UserDto(u.getId(), u.getUsername(), u.getRole()))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    return new GetResponse<>(true, dto, "OK");
}
```

Nota: `resultsSize` viene popolato automaticamente dal costruttore di `GetResponse` (0/size/1). Se hai necessità particolari puoi sovrascriverlo con `setResultsSize(...)`.

**Codifica degli HTTP status**

- `create(...)` ritorna `201 Created` con il body dell'entità salvata.
- `update(...)` ritorna `200 OK` se l'aggiornamento avviene, `404 Not Found` se l'id non esiste.
- `delete(...)` ritorna `204 No Content` se l'entity esiste ed è cancellata, `404 Not Found` altrimenti.
- Per gli endpoint GET stai restituendo sempre `200 OK` (col body `GetResponse`). Per errori usa `ResponseStatusException` o gestori `@ExceptionHandler` per trasformare eccezioni in risposte coerenti.

Esempio: restituire `GetResponse` ma al contempo mandare `404` se non trovato

```java
@GetMapping("/{username}")
public ResponseEntity<GetResponse<UserDto>> getByUsername(@PathVariable String username) {
    UserDto dto = repo.findByUsername(username)
        .map(u -> new UserDto(u.getId(), u.getUsername(), u.getRole()))
        .orElse(null);
    if (dto == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new GetResponse<>(false, null, "User not found"));
    }
    return ResponseEntity.ok(new GetResponse<>(true, dto, "OK"));
}
```

oppure lanciare `ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")` se preferisci un approccio exception-first.

**Validazione e mapping DTO**

- Evita di esporre direttamente le entity JPA nei JSON di risposta. Usa DTO (es. `UserDto`) per non esportare campi sensibili come `password`.
- Esegui la validazione nei metodi `@PostMapping` e `@PutMapping` usando `@Valid` e `BindingResult` o `MethodValidation`.

Esempio di create con validazione:

```java
@PostMapping
public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest req) {
    User entity = mapFromReq(req);
    User saved = userRepo.save(entity);
    UserDto dto = mapToDto(saved);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
}
```

**Error handling consigliato**

- Centralizza la gestione delle eccezioni in una classe `@ControllerAdvice` per trasformare eccezioni in risposte coerenti con `GetResponse` quando appropriato.
- Esempio di handler che restituisce `GetResponse` per eccezioni custom:

```java
@ControllerAdvice
public class RestExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<GetResponse<Object>> handleNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(new GetResponse<>(false, null, ex.getMessage()));
  }
}
```

**Best practices e suggerimenti**

- Non mettere logica business complessa nei controller; usa servizi (`@Service`) e chiama i servizi dai controller.
- Usa DTO per input/output e mapparli con librerie come MapStruct se la mappatura diventa complessa.
- Metti l'annotazione `@Transactional` dove serve (tipicamente sui metodi di servizio, non sui controller).
- Se molte risposte GET devono avere lo stesso wrapper, considera un `ResponseEntity<GetResponse<T>>` come contratto API e documentalo nello Swagger/OpenAPI.

**Esempi di richieste (curl)**

- Lista utenti:

```cmd
curl -s http://localhost:8080/users | jq
```

- Singolo utente:

```cmd
curl -s http://localhost:8080/users/1 | jq
```

**Testing rapido**

- Compila e avvia l'applicazione:

```cmd
mvnw.cmd -DskipTests spring-boot:run
```

- Esegui chiamate agli endpoint e verifica che `resultsSize` venga popolato per risposte con liste.

**Estensioni possibili**

- Aggiungere helper per patch parziale (`PATCH`) che applicano aggiornamenti parziali su entity/DTO.
- Implementare parametri comuni per paginazione/sort e mappare `GetResponse.data` su paged DTO (es. `Page<T>`). In quel caso, `resultsSize` dovrebbe rappresentare i totali o la size della pagina a seconda della politica.

---

Se vuoi, posso:

- aggiungere esempi concreti di `UserController` e test di integrazione che verificano `resultsSize`;
- creare l'`OpenAPI`/Swagger esempio che documenta il wrapper `GetResponse`.

Fammi sapere quale di queste opzioni preferisci e procedo.

**Nuove risposte standard per POST / PUT / DELETE**

Ho aggiunto tre DTO standard per uniformare le risposte delle operazioni di modifica:

- `PostResponse<T>`: wrapper per le risposte alle chiamate `POST` (creazione). Campi: `success`, `data`, `message`, `timestamp`.
- `UpdateResponse<T>`: wrapper per le risposte alle chiamate `PUT` (aggiornamento). Campi: `success`, `data`, `message`, `timestamp`.
- `DeleteResponse`: wrapper per le risposte alle chiamate `DELETE`. Campi: `success`, `id`, `message`, `timestamp`.

Questi DTO si trovano in: `src/main/java/com/example/dashboardapi/dto/`.

Ho inoltre aggiunto metodi helper opzionali in `BaseRestController` che ritornano questi DTO invece dei semplici `ResponseEntity` con entità grezze:

- `createWithResponse(entity, repository)` -> `ResponseEntity<PostResponse<S>>` con `201 Created`.
- `updateWithResponse(id, entity, repository)` -> `ResponseEntity<UpdateResponse<S>>` con `200 OK` o `404 Not Found` e body `UpdateResponse` che contiene `success=false`.
- `deleteWithResponse(id, repository)` -> `ResponseEntity<DeleteResponse>` con `200 OK` (body `DeleteResponse`) o `404 Not Found`.

Esempi d'uso nel controller (bozza)

```java
@RestController
@RequestMapping("/users")
public class UserController extends BaseRestController {
    @Autowired
    private UserRepository userRepo;

    @PostMapping
    public ResponseEntity<com.example.dashboardapi.dto.PostResponse<UserDto>> createUser(@RequestBody CreateUserRequest req) {
        User entity = mapFromReq(req);
        // salva e restituisce PostResponse con body e 201
        return createWithResponse(entity, userRepo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.example.dashboardapi.dto.UpdateResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        User entity = mapFromReq(req);
        entity.setId(id);
        // restituisce UpdateResponse con 200 o 404
        return updateWithResponse(id, entity, userRepo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<com.example.dashboardapi.dto.DeleteResponse> deleteUser(@PathVariable Long id) {
        // restituisce DeleteResponse con 200 (deleted) o 404 (not found)
        return deleteWithResponse(id, userRepo);
    }
}
```

Note sul `DELETE`: tecnicamente `204 No Content` è lo status più corretto quando non si restituisce body; tuttavia se desideri fornire conferma strutturata (es. `id` cancellato e `timestamp`), `200 OK` con `DeleteResponse` è più comodo per i client. La helper `deleteWithResponse` usa `200 OK` con body per comodità; il metodo `delete(...)` originale (che restituisce `204 No Content`) è ancora disponibile se preferisci usarlo.

Serializzazione e compatibilità

- Questi wrapper sono POJO semplici — funzionano con Jackson (configurazione Spring Boot predefinita). Assicurati che i DTO dei dati (`UserDto`, ecc.) siano serializzabili correttamente (getters standard).

Linee guida sull'adozione

- Se vuoi uniformare tutte le risposte CRUD nell'API, usa i metodi `createWithResponse`/`updateWithResponse`/`deleteWithResponse` e restituisci `GetResponse` per tutte le GET.
- Se preferisci mantenere gli standard puri REST (es. `201` con `Location` header, `204` per delete), puoi combinare: usa `createWithResponse` per il body e aggiungi l'header `Location` manualmente quando ti serve.

Esempio: `POST` che imposta header `Location`

```java
@PostMapping
public ResponseEntity<PostResponse<UserDto>> createUser(@RequestBody CreateUserRequest req, UriComponentsBuilder uriBuilder) {
    User entity = mapFromReq(req);
    ResponseEntity<PostResponse<User>> resp = createWithResponse(entity, userRepo);
    User saved = resp.getBody().getData();
    URI location = uriBuilder.path("/users/{id}").buildAndExpand(saved.getId()).toUri();
    return ResponseEntity.created(location).body(new PostResponse<>(true, mapToDto(saved), "Created"));
}
```

Se vuoi, aggiungo anche:

- test di integrazione JUnit che verificano i wrapper di risposta;
- esempi OpenAPI/Swagger che dichiarano i wrapper per tutti gli endpoint.
