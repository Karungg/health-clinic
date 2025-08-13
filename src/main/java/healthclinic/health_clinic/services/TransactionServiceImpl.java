package healthclinic.health_clinic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthclinic.health_clinic.dto.CreateTransactionRequest;
import healthclinic.health_clinic.dto.TransactionResponse;
import healthclinic.health_clinic.models.Transaction;
import healthclinic.health_clinic.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionResponse> findAllTransactions() {
        return transactionRepository.findAll()
                .stream().map(this::convertToTransactionResponse)
                .collect(Collectors.toList());
    }

    // public TransactionResponse createTransaction(CreateTransactionRequest
    // request) {
    // }

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
