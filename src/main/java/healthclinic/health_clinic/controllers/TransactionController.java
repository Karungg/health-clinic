package healthclinic.health_clinic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.TransactionResponse;
import healthclinic.health_clinic.services.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(path = "/api/transactions")
    public ResponseEntity<GenericResponse<List<TransactionResponse>>> getTransactions() {

        GenericResponse<List<TransactionResponse>> response = GenericResponse
                .ok(transactionService.findAllTransactions());

        return ResponseEntity.ok().body(response);
    }

}
