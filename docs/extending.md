# Aggiungere Nuove Rotte / Estendere l'API

Questa guida mostra come aggiungere un nuovo controller e nuove rotte, seguendo le convenzioni del progetto.

1. Definire DTO di request/response

- Crea DTO specifici per input (`CreateXRequest`, `UpdateXRequest`) e output (`XDto`). Evita di usare direttamente l'entity.

2. Creare il controller

- Crea una classe nel package `controller` annotata con `@RestController` e `@RequestMapping("/risorsa")`.
- Se applicabile, estendi `BaseRestController` per usare gli helper CRUD.

Esempio completo: aggiungere `ProjectController`

```java
@RestController
@RequestMapping("/projects")
public class ProjectController extends BaseRestController {

    @Autowired
    private ProjectRepository repo;

    @PostMapping
    public ResponseEntity<PostResponse<ProjectDto>> create(@RequestBody CreateProjectRequest req) {
        Project p = mapFromReq(req);
        return createWithResponse(p, repo);
    }

    @GetMapping
    public GetResponse<List<ProjectDto>> list() {
        List<ProjectDto> list = repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
        return new GetResponse<>(true, list, "OK");
    }
}
```

3. Validazione

- Aggiungi annotazioni JSR-380 (`@NotNull`, `@Size`, ecc.) sui DTO di request e usa `@Valid` nei metodi controller.

4. Mapping

- Implementa metodi `mapFromReq`, `mapToDto` o usa MapStruct per automatizzare la mappatura.

5. Documentare l'endpoint

- Aggiungi l'endpoint nella sezione `API Reference` (vedi `api-reference.md`).

6. Testare

- Aggiungi test unitari/integrazione (MockMvc o TestRestTemplate) per verificare il comportamento del controller.

Linee guida

- Mantieni i controller semplici: orchestrazione e validazione, non logica di business complessa.
- Usa i helper `BaseRestController` con giudizio: se devi fare controlli pre-save, fai la verifica nel controller o nel service prima di chiamare l'helper.
