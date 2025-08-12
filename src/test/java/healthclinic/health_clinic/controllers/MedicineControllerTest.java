package healthclinic.health_clinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
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
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureWebMvc
public class MedicineControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private MedicineRepository medicineRepository;

        private CreateMedicineRequest request;

        @BeforeEach
        void setUp() {
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
        }

        void findAllMedicines() throws Exception {
                mockMvc.perform(
                                get("/api/medicines")).andExpect(status().isOk());
        }

        void createMedicineSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/medicines")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(status().isCreated(),
                                                jsonPath("$.data.name").value(request.getName()));
        }

        void createMedicineError() throws Exception {
                request.setName(null);

                mockMvc.perform(
                                post("/api/medicines")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(status().isBadRequest(),
                                                jsonPath("$.errors.name").value("Nama harus diisi"));
        }

        void updateMedicineSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/medicines")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)));

                Medicine medicine = medicineRepository.findByNameEquals("Paracetamol Tablet 500mg").orElse(null);

                request.setName("Obat Demam"); // update name

                mockMvc.perform(
                                put("/api/medicines/" + medicine.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(status().isOk(),
                                                jsonPath("$.data.name").value(request.getName()));

        }

        void updateMedicineError() throws Exception {
                mockMvc.perform(
                                post("/api/medicines")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)));

                Medicine medicine = medicineRepository.findByNameEquals("Paracetamol Tablet 500mg").orElse(null);

                request.setName(null); // update name

                mockMvc.perform(
                                put("/api/medicines/" + medicine.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(status().isBadRequest(),
                                                jsonPath("$.errors.name").value("Nama harus diisi"));

        }

        void updateMedicineErrorNotFound() throws Exception {
                mockMvc.perform(
                                put("/api/medicines/" + UUID.randomUUID())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpectAll(status().isNotFound());

        }

        void deleteMedicineSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/medicines")
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                                .content(objectMapper.writeValueAsString(request)));

                Medicine medicine = medicineRepository.findByNameEquals("Paracetamol Tablet 500mg").orElse(null);

                mockMvc.perform(
                                delete("/api/medicines/" + medicine.getId())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isNoContent());

        }

        void deleteMedicineError() throws Exception {
                mockMvc.perform(
                                delete("/api/medicines/" + UUID.randomUUID())
                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isNotFound());

        }

}
