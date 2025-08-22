package healthclinic.health_clinic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateMedicineRequest;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.MedicineResponse;
import healthclinic.health_clinic.services.MedicineService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping(path = "/api/medicines")
    public ResponseEntity<GenericResponse<List<MedicineResponse>>> getMedicines() {

        GenericResponse<List<MedicineResponse>> response = GenericResponse.ok(medicineService.findAllMedicines());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/api/medicines/{medicineId}")
    public ResponseEntity<GenericResponse<MedicineResponse>> getMedicineById(
            @PathVariable(name = "medicineId", required = true) UUID medicineId) {

        GenericResponse<MedicineResponse> response = GenericResponse.ok(medicineService.getMedicineById(medicineId));

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/api/medicines")
    public ResponseEntity<GenericResponse<MedicineResponse>> createMedicine(
            @RequestBody @Valid CreateMedicineRequest request) {

        GenericResponse<MedicineResponse> response = GenericResponse.created(medicineService.createMedicine(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/api/medicines/{medicineId}")
    public ResponseEntity<GenericResponse<MedicineResponse>> updateMedicine(
            @PathVariable(name = "medicineId", required = true) UUID medicineId,
            @RequestBody @Valid CreateMedicineRequest request) {

        GenericResponse<MedicineResponse> response = GenericResponse
                .ok(medicineService.updateMedicine(request, medicineId));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(path = "/api/medicines/{medicineId}")
    public ResponseEntity<GenericResponse<Void>> deleteMedicine(
            @PathVariable(name = "medicineId", required = true) UUID medicineId) {

        medicineService.deleteMedicine(medicineId);

        return ResponseEntity.noContent().build();
    }

}
