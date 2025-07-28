package healthclinic.health_clinic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.DoctorResponse;
import healthclinic.health_clinic.services.DoctorService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping(path = "/api/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DoctorResponse>> getDoctors() {
        return ResponseEntity.ok().body(doctorService.findAllDoctors());
    }

}
