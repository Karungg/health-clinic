package healthclinic.health_clinic.services;

import java.util.List;

import healthclinic.health_clinic.dto.CreateTransactionRequest;
import healthclinic.health_clinic.dto.TransactionResponse;

public interface TransactionService {

    List<TransactionResponse> findAllTransactions();

    TransactionResponse createTransaction(CreateTransactionRequest request);
}
