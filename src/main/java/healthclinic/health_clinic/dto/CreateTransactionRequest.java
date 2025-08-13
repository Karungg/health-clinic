package healthclinic.health_clinic.dto;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.Enums.PaymentMethod;
import healthclinic.health_clinic.validator.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {

    @NotBlank(message = "{transaction.complaint.notblank}")
    @Size(min = 3, max = 512, message = "{transaction.complaint.size}")
    private String complaintOfPain;

    @NotBlank(message = "{transaction.complaint.notblank}")
    @Size(min = 3, max = 512, message = "{transaction.complaint.size}")
    private String treatment;

    @ValueOfEnum(enumClass = PaymentMethod.class, message = "{transaction.payment.enum}")
    @NotBlank(message = "{transaction.payment.notblank}")
    private String paymentMethod;

    @Valid
    @NotNull(message = "{transaction.transactiondetails.notnull}")
    private List<CreateTransactionDetailRequest> transactionDetails;

    @NotBlank(message = "{transaction.patientid.notblank}")
    private UUID patientId;

    @NotBlank(message = "{transaction.doctorid.notblank}")
    private UUID doctorId;
}
