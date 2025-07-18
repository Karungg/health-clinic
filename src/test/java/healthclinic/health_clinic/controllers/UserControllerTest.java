package healthclinic.health_clinic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        private void addUser(String username, String password) throws Exception {
                mockMvc.perform(
                                post("/api/users")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .param("username", username)
                                                .param("password", password));
        }

        @Test
        void getUsers() throws Exception {
                mockMvc.perform(
                                get("/api/users"))
                                .andExpect(status().isOk());
        }

        @Test
        void createUserSuccess() throws Exception {
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

        @Test
        void createUserErrorUniqueUsername() throws Exception {
                addUser("user 2", "password");

                mockMvc.perform(
                                post("/api/users")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .param("username", "user 2")
                                                .param("password", "password"))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof IllegalArgumentException))
                                .andExpect(result -> assertEquals("Username user 2 is already taken.",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void updateUserSuccess() throws Exception {
                addUser("user 3", "password");

                User user = userRepository.findByUsername("user 3").orElse(null);

                mockMvc.perform(
                                put("/api/users/" + user.getId() + "/edit")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .param("username", "user 4")
                                                .param("password", "password"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString(
                                                "User with username user 4 successfully updated")));

                user = userRepository.findByUsername("user 4").orElse(null);
                assertNotNull(user);
        }

        @Test
        void updateUserError() throws Exception {
                addUser("user 1", "password");
                addUser("user 3", "password");

                User user = userRepository.findByUsername("user 3").orElse(null);

                mockMvc.perform(
                                put("/api/users/" + user.getId() + "/edit")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .param("username", "user 1")
                                                .param("password", "password"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(Matchers.containsString(
                                                "Username user 1 is already taken.")));
        }

        @Test
        void deleteUserSuccess() throws Exception {
                addUser("user 1", "password");

                User user = userRepository.findByUsername("user 1").orElse(null);

                mockMvc.perform(
                                delete("/api/users/" + user.getId() + "/delete"))
                                .andExpect(status().isNoContent());

                user = userRepository.findByUsername("user 1").orElse(null);
                assertNull(user);
        }

        @Test
        void deleteUserError() throws Exception {
                mockMvc.perform(
                                delete("/api/users/1f1d1dae-b6fd-48ed-a0ec-0c21b97a222e/delete")) // wrong uuid
                                .andExpect(status().isNotFound());
        }
}
