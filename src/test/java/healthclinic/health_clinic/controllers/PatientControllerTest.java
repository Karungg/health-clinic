package healthclinic.health_clinic.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.hamcrest.Matchers;
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

import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.repository.PatientRepository;
import healthclinic.health_clinic.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PatientRepository patientRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private CreatePatientRequest patientRequest;

        @BeforeEach
        void setUp() {
                patientRequest = buildValidPatientRequest();
        }

        private CreatePatientRequest buildValidPatientRequest() {
                LocalDate dateOfBirth = LocalDate.of(2004, 11, 27);

                Address address = new Address();
                address.setCity("Kota");
                address.setPostalCode("16221");
                address.setStreet("Jalan");

                CreateUserRequest user = new CreateUserRequest();
                user.setUsername("user1");
                user.setPassword("password");

                CreatePatientRequest request = new CreatePatientRequest();
                request.setAddress(address);
                request.setAge(20);
                request.setBloodType("AB-");
                request.setDateOfBirth(dateOfBirth);
                request.setFullName("Pasien 1");
                request.setGender("MALE");
                request.setHeight(200);
                request.setJob("Guru");
                request.setNik("1234567890123456");
                request.setPhone("123456789");
                request.setPlaceOfBirth("Bogor");
                request.setUser(user);
                request.setWeight(90);
                return request;
        }

        private Patient createPatient() throws Exception {
                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(patientRequest)));

                return patientRepository.findByFullNameEquals(patientRequest.getFullName()).orElse(null);
        }

        @Nested
        @DisplayName("Get All Patients")
        class GetAllPatients {

                @Test
                @DisplayName("Should return empty list when no patients exist")
                void shouldReturnEmptyListWhenNoPatientsExist() throws Exception {
                        mockMvc.perform(get("/api/patients"))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data").isArray(),
                                                        jsonPath("$.data", hasSize(0)));
                }

                @Test
                @DisplayName("Should return all patients when they exist")
                void shouldReturnAllPatientsWhenTheyExist() throws Exception {
                        createPatient();

                        mockMvc.perform(get("/api/patients"))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data").isArray(),
                                                        jsonPath("$.data", hasSize(1)),
                                                        jsonPath("$.data[0].fullName").value("Pasien 1"));
                }
        }

        @Nested
        @DisplayName("Get Patient By ID")
        class GetPatientById {

                @Test
                @DisplayName("Should return patient when it exists")
                void shouldReturnPatientWhenItExists() throws Exception {
                        Patient patient = createPatient();

                        mockMvc.perform(get("/api/patients/" + patient.getId()))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.id").value(patient.getId().toString()),
                                                        jsonPath("$.data.fullName").value("Pasien 1"),
                                                        jsonPath("$.data.nik").value("1234567890123456"),
                                                        jsonPath("$.data.bloodType").value("AB-"),
                                                        jsonPath("$.data.gender").value("MALE"));
                }

                @Test
                @DisplayName("Should return not found when patient does not exist")
                void shouldReturnNotFoundWhenPatientDoesNotExist() throws Exception {
                        mockMvc.perform(get("/api/patients/" + UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request for invalid UUID format")
                void shouldReturnBadRequestForInvalidUuidFormat() throws Exception {
                        mockMvc.perform(get("/api/patients/invalid-uuid"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Create Patient")
        class CreatePatient {

                @Test
                @DisplayName("Should create patient successfully with valid data")
                void shouldCreatePatientSuccessfully() throws Exception {
                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.fullName").value(patientRequest.getFullName()),
                                                        jsonPath("$.data.nik").value(patientRequest.getNik()),
                                                        jsonPath("$.data.bloodType")
                                                                        .value(patientRequest.getBloodType()),
                                                        jsonPath("$.data.gender").value(patientRequest.getGender()),
                                                        jsonPath("$.data.age").value(patientRequest.getAge()),
                                                        jsonPath("$.data.height").value(patientRequest.getHeight()),
                                                        jsonPath("$.data.weight").value(patientRequest.getWeight()),
                                                        jsonPath("$.data.job").value(patientRequest.getJob()),
                                                        jsonPath("$.data.phone").value(patientRequest.getPhone()),
                                                        jsonPath("$.data.placeOfBirth")
                                                                        .value(patientRequest.getPlaceOfBirth()));
                }

                @Test
                @DisplayName("Should return validation error when NIK is empty")
                void shouldReturnValidationErrorWhenNikIsEmpty() throws Exception {
                        patientRequest.setNik("");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik", Matchers.containsInAnyOrder(
                                                                        "NIK harus diisi",
                                                                        "Panjang NIK harus 16 digit",
                                                                        "NIK hanya boleh berisi angka")));
                }

                @Test
                @DisplayName("Should return validation error when NIK is null")
                void shouldReturnValidationErrorWhenNikIsNull() throws Exception {
                        patientRequest.setNik(null);

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik").exists());
                }

                @Test
                @DisplayName("Should return validation error when NIK has invalid length")
                void shouldReturnValidationErrorWhenNikHasInvalidLength() throws Exception {
                        patientRequest.setNik("12345"); // Less than 16 digits

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik", Matchers
                                                                        .contains("Panjang NIK harus 16 digit")));
                }

                @Test
                @DisplayName("Should return validation error when NIK contains non-numeric characters")
                void shouldReturnValidationErrorWhenNikContainsNonNumericCharacters() throws Exception {
                        patientRequest.setNik("123456789012345A");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik", Matchers
                                                                        .contains("NIK hanya boleh berisi angka")));
                }

                @Test
                @DisplayName("Should return validation error when full name is empty")
                void shouldReturnValidationErrorWhenFullNameIsEmpty() throws Exception {
                        patientRequest.setFullName("");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.fullName", Matchers.containsInAnyOrder(
                                                                        "Panjang nama lengkap harus minimal 3 karakter",
                                                                        "Nama lengkap harus diisi")));
                }

                @Test
                @DisplayName("Should return validation error when phone is empty")
                void shouldReturnValidationErrorWhenPhoneIsEmpty() throws Exception {
                        patientRequest.setPhone("");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.phone", Matchers.containsInAnyOrder(
                                                                        "Panjang nomor telepon harus diantara 9 dan 14 digit",
                                                                        "Nomor telepon harus diisi")));
                }

                @Test
                @DisplayName("Should return validation error when age is invalid")
                void shouldReturnValidationErrorWhenAgeIsInvalid() throws Exception {
                        patientRequest.setAge(-1);

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.age")
                                                                        .value("Umur harus diantara 1 dan 999"));
                }

                @Test
                @DisplayName("Should return validation error when height is invalid")
                void shouldReturnValidationErrorWhenHeightIsInvalid() throws Exception {
                        patientRequest.setHeight(0);

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.height")
                                                                        .value("Tinggi badan harus diantara 1 dan 999"));
                }

                @Test
                @DisplayName("Should return validation error when weight is invalid")
                void shouldReturnValidationErrorWhenWeightIsInvalid() throws Exception {
                        patientRequest.setWeight(0);

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.weight")
                                                                        .value("Berat badan harus diantara 1 dan 999"));
                }

                @Test
                @DisplayName("Should return validation error when gender is invalid")
                void shouldReturnValidationErrorWhenGenderIsInvalid() throws Exception {
                        patientRequest.setGender("dsad");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.gender")
                                                                        .value("Jenis kelamin harus diantara : MALE, FEMALE"));
                }

                @Test
                @DisplayName("Should return validation error when blood type is invalid")
                void shouldReturnValidationErrorWhenBloodTypeIsInvalid() throws Exception {
                        patientRequest.setBloodType("Z+");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.bloodType")
                                                                        .value("Golongan darah harus diantara : A+, A-, B+, B-, AB+, AB-, O+, O-"));
                }

                @Test
                @DisplayName("Should return validation error when date of birth is in the future")
                void shouldReturnValidationErrorWhenDateOfBirthIsInTheFuture() throws Exception {
                        patientRequest.setDateOfBirth(LocalDate.now().plusDays(1));

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.dateOfBirth").value(
                                                                        "Tanggal lahir harus dimasa lalu"));
                }

                @Test
                @DisplayName("Should return error when NIK already exists")
                void shouldReturnErrorWhenNikAlreadyExists() throws Exception {
                        createPatient();

                        // Try to create another patient with same NIK
                        patientRequest.setFullName("Different Name");
                        patientRequest.getUser().setUsername("differentuser");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik").value("NIK is already taken."));
                }

                @Test
                @DisplayName("Should return error when phone already exists")
                void shouldReturnErrorWhenPhoneAlreadyExists() throws Exception {
                        createPatient();

                        // Try to create another patient with same phone
                        patientRequest.setFullName("Different Name");
                        patientRequest.setNik("9876543210123456");
                        patientRequest.getUser().setUsername("differentuser");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.phone").value("Phone is already taken."));
                }

                @Test
                @DisplayName("Should return error when username already exists")
                void shouldReturnErrorWhenUsernameAlreadyExists() throws Exception {
                        createPatient();

                        // Try to create another patient with same username
                        patientRequest.setFullName("Different Name");
                        patientRequest.setNik("9876543210123456");
                        patientRequest.setPhone("987654321");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors['user.username']")
                                                                        .value("Username is already taken."));
                }

                @Test
                @DisplayName("Should return error when request body is malformed")
                void shouldReturnErrorWhenRequestBodyIsMalformed() throws Exception {
                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content("{ invalid json }"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Update Patient")
        class UpdatePatient {

                @Test
                @DisplayName("Should update patient successfully")
                void shouldUpdatePatientSuccessfully() throws Exception {
                        Patient patient = createPatient();

                        String updatedName = "Miftah Update";
                        patientRequest.setFullName(updatedName);
                        patientRequest.setAge(25);
                        patientRequest.setWeight(85);

                        mockMvc.perform(
                                        put("/api/patients/" + patient.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.fullName").value(updatedName),
                                                        jsonPath("$.data.age").value(25),
                                                        jsonPath("$.data.weight").value(85));
                }

                @Test
                @DisplayName("Should return validation error when updating with invalid data")
                void shouldReturnValidationErrorWhenUpdatingWithInvalidData() throws Exception {
                        Patient patient = createPatient();
                        patientRequest.setNik(""); // Invalid NIK

                        mockMvc.perform(
                                        put("/api/patients/" + patient.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik").exists());
                }

                @Test
                @DisplayName("Should return not found when updating non-existent patient")
                void shouldReturnNotFoundWhenUpdatingNonExistentPatient() throws Exception {
                        mockMvc.perform(
                                        put("/api/patients/" + UUID.randomUUID())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request when updating with invalid UUID")
                void shouldReturnBadRequestWhenUpdatingWithInvalidUuid() throws Exception {
                        mockMvc.perform(
                                        put("/api/patients/invalid-uuid")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("Should update patient address successfully")
                void shouldUpdatePatientAddressSuccessfully() throws Exception {
                        Patient patient = createPatient();

                        Address newAddress = new Address();
                        newAddress.setCity("Jakarta");
                        newAddress.setPostalCode("12345");
                        newAddress.setStreet("Jalan Baru");
                        patientRequest.setAddress(newAddress);

                        mockMvc.perform(
                                        put("/api/patients/" + patient.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.address.city").value("Jakarta"),
                                                        jsonPath("$.data.address.postalCode").value("12345"),
                                                        jsonPath("$.data.address.street").value("Jalan Baru"));
                }

                @Test
                @DisplayName("Should prevent updating to existing NIK")
                void shouldPreventUpdatingToExistingNik() throws Exception {
                        Patient patient1 = createPatient();

                        // Create second patient
                        patientRequest.setFullName("Patient 2");
                        patientRequest.setNik("9876543210123456");
                        patientRequest.getUser().setUsername("user2");
                        patientRequest.setPhone("987654321");
                        Patient patient2 = createPatient();

                        // Try to update patient2 with patient1's NIK
                        patientRequest.setNik(patient1.getNik());

                        mockMvc.perform(
                                        put("/api/patients/" + patient2.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.nik").value("NIK is already taken."));
                }
        }

        @Nested
        @DisplayName("Delete Patient")
        class DeletePatient {

                @Test
                @DisplayName("Should delete patient successfully")
                void shouldDeletePatientSuccessfully() throws Exception {
                        Patient patient = createPatient();

                        mockMvc.perform(delete("/api/patients/" + patient.getId()))
                                        .andExpect(status().isNoContent());

                        // Verify deletion
                        assertTrue(patientRepository.findById(patient.getId()).isEmpty());
                }

                @Test
                @DisplayName("Should return not found when deleting non-existent patient")
                void shouldReturnNotFoundWhenDeletingNonExistentPatient() throws Exception {
                        mockMvc.perform(delete("/api/patients/" + UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request when deleting with invalid UUID format")
                void shouldReturnBadRequestWhenDeletingWithInvalidUuidFormat() throws Exception {
                        mockMvc.perform(delete("/api/patients/invalid-uuid"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Blood Types and Medical Data")
        class BloodTypesAndMedicalData {

                @Test
                @DisplayName("Should accept all valid blood types")
                void shouldAcceptAllValidBloodTypes() throws Exception {
                        String[] validBloodTypes = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };

                        for (int i = 0; i < validBloodTypes.length; i++) {
                                patientRequest.setFullName("Patient " + validBloodTypes[i]);
                                patientRequest.setNik("1234567890123"
                                                + ThreadLocalRandom.current().nextInt(100, 999));
                                patientRequest.getUser().setUsername(
                                                "user" + validBloodTypes[i]);
                                patientRequest.setPhone("12345678" + i);
                                patientRequest.setBloodType(validBloodTypes[i]);

                                mockMvc.perform(
                                                post("/api/patients")
                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                .content(objectMapper
                                                                                .writeValueAsString(patientRequest)))
                                                .andExpectAll(
                                                                status().isCreated(),
                                                                jsonPath("$.data.bloodType").value(validBloodTypes[i]));
                        }
                }

                @Test
                @DisplayName("Should accept both MALE and FEMALE genders")
                void shouldAcceptBothMaleAndFemaleGenders() throws Exception {
                        String[] validGenders = { "MALE", "FEMALE" };

                        for (String gender : validGenders) {
                                patientRequest.setFullName("Patient " + gender);
                                patientRequest.setNik("123456789012345" + (gender.equals("MALE") ? "1" : "2"));
                                patientRequest.getUser().setUsername("user" + gender.toLowerCase());
                                patientRequest.setPhone("12345678" + (gender.equals("MALE") ? "01" : "02"));
                                patientRequest.setGender(gender);

                                mockMvc.perform(
                                                post("/api/patients")
                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                .content(objectMapper
                                                                                .writeValueAsString(patientRequest)))
                                                .andExpectAll(
                                                                status().isCreated(),
                                                                jsonPath("$.data.gender").value(gender));
                        }
                }
        }

        @Nested
        @DisplayName("Address Validation")
        class AddressValidation {

                @Test
                @DisplayName("Should create patient with complete address")
                void shouldCreatePatientWithCompleteAddress() throws Exception {
                        Address address = new Address();
                        address.setStreet("Jl. Merdeka No. 123");
                        address.setCity("Jakarta Pusat");
                        address.setPostalCode("10110");
                        patientRequest.setAddress(address);

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.address.street").value("Jl. Merdeka No. 123"),
                                                        jsonPath("$.data.address.city").value("Jakarta Pusat"),
                                                        jsonPath("$.data.address.postalCode").value("10110"));
                }

                @Test
                @DisplayName("Should return validation error when address is null")
                void shouldReturnValidationErrorWhenAddressIsNull() throws Exception {
                        patientRequest.setAddress(null);

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.address").value("Alamat harus diisi"));
                }

                @Test
                @DisplayName("Should return validation error when postal code is invalid")
                void shouldReturnValidationErrorWhenPostalCodeIsInvalid() throws Exception {
                        Address address = patientRequest.getAddress();
                        address.setPostalCode("12"); // Invalid postal code format

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors['address.postalCode']",
                                                                        Matchers.containsInAnyOrder(
                                                                                        "Panjang kode pos harus diantara 3 dan 255 karakter")));
                }
        }

        @Nested
        @DisplayName("User Account Integration")
        class UserAccountIntegration {

                @Test
                @DisplayName("Should create user account when creating patient")
                void shouldCreateUserAccountWhenCreatingPatient() throws Exception {
                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpect(status().isCreated());

                        // Verify user was created
                        assertTrue(userRepository.existsByUsername("user1"));
                }

                @Test
                @DisplayName("Should return validation error when username is empty")
                void shouldReturnValidationErrorWhenUsernameIsEmpty() throws Exception {
                        patientRequest.getUser().setUsername("");

                        mockMvc.perform(
                                        post("/api/patients")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(patientRequest)))
                                        .andExpectAll(status().isBadRequest(),
                                                        jsonPath("$.errors['user.username']", Matchers
                                                                        .containsInAnyOrder("Username harus diisi",
                                                                                        "Panjang username harus diantara 3 dan 255 karakter")));
                }

        }
}
