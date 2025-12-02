**API Endpoints**

Questo documento elenca gli endpoint esposti dall'applicazione, il metodo HTTP, il percorso effettivo, se richiedono autenticazione e i DTO principali usati.

**Nota sul prefisso**: l'app applica un prefisso API configurabile (di default `/api/v1`) tramite `ApiPrefixFilter`. Le rotte che iniziano con `/auth` o `/api` sono escluse dal prefisso. Il prefisso è configurabile tramite `application.yml` con la property `api.prefix`.

**Elenco endpoint attivi**

- **Register**

  - Metodo: `POST`
  - Path: `/auth/register`
  - Autenticazione: NO
  - Body: `LoginRequest` (JSON) — `{ "username": "...", "password": "..." }`
  - Response: `201/200` vuoto (salvataggio utente)

- **Login**

  - Metodo: `POST`
  - Path: `/auth/login`
  - Autenticazione: NO
  - Body: `LoginRequest` (JSON)
  - Response: `AuthResponse` — `{ "token": "<jwt>" }`

- **Get user**

  - Metodo: `GET`
  - Path: `/api/v1/users/{username}`
  - Autenticazione: SÌ (endpoint protetto da JWT). Accesso consentito solo a `ROLE_ADMIN` o all'utente stesso (es. `username` == token subject).
  - Response: `GetResponse<UserDto>`
    - Esempio:
      `{ "success": true, "data": { "id": 1, "username": "alice", "role": "ROLE_USER" }, "message": "OK", "timestamp": 1700000000000, "resultsSize": 1 }`

- **Protected test**
  - Metodo: `GET`
  - Path: `/api/v1/protected/test`
  - Autenticazione: SÌ (richiede header `Authorization: Bearer <token>`)
  - Response: `GetResponse<String>` con messaggio contenente l'username autenticato

**Configurazione prefisso**

- `src/main/resources/application.yml` — proprietà rilevanti:

```yaml
api:
  prefix: /api/v1
  exclusions: /auth,/api,/swagger,/actuator
```

La property `api.prefix` definisce il prefisso aggiunto dinamicamente alle richieste dal filtro `ApiPrefixFilter` quando la rotta non è esclusa. Modifica `api.exclusions` per escludere altre rotte dal prefisso.

**Formato di risposta GET unificato (`GetResponse<T>`)**

Campi principali:

- `success`: boolean — indica successo logico
- `data`: payload (oggetto o lista)
- `message`: stringa descrittiva
- `timestamp`: data/ora (epoch)
- `resultsSize`: numero di elementi restituiti (0, 1, o dimensione della collection)

Usare `GetResponse<T>` per tutti gli endpoint GET fornisce un formato coerente per client e documentazione.

**Sicurezza**

- Le rotte sotto `/api/v1/**` sono protette via JWT (configurazione in `SecurityConfig`).
- Le rotte `/auth/**` rimangono pubbliche per permmettere registrazione e login.

**Uso di `BaseRestController`**

- Per i controller che espongono create/update/delete usare `BaseRestController` come helper per risposte uniformi (`PostResponse`, `UpdateResponse`, `DeleteResponse`).

**Esempi rapidi**

- Login (curl):

```bash
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"alice","password":"pwd"}'
```

- Chiamata protetta (curl):

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/v1/protected/test
```

---

Se vuoi, posso:

- Aggiungere esempi di request/response completi per ciascun endpoint.
- Generare un file OpenAPI/Swagger minimal con questi endpoint.
- Aggiornare i controller per usare `@RequestMapping("/api/v1")` esplicito invece del filtro.
