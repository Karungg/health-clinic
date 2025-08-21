package healthclinic.health_clinic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateDoctorRequest;
import healthclinic.health_clinic.dto.DoctorResponse;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.services.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping(path = "/api/doctors")
    public ResponseEntity<GenericResponse<List<DoctorResponse>>> getDoctors() {

        GenericResponse<List<DoctorResponse>> response = GenericResponse.ok(doctorService.findAllDoctors());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/api/doctors/{doctorId}")
    public ResponseEntity<GenericResponse<DoctorResponse>> getDoctorById(
            @PathVariable(name = "doctorId", required = true) UUID doctorId) {

        GenericResponse<DoctorResponse> response = GenericResponse.ok(doctorService.getDoctorById(doctorId));

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/api/doctors")
    public ResponseEntity<GenericResponse<DoctorResponse>> createDoctor(
            @RequestBody @Valid CreateDoctorRequest request) {

        GenericResponse<DoctorResponse> response = GenericResponse.created(doctorService.createDoctor(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/api/doctors/{doctorId}")
    public ResponseEntity<GenericResponse<DoctorResponse>> updateDoctor(
            @PathVariable(name = "doctorId") UUID doctorId,
            @RequestBody @Valid CreateDoctorRequest request) {

        GenericResponse<DoctorResponse> response = GenericResponse.ok(doctorService.updateDoctor(request, doctorId));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(path = "/api/doctors/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable(name = "doctorId", required = true) UUID doctorId) {
        doctorService.deleteDoctor(doctorId);

        return ResponseEntity.noContent().build();
    }
}
