package healthclinic.health_clinic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreatePatientResponse;
import healthclinic.health_clinic.services.PatientService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(path = "/api/patients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreatePatientResponse>> getPatients() {
        return ResponseEntity.ok().body(patientService.findAllPatients());
    }

    @PostMapping(path = "/api/patients", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPatient(@ModelAttribute @Valid CreatePatientRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(value -> value.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors.toString());
        }

        CreatePatientResponse response = patientService.createPatient(request);

        return ResponseEntity.ok().body("Patient with name " + response.getFullName() + " successfully created.");
    }

}
