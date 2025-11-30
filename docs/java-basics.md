# Appendice: Nozioni Java utili (per il progetto)

Questa appendice spiega concetti Java che appaiono frequentemente nel codice del progetto.

Generics

- Sintassi: `class Box<T> { private T value; }`
- Nei repository/DTO: `GetResponse<T>` permette di riusare lo stesso wrapper per liste o oggetti singoli.
- Vantaggi: sicurezza di tipo a compile-time e minor duplicazione di codice.

Optional

- `Optional<T>` è un contenitore che può essere vuoto o contenere un valore.
- Uso tipico: `repo.findById(id).map(...).orElseThrow(...)` per gestire il caso 'not found' in modo funzionale.

Stream API

- Permette di processare collezioni in modo dichiarativo: `list.stream().map(...).collect(Collectors.toList())`.
- Utile per trasformare entity -> DTO.

Annotazioni Spring comuni

- `@RestController` — controller REST; combina `@Controller` + `@ResponseBody`.
- `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` — mappature delle rotte HTTP.
- `@Autowired` — inietta automaticamente i bean Spring (alternativa consigliata: constructor injection).
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue` — annotazioni JPA per le entity.

ResponseEntity

- `ResponseEntity<T>` rappresenta una risposta HTTP completa (status, headers, body).
- Esempio: `ResponseEntity.status(HttpStatus.CREATED).body(saved)`.

Tip: preferisci constructor injection

```java
private final UserRepository repo;
public UserController(UserRepository repo) { this.repo = repo; }
```

Vantaggi: facilita il testing e rende il bean `final`.
