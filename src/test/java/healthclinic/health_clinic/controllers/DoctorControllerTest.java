package healthclinic.health_clinic.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreateDoctorRequest;
import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.models.Doctor;
import healthclinic.health_clinic.repository.DoctorRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DoctorControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private DoctorRepository doctorRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private CreateDoctorRequest doctorRequest;
        private CreateUserRequest userRequest;

        @BeforeEach
        void setUp() {
                LocalDate dateOfBirth = LocalDate.of(2004, 11, 27);

                Address address = new Address();
                address.setCity("Kota");
                address.setPostalCode("16221");
                address.setStreet("Jalan");

                userRequest = new CreateUserRequest();
                userRequest.setUsername("user1");
                userRequest.setPassword("password");

                doctorRequest = new CreateDoctorRequest();
                doctorRequest.setAddress(address);
                doctorRequest.setAge(20);
                doctorRequest.setDateOfBirth(dateOfBirth);
                doctorRequest.setFullName("Pasien 1");
                doctorRequest.setGender("MALE");
                doctorRequest.setSip("123/abc/232/2023");
                doctorRequest.setPhone("123456789");
                doctorRequest.setPlaceOfBirth("Bogor");
                doctorRequest.setUser(userRequest);
        }

        @Test
        void getDoctors() throws Exception {
                mockMvc.perform(get("/api/doctors"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString("[]")));
        }

        @Test
        void getDoctorByIdSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                Doctor doctor = doctorRepository.findAll().get(0);
                mockMvc.perform(
                                get("/api/doctors/" + doctor.getId()))
                                .andExpectAll(status().isOk(),
                                                jsonPath("$.data.id",
                                                                Matchers.containsString(doctor.getId().toString())));
        }

        @Test
        void getDoctorByIdErrorNotFound() throws Exception {
                mockMvc.perform(
                                get("/api/doctors/" + UUID.randomUUID()))
                                .andExpectAll(status().isNotFound());
        }

        @Test
        void createDoctorsuccess() throws Exception {
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)))
                                .andExpectAll(
                                                status().isCreated(),
                                                jsonPath("$.data.fullName").value(doctorRequest.getFullName()));
        }

        @Test
        void createDoctorErrorLength() throws Exception {
                doctorRequest.setSip("");

                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors.sip", Matchers.containsInAnyOrder("SIP harus diisi",
                                                                "Panjang SIP harus 16 digit")));
        }

        @Test
        void createDoctorErrorUnique() throws Exception {
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors['user.username']")
                                                                .value("Username is already taken."),
                                                jsonPath("$.errors['phone']").value("Phone is already taken."),
                                                jsonPath("$.errors['sip']").value("SIP is already taken."));
        }

        @Test
        void updateDoctorsuccess() throws Exception {
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                Doctor doctor = doctorRepository.findBySipEquals(doctorRequest.getSip()).orElse(null);
                String fullName = "Miftah Update";
                doctorRequest.setFullName(fullName);

                mockMvc.perform(
                                put("/api/doctors/" + doctor.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.data.fullName").value(fullName));
        }

        @Test
        void updateDoctorErrorEmpty() throws Exception {
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                Doctor doctor = doctorRepository.findBySipEquals(doctorRequest.getSip()).orElse(null);
                String fullName = "";
                doctorRequest.setFullName(fullName);

                mockMvc.perform(
                                put("/api/doctors/" + doctor.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors['fullName']").value(Matchers.containsInAnyOrder(
                                                                "Nama lengkap harus diisi",
                                                                "Panjang nama lengkap harus minimal 3 karakter")));
        }

        @Test
        void updateDoctorErrorUnique() throws Exception {
                // Doctor 1
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                // Doctor 2
                userRequest.setUsername("doctor2");
                doctorRequest.setSip("111/aaa/111/1234");
                doctorRequest.setPhone("111111111");
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                Doctor doctor = doctorRepository.findBySipEquals(doctorRequest.getSip()).orElse(null); // get doctor 2
                doctorRequest.setSip("123/abc/232/2023");
                doctorRequest.setPhone("123456789");
                userRequest.setUsername("user1");

                mockMvc.perform(
                                put("/api/doctors/" + doctor.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors['user.username']")
                                                                .value("Username is already taken."),
                                                jsonPath("$.errors['sip']").value("SIP is already taken."),
                                                jsonPath("$.errors['phone']").value("Phone is already taken."));
        }

        @Test
        void deleteDoctorsuccess() throws Exception {
                mockMvc.perform(
                                post("/api/doctors")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(doctorRequest)));

                Doctor doctor = doctorRepository.findBySipEquals(doctorRequest.getSip()).orElse(null);

                mockMvc.perform(
                                delete("/api/doctors/" + doctor.getId()))
                                .andExpectAll(
                                                status().isNoContent());
        }

        @Test
        void deleteDoctorError() throws Exception {
                mockMvc.perform(
                                delete("/api/doctors/" + UUID.randomUUID())).andExpect(status().isNotFound());
        }
}
