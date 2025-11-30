# Controller e Rotte

I controller gestiscono l'interazione HTTP: ricevono richieste, delegano la business logic (idealmente a servizi) e ritornano risposte.

Struttura tipica

- `@RestController` su classi che espongono rotte REST.
- `@RequestMapping("/base")` per il prefisso comune delle rotte.
- Usare `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` per i metodi.

`BaseRestController`

- Il progetto include `BaseRestController` con helper protetti per `create`, `update`, `delete` e varianti che ritornano DTO (`createWithResponse`, `updateWithResponse`, `deleteWithResponse`).
- Questi helper riducono boilerplate ma non sostituiscono la logica di mapping/validazione che resta responsabilità del controller.

Esempio: `AuthController` (estratto)

```java
@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) { ... }

    @GetMapping("/user/{username}")
    public GetResponse<UserDto> getUser(@PathVariable String username) { ... }
}
```

Best practice per i controller

- Non inserire logica complessa: utilizza `@Service` per la logica di business.
- Validazione input: usa `@Valid` e DTO di request per validare i campi.
- Mappatura: mappa `Entity` -> `Dto` prima di restituire i dati.

Esempio completo con `BaseRestController` helpers

```java
@RestController
@RequestMapping("/users")
public class UserController extends BaseRestController {
    @Autowired
    private UserRepository userRepo;

    @PostMapping
    public ResponseEntity<PostResponse<UserDto>> createUser(@RequestBody CreateUserRequest req) {
        User entity = mapFromReq(req);
        return createWithResponse(entity, userRepo);
    }
}
```
