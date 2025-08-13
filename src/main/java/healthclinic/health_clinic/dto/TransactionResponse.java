package healthclinic.health_clinic.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.Enums.PaymentMethod;
import healthclinic.health_clinic.models.Doctor;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.models.TransactionDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private UUID id;

    private String complaintOfPain;

    private String treatment;

    private PaymentMethod paymentMethod;

    private BigDecimal total;

    private Patient patient;

    private Doctor doctor;

    private List<TransactionDetail> transactionDetails;

    private Instant createdAt;

    private Instant updatedAt;
}
