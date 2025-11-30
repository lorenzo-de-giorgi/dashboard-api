# DTO e Risposte Unificate

Questa sezione descrive i DTO usati nel progetto per normalizzare le risposte e separare i modelli di persistenza dalle rappresentazioni JSON esposte all'API.

DTO principali presenti

- `GetResponse<T>` — wrapper per tutte le risposte GET. Contiene: `success`, `data`, `message`, `timestamp`, `resultsSize`.
- `PostResponse<T>` — wrapper per le risposte POST (creazione).
- `UpdateResponse<T>` — wrapper per le risposte PUT (aggiornamento).
- `DeleteResponse` — wrapper per le risposte DELETE.
- `UserDto` — DTO che espone i dati pubblici dell'utente (`id`, `username`, `role`).

Perché usare DTO

- Separano la rappresentazione pubblica dalle entità JPA (es. non inviare `password`).
- Consentono di cambiare i field esposti senza modificare gli entity.

Esempio: creare una risposta GET uniforme

```java
List<UserDto> dtos = repo.findAll().stream()
    .map(u -> new UserDto(u.getId(), u.getUsername(), u.getRole()))
    .collect(Collectors.toList());
return new GetResponse<>(true, dtos, "OK");
```

Nota su `resultsSize`

- `GetResponse` calcola `resultsSize` automaticamente: `0` per `null`, la size se `data` è una `Collection`, `1` per oggetto singolo.

Serializzazione

- I DTO sono POJO con getters/setters: Jackson (configurazione Spring Boot) serializza automaticamente.
