# Sicurezza e JWT

Il progetto utilizza JSON Web Token (JWT) per autenticare le chiamate protette.

Componenti principali

- `JwtUtil` — helper per generare e validare token JWT.
- `JwtFilter` — filtro che intercetta le richieste HTTP, estrae il token dall'header `Authorization: Bearer <token>` e verifica l'identità.
- `DatabaseUserDetailsService` — implementa `UserDetailsService` per caricare gli utenti dal DB.
- `SecurityConfig` — configura le regole di sicurezza (ciechi, endpoints pubblici, filtri, password encoder).

Flusso di autenticazione

1. Client invia `POST /auth/login` con username/password.
2. `AuthController` autentica usando `AuthenticationManager` e, se valida, genera un token con `JwtUtil.generateToken(username)`.
3. Il client salva il token e lo include nelle richieste successive nell'header `Authorization`.
4. `JwtFilter` valida il token e popola il `SecurityContext` per permettere l'accesso alle risorse protette.

Buone pratiche

- Proteggi le chiavi segrete usate per firmare JWT (non committarle in git, usare variabili d'ambiente o secret manager).
- Imposta una scadenza ragionevole per i token e supporta il refresh token se necessario.
- Hash delle password: usa `PasswordEncoder` (già presente nel progetto).

Esempio: header di richiesta

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...<token>
```
