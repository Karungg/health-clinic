package healthclinic.health_clinic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateMedicineRequest;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.MedicineResponse;
import healthclinic.health_clinic.services.MedicineService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping(path = "/api/medicines")
    public ResponseEntity<GenericResponse<List<MedicineResponse>>> getMedicines() {

        GenericResponse<List<MedicineResponse>> response = GenericResponse.ok(medicineService.findAllMedicines());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/api/medicines")
    public ResponseEntity<GenericResponse<MedicineResponse>> createMedicine(
            @RequestBody @Valid CreateMedicineRequest request) {

        GenericResponse<MedicineResponse> response = GenericResponse.created(medicineService.createMedicine(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
