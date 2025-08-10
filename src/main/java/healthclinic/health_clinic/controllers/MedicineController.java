package healthclinic.health_clinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.services.MedicineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping(path = "/api/medicines")
    public ResponseEntity<GenericResponse getMedicines() {
        return ResponseEntity.ok();
    }

}
