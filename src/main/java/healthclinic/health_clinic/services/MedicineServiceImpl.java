package healthclinic.health_clinic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthclinic.health_clinic.dto.MedicineResponse;
import healthclinic.health_clinic.models.Medicine;
import healthclinic.health_clinic.repository.MedicineRepository;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<MedicineResponse> findAllMedicines() {
        return medicineRepository
                .findAll()
                .stream()
                .map(this::convertToMedicineResponse)
                .collect(Collectors.toList());
    }

    private MedicineResponse convertToMedicineResponse(Medicine medicine) {
        return new MedicineResponse(
                medicine.getId(),
                medicine.getMedicineCode(),
                medicine.getName(),
                medicine.getCategory(),
                medicine.getForm(),
                medicine.getStrength(),
                medicine.getDescription(),
                medicine.getManufacturer(),
                medicine.getBatchNumber(),
                medicine.getExpiryDate(),
                medicine.getStock(),
                medicine.getPrice(),
                medicine.getStorageConditions(),
                medicine.getCreatedAt(),
                medicine.getUpdatedAt());
    }

}
