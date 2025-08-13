package healthclinic.health_clinic.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@Slf4j
public class CreateTransactionRequestTest {

    @Autowired
    private Validator validator;

    @Test
    void createTransactionRequestSuccess() {
        List<CreateTransactionDetailRequest> transactionDetails = new ArrayList<>();

        CreateTransactionRequest transactionRequest = new CreateTransactionRequest();
        transactionRequest.setComplaintOfPain("Demam 3 hari");
        transactionRequest.setDoctorId(UUID.randomUUID());
        transactionRequest.setPatientId(UUID.randomUUID());
        transactionRequest.setPaymentMethod("CASH");
        transactionRequest.setTreatment("Istirahat dan pemberian beberapa obat");

        CreateTransactionDetailRequest transactionDetailRequest = new CreateTransactionDetailRequest();
        transactionDetailRequest.setMedicineId(UUID.randomUUID());
        transactionDetailRequest.setQuantity(10);
        transactionDetails.add(transactionDetailRequest);

        CreateTransactionDetailRequest transactionDetailRequest1 = new CreateTransactionDetailRequest();
        transactionDetailRequest1.setMedicineId(UUID.randomUUID());
        transactionDetailRequest1.setQuantity(10);
        transactionDetails.add(transactionDetailRequest1);

        transactionRequest.setTransactionDetails(transactionDetails);

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    void createTransactionRequestError() {
        List<CreateTransactionDetailRequest> transactionDetails = new ArrayList<>();

        CreateTransactionRequest transactionRequest = new CreateTransactionRequest();
        transactionRequest.setComplaintOfPain(null);
        transactionRequest.setDoctorId(UUID.randomUUID());
        transactionRequest.setPatientId(UUID.randomUUID());
        transactionRequest.setPaymentMethod("CASH");
        transactionRequest.setTreatment("Istirahat dan pemberian beberapa obat");

        CreateTransactionDetailRequest transactionDetailRequest = new CreateTransactionDetailRequest();
        transactionDetailRequest.setMedicineId(null);
        transactionDetailRequest.setQuantity(10);
        transactionDetails.add(transactionDetailRequest);

        CreateTransactionDetailRequest transactionDetailRequest1 = new CreateTransactionDetailRequest();
        transactionDetailRequest1.setMedicineId(UUID.randomUUID());
        transactionDetailRequest1.setQuantity(null);
        transactionDetails.add(transactionDetailRequest1);

        transactionRequest.setTransactionDetails(transactionDetails);

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        Assertions.assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        tuple("transactionDetails[0].medicineId", "ID Obat harus diisi."),
                        tuple("transactionDetails[1].quantity", "Kuantitas harus diisi."),
                        tuple("complaintOfPain", "Keluhan harus diisi."));
    }
}
