package healthclinic.health_clinic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import healthclinic.health_clinic.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(
                get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser() throws Exception {
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "user 2")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(Matchers.containsString(
                                "User with username user 2 successfully created")));
    }

}
