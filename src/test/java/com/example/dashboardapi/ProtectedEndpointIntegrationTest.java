package com.example.dashboardapi;

import com.example.dashboardapi.model.User;
import com.example.dashboardapi.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProtectedEndpointIntegrationTest {

    MockMvc mvc;

    @Autowired
    org.springframework.web.context.WebApplicationContext wac;

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setup() {
        this.mvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup(this.wac).build();
        userRepo.deleteAll();
        User u = new User();
        u.setUsername("testuser");
        u.setPassword(encoder.encode("pass"));
        u.setRole("ROLE_USER");
        userRepo.save(u);
    }

    @Test
    void protectedEndpoint_requiresJwt_and_returnsAuthenticatedUser() throws Exception {
        // login
        String loginJson = "{\"username\":\"testuser\",\"password\":\"pass\"}";
        String resp = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode node = mapper.readTree(resp);
        String token = node.get("token").asText();
        assertThat(token).isNotBlank();

        // call protected endpoint with token
        String prot = mvc.perform(get("/api/v1/protected/test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode r2 = mapper.readTree(prot);
        assertThat(r2.get("success").asBoolean()).isTrue();
        assertThat(r2.get("data").asText()).contains("testuser");
    }
}
