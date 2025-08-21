package healthclinic.health_clinic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.PatientResponse;
import healthclinic.health_clinic.services.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(path = "/api/patients")
    public ResponseEntity<GenericResponse<List<PatientResponse>>> getPatients() {

        GenericResponse<List<PatientResponse>> response = GenericResponse.ok(patientService.findAllPatients());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/api/patients/{patientId}")
    public ResponseEntity<GenericResponse<PatientResponse>> getPatientById(
            @PathVariable(name = "patientId", required = true) UUID patientId) {

        GenericResponse<PatientResponse> response = GenericResponse.ok(patientService.getPatientById(patientId));

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/api/patients")
    public ResponseEntity<GenericResponse<PatientResponse>> createPatient(
            @RequestBody @Valid CreatePatientRequest request) {

        GenericResponse<PatientResponse> response = GenericResponse.created(patientService.createPatient(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/api/patients/{patientId}")
    public ResponseEntity<GenericResponse<PatientResponse>> updatePatient(
            @PathVariable(name = "patientId") UUID patientId,
            @RequestBody @Valid CreatePatientRequest request) {

        GenericResponse<PatientResponse> response = GenericResponse
                .ok(patientService.updatePatient(request, patientId));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(path = "/api/patients/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable(name = "patientId", required = true) UUID patientId) {
        patientService.deletePatient(patientId);

        return ResponseEntity.noContent().build();
    }

}
