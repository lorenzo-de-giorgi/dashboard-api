# Setup e Avvio

Questa sezione descrive come preparare l'ambiente di sviluppo e come avviare l'applicazione.

Prerequisiti

- Java 17+ (qui il progetto è stato avviato con Java 25 nella tua macchina; verifica la versione installata).
- Maven (o usa il wrapper `mvnw.cmd`).
- Database PostgreSQL (configurazione in `src/main/resources/application.yml` o `application.properties`).

Configurazione ambiente

1. Copia il file di configurazione `src/main/resources/application.yml` o modifica `application.properties` con le tue credenziali DB.
2. Assicurati che il DB sia raggiungibile e che lo schema sia inizializzato (liquibase/flyway o SQL in `src/main/resources/db/migration`).

Comandi utili

- Compilare il progetto:

```cmd
mvnw.cmd -DskipTests clean package
```

- Avviare l'app in sviluppo:

```cmd
mvnw.cmd -DskipTests spring-boot:run
```

- Eseguire i test (se presenti):

```cmd
mvnw.cmd test
```

Azioni post-avvio

- L'API gira di default su `http://localhost:8080`.
- Verifica gli endpoint con `curl`, Postman o il browser.

Nota sui logs

- Se vedi messaggi su `spring.jpa.open-in-view` o `HikariPool`, sono informazioni normali; presta attenzione a eventuali errori `ERROR` o `Exception` in fase di avvio.
