package healthclinic.health_clinic.controllers;

import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateMedicalHistoryRequest;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.MedicalHistoryResponse;
import healthclinic.health_clinic.services.MedicalHistoryService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @GetMapping(path = "/api/medical-histories")
    public ResponseEntity<GenericResponse<List<MedicalHistoryResponse>>> getMedicalHistories() {

        GenericResponse<List<MedicalHistoryResponse>> response = GenericResponse
                .ok(medicalHistoryService.findAllMedicalHistories());

        return ResponseEntity.ok().body(response);

    }

    @PostMapping(path = "/api/medical-histories")
    public ResponseEntity<GenericResponse<MedicalHistoryResponse>> createMedicalHistory(
            @RequestBody @Valid CreateMedicalHistoryRequest request) {

        GenericResponse<MedicalHistoryResponse> response = GenericResponse
                .created(medicalHistoryService.createMedicalHistory(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
