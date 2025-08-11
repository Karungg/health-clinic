package healthclinic.health_clinic.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import healthclinic.health_clinic.Enums.MedicineCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMedicineRequest {
    @NotBlank(message = "{medicine.name.notblank}")
    @Size(min = 3, max = 100, message = "{medicine.name.size}")
    private String name;

    @NotNull(message = "{medicine.category.notnull}")
    private MedicineCategory category;

    @NotBlank(message = "{medicine.form.notblank}")
    @Size(min = 3, max = 50, message = "{medicine.form.size}")
    private String form;

    @NotBlank(message = "{medicine.strength.notblank}")
    @Size(min = 1, max = 50, message = "{medicine.strength.size}")
    private String strength;

    @NotBlank(message = "{medicine.description.notblank}")
    @Size(min = 10, max = 500, message = "{medicine.description.size}")
    private String description;

    @NotBlank(message = "{medicine.manufacturer.notblank}")
    @Size(min = 3, max = 100, message = "{medicine.manufacturer.size}")
    private String manufacturer;

    @NotBlank(message = "{medicine.batchNumber.notblank}")
    @Pattern(regexp = "^[A-Z0-9]{5,20}$", message = "{medicine.batchNumber.pattern}")
    private String batchNumber;

    @NotNull(message = "{medicine.expiryDate.notnull}")
    @Future(message = "{medicine.expiryDate.future}")
    private LocalDate expiryDate;

    @NotNull(message = "{medicine.stock.notnull}")
    @PositiveOrZero(message = "{medicine.stock.positiveOrZero}")
    private Integer stock;

    @NotNull(message = "{medicine.price.notnull}")
    @DecimalMin(value = "0.01", message = "{medicine.price.min}")
    private BigDecimal price;

    @NotBlank(message = "{medicine.storageConditions.notblank}")
    @Size(min = 5, max = 200, message = "{medicine.storageConditions.size}")
    private String storageConditions;
}
