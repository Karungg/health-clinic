package healthclinic.health_clinic.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateDoctorRequest;
import healthclinic.health_clinic.dto.DoctorResponse;
import healthclinic.health_clinic.services.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping(path = "/api/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DoctorResponse>> getDoctors() {
        return ResponseEntity.ok().body(doctorService.findAllDoctors());
    }

    @PostMapping(path = "/api/doctors", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createDoctor(@ModelAttribute @Valid CreateDoctorRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(v -> v.getDefaultMessage())
                    .collect(Collectors.toList());

            log.info("Request failed with errors : " + errors.toString());
            return ResponseEntity.badRequest().body("Validation errors : " + errors.toString());
        }

        DoctorResponse response = doctorService.createDoctor(request);

        return ResponseEntity.ok().body("Doctor with name " + response.getFullName() + " successfully created.");
    }

}
