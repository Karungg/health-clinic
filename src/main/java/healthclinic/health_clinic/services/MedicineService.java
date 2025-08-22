package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreateMedicineRequest;
import healthclinic.health_clinic.dto.MedicineResponse;

public interface MedicineService {
    List<MedicineResponse> findAllMedicines();

    MedicineResponse getMedicineById(UUID medicineId);

    MedicineResponse createMedicine(CreateMedicineRequest request);

    MedicineResponse updateMedicine(CreateMedicineRequest request, UUID medicineId);

    void deleteMedicine(UUID medicineId);
}
