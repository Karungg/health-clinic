package healthclinic.health_clinic.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDetailRequest {

    @NotNull(message = "{transactionDetail.medicineId.notnull}")
    private UUID medicineId;

    @NotNull(message = "{transactionDetail.quantity.notnull}")
    @Min(value = 1, message = "{transactionDetail.quantity.min}")
    private Integer quantity;

}
