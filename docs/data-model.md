# Modello dati

Entità principali

- `User` — rappresenta l'utente del sistema. Campi tipici: `id`, `email`, `password`, `role`.

Repository

- `UserRepository extends JpaRepository<User, Long>` — fornisce metodi CRUD e possibilità di definire query custom (`findByUsername`, ecc.).

Migration

- Le migration SQL sono in `src/main/resources/db/migration` e vengono eseguite all'avvio per creare tabelle e colonne.

Consigli su entity e mapping

- Evita relazioni EAGER non necessarie per non degradare le performance.
- Usa tipi wrapper (`Long`, `Integer`) per gli ID per supportare `null` quando necessario.

Esempio `User` (semplificato)

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role;
    // getters/setters
}
```
