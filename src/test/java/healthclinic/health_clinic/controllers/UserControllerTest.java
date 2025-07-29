package healthclinic.health_clinic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import healthclinic.health_clinic.Enums.Role;
import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private User initialUser;

        @BeforeEach
        void setUp() {
                User user = new User();
                user.setUsername("user1");
                user.setPassword("password");
                user.setRole(Role.ROLE_ADMIN);
                initialUser = userRepository.save(user);
        }

        @Test
        void getUsers() throws Exception {
                mockMvc.perform(get("/api/users"))
                                .andExpect(status().isOk());
        }

        @Test
        void createUserSuccess() throws Exception {
                CreateUserRequest request = new CreateUserRequest("user2", "password");

                mockMvc.perform(
                                post("/api/users")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.username").value("user2"));
        }

        @Test
        void createUserErrorUniqueUsername() throws Exception {
                CreateUserRequest request = new CreateUserRequest("user1", "password");

                mockMvc.perform(
                                post("/api/users")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof IllegalArgumentException))
                                .andExpect(result -> assertEquals("Username user1 is already taken.",
                                                result.getResolvedException().getMessage()));
        }

        @Test
        void updateUserSuccess() throws Exception {
                CreateUserRequest request = new CreateUserRequest("user-updated", "new-password");

                mockMvc.perform(
                                put("/api/users/" + initialUser.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.username").value("user-updated"));

                User updatedUser = userRepository.findById(initialUser.getId()).orElseThrow();
                assertEquals("user-updated", updatedUser.getUsername());
        }

        @Test
        void updateUserErrorUsernameExists() throws Exception {
                User anotherUser = new User();
                anotherUser.setUsername("existing_user");
                anotherUser.setPassword("password");
                anotherUser.setRole(Role.ROLE_PATIENT);
                userRepository.save(anotherUser);

                CreateUserRequest request = new CreateUserRequest("existing_user", "password");

                mockMvc.perform(
                                put("/api/users/" + initialUser.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deleteUserSuccess() throws Exception {
                mockMvc.perform(
                                delete("/api/users/" + initialUser.getId()))
                                .andExpect(status().isNoContent());

                assertFalse(userRepository.existsById(initialUser.getId()));
        }

        @Test
        void deleteUserErrorNotFound() throws Exception {
                mockMvc.perform(
                                delete("/api/users/" + UUID.randomUUID()))
                                .andExpect(status().isNotFound());
        }
}
