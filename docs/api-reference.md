# API Reference

Elenco degli endpoint principali forniti dal progetto (stato attuale):

AuthController

- `POST /auth/register` — registra un nuovo utente. Body: `LoginRequest` (username, password). Restituisce `201` e l'entità salvata.
- `POST /auth/login` — effettua il login. Body: `LoginRequest`. Restituisce `AuthResponse` con il token JWT.

UsersController

- `GET /api/v1/users/{username}` — restituisce informazioni pubbliche sull'utente (`GetResponse<UserDto>`). Se non trovato -> `404`.

ProtectedController

- `GET /api/v1/protected/test` — endpoint di test protetto tramite JWT; richiede header `Authorization: Bearer <token>` e restituisce il nome dell'utente autenticato in `data`.

Esempio di chiamata protetta (curl):

```cmd
# prima ottieni il token
curl -X POST -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"password\":\"pass\"}" http://localhost:8080/auth/login

# poi chiama l'endpoint protetto
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/v1/protected/test
```

Esempi di risposta

-- `GET /api/v1/users/{username}` (200):

```json
{
  "success": true,
  "data": { "id": 1, "username": "mario", "role": "ROLE_USER" },
  "message": "OK",
  "timestamp": 1700000000000,
  "resultsSize": 1
}
```

Suggerimento

- Quando aggiungi nuovi controller, aggiorna questa pagina con le rotte, i body richiesti e gli esempi di risposta.
