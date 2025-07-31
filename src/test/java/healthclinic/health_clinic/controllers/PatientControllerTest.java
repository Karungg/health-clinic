package healthclinic.health_clinic.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.repository.PatientRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PatientRepository patientRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private CreatePatientRequest patientRequest;

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

                patientRequest = new CreatePatientRequest();
                patientRequest.setAddress(address);
                patientRequest.setAge(20);
                patientRequest.setBloodType("O");
                patientRequest.setDateOfBirth(dateOfBirth);
                patientRequest.setFullName("Pasien 1");
                patientRequest.setGender("Pria");
                patientRequest.setHeight(200);
                patientRequest.setJob("Guru");
                patientRequest.setNik("1234567890123456");
                patientRequest.setPhone("123456789");
                patientRequest.setPlaceOfBirth("Bogor");
                patientRequest.setUser(user);
                patientRequest.setWeight(90);
        }

        @Test
        void getPatients() throws Exception {
                mockMvc.perform(get("/api/patients"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString("[]")));
        }

        @Test
        void createPatientSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(patientRequest)))
                                .andExpectAll(
                                                status().isCreated(),
                                                jsonPath("$.data.fullName").value(patientRequest.getFullName()));
        }

        @Test
        void createPatientError() throws Exception {
                patientRequest.setNik("");

                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(patientRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors.nik", Matchers.containsInAnyOrder("NIK harus diisi",
                                                                "Panjang NIK harus 16 digit",
                                                                "NIK hanya boleh berisi angka")));
        }

        @Test
        void updatePatientSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(patientRequest)));

                Patient patient = patientRepository.findByFullNameEquals(patientRequest.getFullName()).orElse(null);
                String fullName = "Miftah Update";
                patientRequest.setFullName(fullName);

                mockMvc.perform(
                                put("/api/patients/" + patient.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(patientRequest)))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.data.fullName").value(fullName));
        }

        @Test
        void deletePatientSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(patientRequest)));

                Patient patient = patientRepository.findByFullNameEquals(patientRequest.getFullName()).orElse(null);

                mockMvc.perform(
                                delete("/api/patients/" + patient.getId()))
                                .andExpectAll(
                                                status().isNoContent());
        }
}
