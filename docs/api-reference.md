# API Reference

Elenco degli endpoint principali forniti dal progetto (stato attuale):

AuthController

- `POST /auth/register` — registra un nuovo utente. Body: `LoginRequest` (username, password). Restituisce `201` e l'entità salvata.
- `POST /auth/login` — effettua il login. Body: `LoginRequest`. Restituisce `AuthResponse` con il token JWT.
- `GET /auth/user/{username}` — restituisce informazioni pubbliche sull'utente (`GetResponse<UserDto>`). Se non trovato -> `404`.

Esempi di risposta

- `GET /auth/user/{username}` (200):

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
