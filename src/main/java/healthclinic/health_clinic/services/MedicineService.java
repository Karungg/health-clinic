package healthclinic.health_clinic.services;

import java.util.List;

import healthclinic.health_clinic.dto.CreateMedicineRequest;
import healthclinic.health_clinic.dto.MedicineResponse;

public interface MedicineService {
    List<MedicineResponse> findAllMedicines();

    MedicineResponse createMedicine(CreateMedicineRequest request);
}
