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
import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreateMedicalHistoryRequest;
import healthclinic.health_clinic.models.MedicalHistory;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.MedicalHistoryRepository;
import healthclinic.health_clinic.repository.PatientRepository;
import healthclinic.health_clinic.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.hamcrest.Matchers;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MedicalHistoryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private PatientRepository patientRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private MedicalHistoryRepository medicalHistoryRepository;

        private CreateMedicalHistoryRequest medicalRequest;

        @BeforeEach
        void setUp() {
                LocalDate dateOfBirth = LocalDate.of(2004, 11, 27);

                Address address = new Address();
                address.setCity("Kota");
                address.setPostalCode("16221");
                address.setStreet("Jalan");

                User user = new User();
                user.setUsername("user1");
                user.setPassword("password");
                user.setRole(Role.ROLE_PATIENT);
                User savedUser = userRepository.save(user);

                Patient patient = new Patient();
                patient.setAddress(address);
                patient.setAge(20);
                patient.setBloodType("O");
                patient.setDateOfBirth(dateOfBirth);
                patient.setFullName("Pasien 1");
                patient.setGender("Pria");
                patient.setHeight(200);
                patient.setJob("Guru");
                patient.setNik("1234567890123456");
                patient.setPhone("123456789");
                patient.setPlaceOfBirth("Bogor");
                patient.setUser(savedUser);
                patient.setWeight(90);
                Patient savedPatient = patientRepository.save(patient);

                medicalRequest = new CreateMedicalHistoryRequest();
                medicalRequest.setAnamnesis("Demam");
                medicalRequest.setBodyCheck("Normal");
                medicalRequest.setDiagnose("Demam biasa");
                medicalRequest.setTherapy("Obat paracetamol");
                medicalRequest.setPatientId(savedPatient.getId());
        }

        @Test
        void getMedicalHistories() throws Exception {
                mockMvc.perform(
                                get("/api/medical-histories"))
                                .andExpectAll(
                                                status().isOk(),
                                                content().string(Matchers.containsString("[]")));
        }

        @Test
        void createMedicalHistorySuccess() throws Exception {
                mockMvc.perform(
                                post("/api/medical-histories")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)))
                                .andExpectAll(
                                                status().isCreated(),
                                                jsonPath("$.data.anamnesis").value(medicalRequest.getAnamnesis()));
        }

        @Test
        void createMedicalHistoryError() throws Exception {
                medicalRequest.setAnamnesis("");
                mockMvc.perform(
                                post("/api/medical-histories")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors.anamnesis",
                                                                Matchers.containsInAnyOrder(
                                                                                "Panjang anamnesis harus diantara 3 dan 1024 karakter",
                                                                                "Anamnesis harus diisi")));
        }

        @Test
        void updateMedicalHistorySuccess() throws Exception {
                mockMvc.perform(
                                post("/api/medical-histories")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)));

                MedicalHistory medicalHistory = medicalHistoryRepository.findAll().get(0);

                medicalRequest.setAnamnesis("Geuring Panas");
                medicalRequest.setBodyCheck("Sehat Wal'afiat");
                medicalRequest.setTherapy("Urut");

                mockMvc.perform(
                                put("/api/medical-histories/" + medicalHistory.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)))
                                .andExpectAll(
                                                status().isOk(),
                                                jsonPath("$.data.anamnesis").value("Geuring Panas"));
        }

        @Test
        void updateMedicalHistoryError() throws Exception {
                mockMvc.perform(
                                post("/api/medical-histories")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)));

                MedicalHistory medicalHistory = medicalHistoryRepository.findAll().get(0);

                medicalRequest.setAnamnesis("");

                mockMvc.perform(
                                put("/api/medical-histories/" + medicalHistory.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                jsonPath("$.errors.anamnesis",
                                                                Matchers.containsInAnyOrder(
                                                                                "Panjang anamnesis harus diantara 3 dan 1024 karakter",
                                                                                "Anamnesis harus diisi")));
        }

        @Test
        void deleteMedicalHistorySuccess() throws Exception {
                mockMvc.perform(
                                post("/api/medical-histories")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)));

                MedicalHistory medicalHistory = medicalHistoryRepository.findAll().get(0);

                mockMvc.perform(
                                delete("/api/medical-histories/" + medicalHistory.getId()))
                                .andExpect(status().isNoContent());
        }

        @Test
        void deleteMedicalHistoryError() throws Exception {
                mockMvc.perform(
                                delete("/api/medical-histories/" + UUID.randomUUID()))
                                .andExpect(status().isNotFound());
        }

}
