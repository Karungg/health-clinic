package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreateMedicalHistoryRequest;
import healthclinic.health_clinic.dto.MedicalHistoryResponse;

public interface MedicalHistoryService {

    List<MedicalHistoryResponse> findAllMedicalHistories();

    MedicalHistoryResponse createMedicalHistory(CreateMedicalHistoryRequest request);

    MedicalHistoryResponse updateMedicalHistory(CreateMedicalHistoryRequest request, UUID medicalId);

    void deleteMedicalHistory(UUID medicalId);

}
