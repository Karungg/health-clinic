package healthclinic.health_clinic.services;

import java.util.List;

import healthclinic.health_clinic.dto.CreateMedicalHistoryRequest;
import healthclinic.health_clinic.dto.MedicalHistoryResponse;

public interface MedicalHistoryService {

    List<MedicalHistoryResponse> findAllMedicalHistories();

    MedicalHistoryResponse createMedicalHistory(CreateMedicalHistoryRequest request);

}
