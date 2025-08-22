package healthclinic.health_clinic.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthclinic.health_clinic.dto.CreateTransactionDetailRequest;
import healthclinic.health_clinic.dto.CreateTransactionRequest;
import healthclinic.health_clinic.dto.TransactionResponse;
import healthclinic.health_clinic.exception.ResourceNotFoundException;
import healthclinic.health_clinic.models.Doctor;
import healthclinic.health_clinic.models.Medicine;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.models.Transaction;
import healthclinic.health_clinic.models.TransactionDetail;
import healthclinic.health_clinic.repository.DoctorRepository;
import healthclinic.health_clinic.repository.MedicineRepository;
import healthclinic.health_clinic.repository.PatientRepository;
import healthclinic.health_clinic.repository.TransactionDetailRepository;
import healthclinic.health_clinic.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    public List<TransactionResponse> findAllTransactions() {
        return transactionRepository.findAll()
                .stream().map(this::convertToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        log.info("Attempting to create transaction with patient ID {}", request.getPatientId());

        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> {
            log.warn("Failed to create transaction. patient with ID {} not found", request.getPatientId());
            throw new ResourceNotFoundException("Patient with ID " + request.getPatientId() + " not found");
        });

        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> {
            log.warn("Failed to create transaction. Doctor with ID {} not found", request.getDoctorId());
            throw new ResourceNotFoundException("Doctor with ID " + request.getDoctorId() + " not found");
        });

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setComplaintOfPain(request.getComplaintOfPain());
        transaction.setTreatment(request.getTreatment());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setTotal(BigDecimal.valueOf(0));
        transaction.setPatient(patient);
        transaction.setDoctor(doctor);

        // Save transaction
        Transaction transactionSaved = transactionRepository.save(transaction);

        // Create transaction details
        List<TransactionDetail> transactionDetails = new ArrayList<TransactionDetail>();
        request.getTransactionDetails().stream().forEach(t -> {
            log.info("Attempting to add transaction details to transaction ID {}", transactionSaved.getId());

            Medicine medicine = medicineRepository.findById(t.getMedicineId()).orElseThrow(() -> {
                log.warn("Failed to create transaction. Medicine with ID {} not found", t.getMedicineId());
                throw new ResourceNotFoundException("Medicine with ID " + t.getMedicineId() + " not found");
            });

            // Initiate new object transaction detail
            TransactionDetail transactionDetailToSave = new TransactionDetail();

            transactionDetailToSave.setMedicine(medicine);
            transactionDetailToSave.setCurrentPrice(medicine.getPrice());
            transactionDetailToSave.setQuantity(t.getQuantity());
            transactionDetailToSave.setSubtotal(medicine.getPrice().multiply(BigDecimal.valueOf(t.getQuantity())));
            transactionDetailToSave.setTransaction(transactionSaved);

            // Add transaction detail to list
            transactionDetails.add(transactionDetailToSave);
        });

        transactionSaved.setTransactionDetails(transactionDetails);

        log.info("Transaction with ID {} successfully created", transactionSaved.getId());

        return convertToTransactionResponse(transactionSaved);
    }

    private TransactionResponse convertToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getComplaintOfPain(),
                transaction.getTreatment(),
                transaction.getPaymentMethod(),
                transaction.getTotal(),
                transaction.getPatient(),
                transaction.getDoctor(),
                transaction.getTransactionDetails(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt());
    }

}
