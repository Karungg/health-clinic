package healthclinic.health_clinic.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import healthclinic.health_clinic.dto.CreateMedicineRequest;
import healthclinic.health_clinic.dto.MedicineResponse;
import healthclinic.health_clinic.exception.UniqueConstraintFieldException;
import healthclinic.health_clinic.models.Medicine;
import healthclinic.health_clinic.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public MedicineResponse createMedicine(CreateMedicineRequest request) {
        log.info("Attempting to create medicine with name {}", request.getName());

        medicineRepository.findByNameEquals(request.getName()).ifPresent((m) -> {
            log.warn("Failed to create medicine, name {} is already exists.", m.getName());

            Map<String, List<String>> errors = new HashMap<>();
            errors.put("name", List.of("Name is already exists."));
            throw new UniqueConstraintFieldException("Name is already exists", errors);
        });

        String medicineCode = generateMedicineCode(request.getName());

        Medicine medicine = new Medicine();
        medicine.setMedicineCode(medicineCode);
        medicine.setName(request.getName());
        medicine.setCategory(request.getCategory());
        medicine.setForm(request.getForm());
        medicine.setStrength(request.getStrength());
        medicine.setDescription(request.getDescription());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setBatchNumber(request.getBatchNumber());
        medicine.setExpiryDate(request.getExpiryDate());
        medicine.setStock(request.getStock());
        medicine.setPrice(request.getPrice());
        medicine.setStorageConditions(request.getStorageConditions());

        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("Medicine with name {} successfully created.", savedMedicine.getName());

        return convertToMedicineResponse(savedMedicine);
    }

    private String generateMedicineCode(String name) {
        String prefix = extractPrefix(name);
        int nextNumber = getNexSequenceNumber(prefix);

        return String.format("%s-%03d", prefix, nextNumber);
    }

    private String extractPrefix(String name) {
        String cleanName = name.replaceAll("[^a-zA-Z]", "").toUpperCase();

        if (cleanName.length() >= 4) {
            return cleanName.substring(0, 4);
        } else {
            return String.format("%-4s", cleanName).replace(" ", "X");
        }
    }

    private int getNexSequenceNumber(String prefix) {
        String pattern = prefix + "-%";
        String lastCode = medicineRepository.findLastMedicineCodeByPattern(pattern);

        if (lastCode != null) {
            String[] parts = lastCode.split("-");
            if (parts.length == 2) {
                try {
                    return Integer.parseInt(parts[1]) + 1;
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }

        return 1;
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
