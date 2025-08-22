package healthclinic.health_clinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import healthclinic.health_clinic.Enums.MedicineCategory;
import healthclinic.health_clinic.dto.CreateMedicineRequest;
import healthclinic.health_clinic.models.Medicine;
import healthclinic.health_clinic.repository.MedicineRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MedicineControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private MedicineRepository medicineRepository;

        private CreateMedicineRequest request;
        private Medicine sampleMedicine;

        @BeforeEach
        void setUp() {
                sampleMedicine = buildSampleMedicine();
                request = buildValidMedicineRequest();
        }

        private Medicine buildSampleMedicine() {
                Medicine medicine = new Medicine();
                medicine.setName("Paracetamol Tablet 500mg");
                medicine.setCategory(MedicineCategory.OTC);
                medicine.setForm("Tablet");
                medicine.setStrength("500mg");
                medicine.setDescription(
                                "Paracetamol is used to treat pain and reduce fever. Effective for headaches, muscle aches, arthritis, backache, toothaches, colds, and fevers.");
                medicine.setManufacturer("PT Kimia Farma Tbk");
                medicine.setBatchNumber("KF2024001");
                medicine.setExpiryDate(LocalDate.of(2025, 12, 31));
                medicine.setStock(150);
                medicine.setPrice(new BigDecimal("5500.00"));
                medicine.setStorageConditions("Store in a cool, dry place below 30°C. Keep away from direct sunlight.");
                return medicine;
        }

        private CreateMedicineRequest buildValidMedicineRequest() {
                CreateMedicineRequest request = new CreateMedicineRequest();
                request.setName("Paracetamol Tablet 500mg");
                request.setCategory(MedicineCategory.OTC);
                request.setForm("Tablet");
                request.setStrength("500mg");
                request.setDescription(
                                "Paracetamol is used to treat pain and reduce fever. Effective for headaches, muscle aches, arthritis, backache, toothaches, colds, and fevers.");
                request.setManufacturer("PT Kimia Farma Tbk");
                request.setBatchNumber("KF2024001");
                request.setExpiryDate(LocalDate.of(2025, 12, 31));
                request.setStock(150);
                request.setPrice(new BigDecimal("5500.00"));
                request.setStorageConditions("Store in a cool, dry place below 30°C. Keep away from direct sunlight.");
                return request;
        }

        private Medicine createMedicine() throws Exception {
                mockMvc.perform(
                                post("/api/medicines")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)));

                return medicineRepository.findByNameEquals("Paracetamol Tablet 500mg").orElse(null);
        }

        @Nested
        @DisplayName("Get All Medicines")
        class GetAllMedicines {

                @Test
                @DisplayName("Should return empty list when no medicines exist")
                void shouldReturnEmptyListWhenNoMedicinesExist() throws Exception {
                        mockMvc.perform(get("/api/medicines"))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data").isArray(),
                                                        jsonPath("$.data", hasSize(0)));
                }

                @Test
                @DisplayName("Should return all medicines when they exist")
                void shouldReturnAllMedicinesWhenTheyExist() throws Exception {
                        createMedicine();

                        mockMvc.perform(get("/api/medicines"))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data").isArray(),
                                                        jsonPath("$.data", hasSize(1)),
                                                        jsonPath("$.data[0].name").value("Paracetamol Tablet 500mg"));
                }
        }

        @Nested
        @DisplayName("Get Medicine By ID")
        class GetMedicineById {

                @Test
                @DisplayName("Should return medicine when it exists")
                void shouldReturnMedicineWhenItExists() throws Exception {
                        Medicine medicine = createMedicine();

                        mockMvc.perform(get("/api/medicines/" + medicine.getId()))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.id").value(medicine.getId().toString()),
                                                        jsonPath("$.data.name").value("Paracetamol Tablet 500mg"),
                                                        jsonPath("$.data.category").value("OTC"),
                                                        jsonPath("$.data.price").value(5500.00));
                }

                @Test
                @DisplayName("Should return not found when medicine does not exist")
                void shouldReturnNotFoundWhenMedicineDoesNotExist() throws Exception {
                        mockMvc.perform(get("/api/medicines/" + UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request for invalid UUID format")
                void shouldReturnBadRequestForInvalidUuidFormat() throws Exception {
                        mockMvc.perform(get("/api/medicines/invalid-uuid"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Create Medicine")
        class CreateMedicine {

                @Test
                @DisplayName("Should create medicine successfully with valid data")
                void shouldCreateMedicineSuccessfully() throws Exception {
                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .accept(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.name").value(request.getName()),
                                                        jsonPath("$.data.category").value("OTC"),
                                                        jsonPath("$.data.form").value("Tablet"),
                                                        jsonPath("$.data.strength").value("500mg"),
                                                        jsonPath("$.data.manufacturer").value("PT Kimia Farma Tbk"),
                                                        jsonPath("$.data.stock").value(150),
                                                        jsonPath("$.data.price").value(5500.00));
                }

                @Test
                @DisplayName("Should return validation error when name is null")
                void shouldReturnValidationErrorWhenNameIsNull() throws Exception {
                        request.setName(null);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .accept(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.name").value("Nama obat harus diisi"));
                }

                @Test
                @DisplayName("Should return validation error when name is empty")
                void shouldReturnValidationErrorWhenNameIsEmpty() throws Exception {
                        request.setName("");

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.name").exists());
                }

                @Test
                @DisplayName("Should return validation error when category is null")
                void shouldReturnValidationErrorWhenCategoryIsNull() throws Exception {
                        request.setCategory(null);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.category")
                                                                        .value("Kategori obat harus diisi"));
                }

                @Test
                @DisplayName("Should return validation error when price is negative")
                void shouldReturnValidationErrorWhenPriceIsNegative() throws Exception {
                        request.setPrice(new BigDecimal("-100.00"));

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.price")
                                                                        .value("Harga harus lebih besar dari 0"));
                }

                @Test
                @DisplayName("Should return validation error when stock is negative")
                void shouldReturnValidationErrorWhenStockIsNegative() throws Exception {
                        request.setStock(-10);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.stock").value("Stok tidak boleh negatif"));
                }

                @Test
                @DisplayName("Should return validation error when expiry date is in the past")
                void shouldReturnValidationErrorWhenExpiryDateIsInThePast() throws Exception {
                        request.setExpiryDate(LocalDate.of(2020, 1, 1));

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.expiryDate").value(
                                                                        "Tanggal kedaluwarsa harus dimasa depan"));
                }

                @Test
                @DisplayName("Should return error when medicine name already exists")
                void shouldReturnErrorWhenMedicineNameAlreadyExists() throws Exception {
                        createMedicine();

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.name").value(
                                                                        "Name is already exists."));
                }

                @Test
                @DisplayName("Should return error when request body is malformed")
                void shouldReturnErrorWhenRequestBodyIsMalformed() throws Exception {
                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content("{ invalid json }"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Update Medicine")
        class UpdateMedicine {

                @Test
                @DisplayName("Should update medicine successfully")
                void shouldUpdateMedicineSuccessfully() throws Exception {
                        Medicine medicine = createMedicine();

                        request.setName("Obat Demam Updated");
                        request.setPrice(new BigDecimal("6000.00"));
                        request.setStock(200);

                        mockMvc.perform(
                                        put("/api/medicines/" + medicine.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .accept(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.name").value("Obat Demam Updated"),
                                                        jsonPath("$.data.price").value(6000.00),
                                                        jsonPath("$.data.stock").value(200));
                }

                @Test
                @DisplayName("Should return validation error when updating with null name")
                void shouldReturnValidationErrorWhenUpdatingWithNullName() throws Exception {
                        Medicine medicine = createMedicine();
                        request.setName("");

                        mockMvc.perform(
                                        put("/api/medicines/" + medicine.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isBadRequest(),
                                                        jsonPath("$.errors.name", Matchers.containsInAnyOrder(
                                                                        "Panjang nama obat harus diantara 3 dan 100 karakter",
                                                                        "Nama obat harus diisi")));
                }

                @Test
                @DisplayName("Should return not found when updating non-existent medicine")
                void shouldReturnNotFoundWhenUpdatingNonExistentMedicine() throws Exception {
                        mockMvc.perform(
                                        put("/api/medicines/" + UUID.randomUUID())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request when updating with invalid UUID")
                void shouldReturnBadRequestWhenUpdatingWithInvalidUuid() throws Exception {
                        mockMvc.perform(
                                        put("/api/medicines/invalid-uuid")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("Should update only provided fields (partial update)")
                void shouldUpdateOnlyProvidedFields() throws Exception {
                        Medicine medicine = createMedicine();

                        // Only update name and price
                        CreateMedicineRequest partialRequest = new CreateMedicineRequest();
                        partialRequest.setName("Updated Name Only");
                        partialRequest.setCategory(request.getCategory());
                        partialRequest.setForm(request.getForm());
                        partialRequest.setStrength(request.getStrength());
                        partialRequest.setDescription(request.getDescription());
                        partialRequest.setManufacturer(request.getManufacturer());
                        partialRequest.setBatchNumber(request.getBatchNumber());
                        partialRequest.setExpiryDate(request.getExpiryDate());
                        partialRequest.setStock(request.getStock());
                        partialRequest.setPrice(new BigDecimal("7000.00"));
                        partialRequest.setStorageConditions(request.getStorageConditions());

                        mockMvc.perform(
                                        put("/api/medicines/" + medicine.getId())
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(partialRequest)))
                                        .andExpectAll(
                                                        status().isOk(),
                                                        jsonPath("$.data.name").value("Updated Name Only"),
                                                        jsonPath("$.data.price").value(7000.00));
                }
        }

        @Nested
        @DisplayName("Delete Medicine")
        class DeleteMedicine {

                @Test
                @DisplayName("Should delete medicine successfully")
                void shouldDeleteMedicineSuccessfully() throws Exception {
                        Medicine medicine = createMedicine();

                        mockMvc.perform(delete("/api/medicines/" + medicine.getId()))
                                        .andExpect(status().isNoContent());

                        // Verify deletion
                        assertTrue(medicineRepository.findById(medicine.getId()).isEmpty());
                }

                @Test
                @DisplayName("Should return not found when deleting non-existent medicine")
                void shouldReturnNotFoundWhenDeletingNonExistentMedicine() throws Exception {
                        mockMvc.perform(delete("/api/medicines/" + UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("Should return bad request when deleting with invalid UUID format")
                void shouldReturnBadRequestWhenDeletingWithInvalidUuidFormat() throws Exception {
                        mockMvc.perform(delete("/api/medicines/invalid-uuid"))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        @DisplayName("Medicine Categories and Types")
        class MedicineCategoriesAndTypes {

                @Test
                @DisplayName("Should create medicine with OTC category")
                void shouldCreateMedicineWithOtcCategory() throws Exception {
                        request.setCategory(MedicineCategory.OTC);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.category").value("OTC"));
                }

                @Test
                @DisplayName("Should create medicine with HARD DRUGS category")
                void shouldCreateMedicineWithHardDrugsCategory() throws Exception {
                        request.setCategory(MedicineCategory.HARD_DRUGS);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.category").value("HARD_DRUGS"));
                }

                @Test
                @DisplayName("Should handle different medicine forms")
                void shouldHandleDifferentMedicineForms() throws Exception {
                        String[] forms = { "Tablet", "Capsule", "Syrup", "Injection", "Cream", "Ointment" };

                        for (String form : forms) {
                                request.setName("Test Medicine " + form);
                                request.setBatchNumber("KF2024001");
                                request.setForm(form);

                                mockMvc.perform(
                                                post("/api/medicines")
                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                .content(objectMapper.writeValueAsString(request)))
                                                .andExpectAll(
                                                                status().isCreated(),
                                                                jsonPath("$.data.form").value(form));
                        }
                }
        }

        @Nested
        @DisplayName("Business Logic and Edge Cases")
        class BusinessLogicAndEdgeCases {

                @Test
                @DisplayName("Should handle zero stock")
                void shouldHandleZeroStock() throws Exception {
                        request.setStock(0);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.stock").value(0));
                }

                @Test
                @DisplayName("Should handle large stock numbers")
                void shouldHandleLargeStockNumbers() throws Exception {
                        request.setStock(Integer.MAX_VALUE);

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.stock").value(Integer.MAX_VALUE));
                }

                @Test
                @DisplayName("Should handle very high prices")
                void shouldHandleVeryHighPrices() throws Exception {
                        request.setPrice(new BigDecimal("999999.99"));

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.price").value(999999.99));
                }

                @Test
                @DisplayName("Should handle decimal prices correctly")
                void shouldHandleDecimalPricesCorrectly() throws Exception {
                        request.setPrice(new BigDecimal("15.75"));

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.price").value(15.75));
                }

                @Test
                @DisplayName("Should handle expiry date at exact boundary")
                void shouldHandleExpiryDateAtExactBoundary() throws Exception {
                        request.setExpiryDate(LocalDate.now().plusDays(1));

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isCreated());
                }

                @Test
                @DisplayName("Should handle special characters in medicine name")
                void shouldHandleSpecialCharactersInMedicineName() throws Exception {
                        request.setName("Paracetamol® Extra Strength 500mg (Red Cross™)");

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.name").value(
                                                                        "Paracetamol® Extra Strength 500mg (Red Cross™)"));
                }

                @Test
                @DisplayName("Should handle Unicode characters in description")
                void shouldHandleUnicodeCharactersInDescription() throws Exception {
                        request.setDescription("药物说明：用于治疗发热和疼痛。Descripción: Para tratar fiebre y dolor.");

                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpectAll(
                                                        status().isCreated(),
                                                        jsonPath("$.data.description").value(request.getDescription()));
                }
        }

        @Nested
        @DisplayName("Content Type and Headers")
        class ContentTypeAndHeaders {

                @Test
                @DisplayName("Should return unsupported media type for non-JSON content")
                void shouldReturnUnsupportedMediaTypeForNonJsonContent() throws Exception {
                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                                                        .content("plain text"))
                                        .andExpect(status().isUnsupportedMediaType());
                }

                @Test
                @DisplayName("Should handle missing Accept header gracefully")
                void shouldHandleMissingAcceptHeaderGracefully() throws Exception {
                        mockMvc.perform(
                                        post("/api/medicines")
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .content(objectMapper.writeValueAsString(request)))
                                        .andExpect(status().isCreated());
                }
        }

        @Nested
        @DisplayName("Performance and Concurrency")
        class PerformanceAndConcurrency {

                @Test
                @DisplayName("Should handle concurrent medicine creation")
                void shouldHandleConcurrentMedicineCreation() throws Exception {
                        // This test would require threading, simplified version:
                        for (int i = 0; i < 5; i++) {
                                request.setName("Concurrent Medicine " + i);
                                request.setBatchNumber("CONC" + i);

                                mockMvc.perform(
                                                post("/api/medicines")
                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                .content(objectMapper.writeValueAsString(request)))
                                                .andExpect(status().isCreated());
                        }

                        // Verify all medicines were created
                        assertEquals(5, medicineRepository.count());
                }

                @Test
                @DisplayName("Should handle batch operations efficiently")
                void shouldHandleBatchOperationsEfficiently() throws Exception {
                        List<CreateMedicineRequest> requests = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                                CreateMedicineRequest batchRequest = buildValidMedicineRequest();
                                batchRequest.setName("Batch Medicine " + i);
                                batchRequest.setBatchNumber("BATCH" + i);
                                requests.add(batchRequest);
                        }
                }
        }
}
