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

        @BeforeEach
        void setUp() {
                LocalDate dateOfBirth = LocalDate.of(2004, 11, 27);

                Address address = new Address();
                address.setCity("Kota");
                address.setPostalCode("16221");
                address.setStreet("Jalan");

                CreateUserRequest user = new CreateUserRequest();
                user.setUsername("user1");
                user.setPassword("password");

                doctorRequest = new CreateDoctorRequest();
                doctorRequest.setAddress(address);
                doctorRequest.setAge(20);
                doctorRequest.setDateOfBirth(dateOfBirth);
                doctorRequest.setFullName("Pasien 1");
                doctorRequest.setGender("MALE");
                doctorRequest.setSip("123/abc/232/2023");
                doctorRequest.setPhone("123456789");
                doctorRequest.setPlaceOfBirth("Bogor");
                doctorRequest.setUser(user);
        }

        @Test
        void getDoctors() throws Exception {
                mockMvc.perform(get("/api/doctors"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString("[]")));
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
        void createDoctorError() throws Exception {
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
}
