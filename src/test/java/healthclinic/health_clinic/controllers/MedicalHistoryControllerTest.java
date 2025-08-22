package healthclinic.health_clinic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        private Patient savedPatient;

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
                savedPatient = patientRepository.save(patient);

                medicalRequest = buildValidMedicalRequest();
        }

        private CreateMedicalHistoryRequest buildValidMedicalRequest() {
                CreateMedicalHistoryRequest request = new CreateMedicalHistoryRequest();
                request.setAnamnesis("Demam");
                request.setBodyCheck("Normal");
                request.setDiagnose("Demam biasa");
                request.setTherapy("Obat paracetamol");
                request.setPatientId(savedPatient.getId());
                return request;
        }

        private MedicalHistory createMedicalHistory() throws Exception {
                mockMvc.perform(
                                post("/api/medical-histories")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(medicalRequest)));
                return medicalHistoryRepository.findAll().get(0);
        }

        @Nested
        @DisplayName("Get Medical Histories")
        class GetMedicalHistories {

                @Test
                @DisplayName("Should return empty list when no medical histories exist")
                void shouldReturnEmptyListWhenNoMedicalHistoriesExist() throws Exception {
                        mockMvc.perform(get("/api/medical-histories"))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        content().string(Matchers.containsString("[]")));
                }

                @Test
                @DisplayName("Should return medical histories when they exist")
                void shouldReturnMedicalHistoriesWhenTheyExist() throws Exception {
                        // Create medical history first
                        createMedicalHistory();

                        mockMvc.perform(get("/api/medical-histories"))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data").isArray(),
                                                        jsonPath("$.data", hasSize(1)),
                                                        jsonPath("$.data[0].anamnesis").value("Demam"));
                }
        }

        @Nested
        @DisplayName("Create Medical History")
        class CreateMedicalHistory {

                @Test
                @DisplayName("Should create medical history successfully with valid data")
                void shouldCreateMedicalHistorySuccessfully() throws Exception {
                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.anamnesis")
                                                                        .value(medicalRequest.getAnamnesis()),
                                                        jsonPath("$.data.bodyCheck")
                                                                        .value(medicalRequest.getBodyCheck()),
                                                        jsonPath("$.data.diagnose").value(medicalRequest.getDiagnose()),
                                                        jsonPath("$.data.therapy").value(medicalRequest.getTherapy()));
                }

                @Test
                @DisplayName("Should return validation error when anamnesis is empty")
                void shouldReturnValidationErrorWhenAnamnesisIsEmpty() throws Exception {
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
                @DisplayName("Should return validation error when anamnesis is too short")
                void shouldReturnValidationErrorWhenAnamnesisIsTooShort() throws Exception {
                        medicalRequest.setAnamnesis("Hi");

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.anamnesis",
                                                                        Matchers.contains(
                                                                                        "Panjang anamnesis harus diantara 3 dan 1024 karakter")));
                }

                @Test
                @DisplayName("Should return validation error when anamnesis is too long")
                void shouldReturnValidationErrorWhenAnamnesisIsTooLong() throws Exception {
                        medicalRequest.setAnamnesis("a".repeat(1025));

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.anamnesis",
                                                                        Matchers.contains(
                                                                                        "Panjang anamnesis harus diantara 3 dan 1024 karakter")));
                }

                @Test
                @DisplayName("Should return error when patient does not exist")
                void shouldReturnErrorWhenPatientDoesNotExist() throws Exception {
                        UUID randomUuid = UUID.randomUUID();
                        medicalRequest.setPatientId(randomUuid);

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isNotFound(),
                                                        jsonPath("$.errors").value("Patient id with " + randomUuid
                                                                        + " not found"));
                }

                @Test
                @DisplayName("Should return error when request body is malformed")
                void shouldReturnErrorWhenRequestBodyIsMalformed() throws Exception {
                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content("{ invalid json }"))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("Should return error when all required fields are missing")
                void shouldReturnErrorWhenAllRequiredFieldsAreMissing() throws Exception {
                        CreateMedicalHistoryRequest emptyRequest = new CreateMedicalHistoryRequest();

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(emptyRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors").exists());
                }
        }

        @Nested
        @DisplayName("Update Medical History")
        class UpdateMedicalHistory {

                @Test
                @DisplayName("Should update medical history successfully")
                void shouldUpdateMedicalHistorySuccessfully() throws Exception {
                        MedicalHistory medicalHistory = createMedicalHistory();

                        medicalRequest.setAnamnesis("Geuring Panas");
                        medicalRequest.setBodyCheck("Sehat Wal'afiat");
                        medicalRequest.setTherapy("Urut");

                        mockMvc.perform(
                                        put("/api/medical-histories/" + medicalHistory.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.anamnesis").value("Geuring Panas"),
                                                        jsonPath("$.data.bodyCheck").value("Sehat Wal'afiat"),
                                                        jsonPath("$.data.therapy").value("Urut"));
                }

                @Test
                @DisplayName("Should return validation error when updating with empty anamnesis")
                void shouldReturnValidationErrorWhenUpdatingWithEmptyAnamnesis() throws Exception {
                        MedicalHistory medicalHistory = createMedicalHistory();
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
                @DisplayName("Should return not found when updating non-existent medical history")
                void shouldReturnNotFoundWhenUpdatingNonExistentMedicalHistory() throws Exception {
                        mockMvc.perform(
                                        put("/api/medical-histories/" + UUID.randomUUID())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
        @DisplayName("Delete Medical History")
        class DeleteMedicalHistory {

                @Test
                @DisplayName("Should delete medical history successfully")
                void shouldDeleteMedicalHistorySuccessfully() throws Exception {
                        MedicalHistory medicalHistory = createMedicalHistory();

                        mockMvc.perform(
                                        delete("/api/medical-histories/" + medicalHistory.getId()))
                                        .andExpect(status().isNoContent());

                        // Verify deletion
                        assertTrue(medicalHistoryRepository.findById(medicalHistory.getId()).isEmpty());
                }

                @Test
                @DisplayName("Should return not found when deleting non-existent medical history")
                void shouldReturnNotFoundWhenDeletingNonExistentMedicalHistory() throws Exception {
                        mockMvc.perform(
                                        delete("/api/medical-histories/" + UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request when deleting with invalid UUID format")
                void shouldReturnBadRequestWhenDeletingWithInvalidUuidFormat() throws Exception {
                        mockMvc.perform(
                                        delete("/api/medical-histories/invalid-uuid"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Get Medical History By ID")
        class GetMedicalHistoryById {

                @Test
                @DisplayName("Should return medical history when it exists")
                void shouldReturnMedicalHistoryWhenItExists() throws Exception {
                        MedicalHistory medicalHistory = createMedicalHistory();

                        mockMvc.perform(
                                        get("/api/medical-histories/" + medicalHistory.getId()))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.id").value(medicalHistory.getId().toString()),
                                                        jsonPath("$.data.anamnesis").value("Demam"),
                                                        jsonPath("$.data.bodyCheck").value("Normal"));
                }

                @Test
                @DisplayName("Should return not found when medical history does not exist")
                void shouldReturnNotFoundWhenMedicalHistoryDoesNotExist() throws Exception {
                        mockMvc.perform(
                                        get("/api/medical-histories/" + UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request when ID format is invalid")
                void shouldReturnBadRequestWhenIdFormatIsInvalid() throws Exception {
                        mockMvc.perform(
                                        get("/api/medical-histories/invalid-uuid"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Content Type and Headers")
        class ContentTypeAndHeaders {

                @Test
                @DisplayName("Should return unsupported media type when content type is not JSON")
                void shouldReturnUnsupportedMediaTypeWhenContentTypeIsNotJson() throws Exception {
                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                                                        .content("plain text content"))
                                        .andExpect(status().isUnsupportedMediaType());
                }

                @Test
                @DisplayName("Should handle missing content type gracefully")
                void shouldHandleMissingContentTypeGracefully() throws Exception {
                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpect(status().isUnsupportedMediaType());
                }
        }

        @Nested
        @DisplayName("Edge Cases and Boundary Testing")
        class EdgeCasesAndBoundaryTesting {

                @Test
                @DisplayName("Should handle maximum valid anamnesis length")
                void shouldHandleMaximumValidAnamnesisLength() throws Exception {
                        medicalRequest.setAnamnesis("a".repeat(1024));

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpect(status().isCreated());
                }

                @Test
                @DisplayName("Should handle minimum valid anamnesis length")
                void shouldHandleMinimumValidAnamnesisLength() throws Exception {
                        medicalRequest.setAnamnesis("abc");

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpect(status().isCreated());
                }

                @Test
                @DisplayName("Should handle special characters in anamnesis")
                void shouldHandleSpecialCharactersInAnamnesis() throws Exception {
                        medicalRequest.setAnamnesis("Pasien mengalami demam tinggi (>39°C), menggigil, & sakit kepala");

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.anamnesis")
                                                                        .value(medicalRequest.getAnamnesis()));
                }

                @Test
                @DisplayName("Should handle Unicode characters in anamnesis")
                void shouldHandleUnicodeCharactersInAnamnesis() throws Exception {
                        medicalRequest.setAnamnesis("患者发烧头痛咳嗽症状明显需要进一步检查治疗方案");

                        mockMvc.perform(
                                        post("/api/medical-histories")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(medicalRequest)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.anamnesis")
                                                                        .value(medicalRequest.getAnamnesis()));
                }
        }
}
