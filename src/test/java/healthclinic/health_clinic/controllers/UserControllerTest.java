package healthclinic.health_clinic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @BeforeEach
        void setUp() {
                User initialUser = new User();
                initialUser.setUsername("user 1");
                initialUser.setPassword("password");
                userRepository.save(initialUser);
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
                mockMvc.perform(
                                post("/api/users")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .param("username", "user 1")
                                                .param("password", "password"))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof IllegalArgumentException))
                                .andExpect(result -> assertEquals("Username user 1 is already taken.",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void updateUserSuccess() throws Exception {
                User user = userRepository.findByUsernameEquals("user 1").orElse(null);

                mockMvc.perform(
                                put("/api/users/" + user.getId() + "/edit")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .param("username", "user 4")
                                                .param("password", "password"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString(
                                                "User with username user 4 successfully updated")));

                user = userRepository.findByUsernameEquals("user 4").orElse(null);
                assertNotNull(user);
        }

        @Test
        void updateUserError() throws Exception {
                User newUser = new User();
                newUser.setUsername("user 3");
                newUser.setPassword("password");
                userRepository.save(newUser);

                User user = userRepository.findByUsernameEquals("user 3").orElse(null);

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
                User user = userRepository.findByUsernameEquals("user 1").orElse(null);

                mockMvc.perform(
                                delete("/api/users/" + user.getId() + "/delete"))
                                .andExpect(status().isNoContent());

                user = userRepository.findByUsernameEquals("user 1").orElse(null);
                assertNull(user);
        }

        @Test
        void deleteUserError() throws Exception {
                mockMvc.perform(
                                delete("/api/users/1f1d1dae-b6fd-48ed-a0ec-0c21b97a222e/delete")) // wrong uuid
                                .andExpect(status().isNotFound());
        }
}
