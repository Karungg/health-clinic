package healthclinic.health_clinic.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import healthclinic.health_clinic.Enums.MedicineCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponse {

    private UUID id;

    private String medicineCode;

    private String name;

    private MedicineCategory category;

    private String form;

    private String strength;

    private String description;

    private String manufacturer;

    private String batchNumber;

    private LocalDate expiryDate;

    private Integer stock;

    private BigDecimal price;

    private String storageConditions;

    private Instant createdAt;

    private Instant updatedAt;
}
