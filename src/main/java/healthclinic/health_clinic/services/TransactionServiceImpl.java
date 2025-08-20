package healthclinic.health_clinic.services;

import java.math.BigDecimal;
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
import healthclinic.health_clinic.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

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

    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        log.info("Attempting to create transaction with patient ID {}", request.getPatientId());

        Patient patientToSave = patientRepository.findById(request.getPatientId()).orElseThrow(() -> {
            log.warn("Failed to create transaction. patient with ID {} not found", request.getPatientId());
            throw new ResourceNotFoundException("Patient with ID " + request.getPatientId() + " not found");
        });

        Doctor doctorToSave = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> {
            log.warn("Failed to create transaction. Doctor with ID {} not found", request.getDoctorId());
            throw new ResourceNotFoundException("Doctor with ID " + request.getDoctorId() + " not found");
        });

        Transaction transactionToSave = new Transaction();
        transactionToSave.setPatient(patientToSave);
        transactionToSave.setDoctor(doctorToSave);
        transactionToSave.setComplaintOfPain(request.getComplaintOfPain());
        transactionToSave.setPaymentMethod(request.getPaymentMethod());
        transactionToSave.setTreatment(request.getTreatment());

        Transaction transactionSaved = transactionRepository.save(transactionToSave);

        List<TransactionDetail> transactionDetails = request.getTransactionDetails()
                .stream()
                .map(t -> createTransactionDetail(transactionSaved, t))
                .collect(Collectors.toList());

        transactionSaved.setTransactionDetails(transactionDetails);
        log.info("Transaction with ID {} successfully created", transactionSaved.getId());

        return convertToTransactionResponse(transactionSaved);

    }

    private TransactionDetail createTransactionDetail(Transaction transactionSaved,
            CreateTransactionDetailRequest request) {
        Medicine medicine = medicineRepository.findById(request.getMedicineId()).orElseThrow(() -> {
            log.warn("Failed to create transaction. Medicine with ID {} not found", request.getMedicineId());
            throw new ResourceNotFoundException("Medicine with ID " + request.getMedicineId() + " not found");
        });

        BigDecimal subtotal = medicine.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        return new TransactionDetail(
                UUID.randomUUID(),
                medicine.getPrice(),
                request.getQuantity(),
                subtotal,
                transactionSaved,
                medicine);
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
