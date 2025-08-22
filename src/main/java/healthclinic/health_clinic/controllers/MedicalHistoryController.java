package healthclinic.health_clinic.controllers;

import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateMedicalHistoryRequest;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.MedicalHistoryResponse;
import healthclinic.health_clinic.services.MedicalHistoryService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping(path = "/api/medical-histories/{medicalId}")
    public ResponseEntity<GenericResponse<MedicalHistoryResponse>> getMedicalHistoryById(
            @PathVariable(name = "medicalId", required = true) UUID medicalId) {

        GenericResponse<MedicalHistoryResponse> response = GenericResponse
                .ok(medicalHistoryService.getMedicalById(medicalId));

        return ResponseEntity.ok().body(response);

    }

    @PostMapping(path = "/api/medical-histories")
    public ResponseEntity<GenericResponse<MedicalHistoryResponse>> createMedicalHistory(
            @RequestBody @Valid CreateMedicalHistoryRequest request) {

        GenericResponse<MedicalHistoryResponse> response = GenericResponse
                .created(medicalHistoryService.createMedicalHistory(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/api/medical-histories/{medicalId}")
    public ResponseEntity<GenericResponse<MedicalHistoryResponse>> updateMedicalHistory(
            @PathVariable(name = "medicalId", required = true) UUID medicalId,
            @RequestBody @Valid CreateMedicalHistoryRequest request) {

        GenericResponse<MedicalHistoryResponse> response = GenericResponse
                .ok(medicalHistoryService.updateMedicalHistory(request, medicalId));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(path = "/api/medical-histories/{medicalId}")
    public ResponseEntity<Void> deleteMedicalHistory(
            @PathVariable(name = "medicalId", required = true) UUID medicalId) {

        medicalHistoryService.deleteMedicalHistory(medicalId);

        return ResponseEntity.noContent().build();
    }

}
