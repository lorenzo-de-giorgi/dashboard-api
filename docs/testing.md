# Testing e Best Practices

Tipi di test consigliati

- Unit test (JUnit): testare logica pura dei servizi e helper.
- Integration test (Spring Boot Test + TestRestTemplate o MockMvc): testare controller e flusso di request/response.

Esempio rapido MockMvc

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
  @Autowired MockMvc mvc;

  @Test
  void getUser_returns200() throws Exception {
    mvc.perform(get("/auth/user/testuser")).andExpect(status().isOk());
  }
}
```

Best practices

- Isolare la logica nei servizi per facilitarne il test.
- Usare profili Spring (`application-test.yml`) per DB in memoria durante i test.
- Testare sia i casi di successo che i casi di errore (404, 400, 401).

Continuous Integration

- Aggiungi una pipeline CI che esegue `mvnw.cmd -DskipTests=false test` su commit/pull request.
