package healthclinic.health_clinic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.dto.CreateMedicalHistoryRequest;
import healthclinic.health_clinic.dto.MedicalHistoryResponse;
import healthclinic.health_clinic.exception.ResourceNotFoundException;
import healthclinic.health_clinic.models.MedicalHistory;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.repository.MedicalHistoryRepository;
import healthclinic.health_clinic.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<MedicalHistoryResponse> findAllMedicalHistories() {
        return medicalHistoryRepository.findAll().stream()
                .map(this::convertToMedicalHistoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalHistoryResponse createMedicalHistory(CreateMedicalHistoryRequest request) {
        log.info("Attempting to create medical history with patient id {}", request.getPatientId());

        Patient patientToAdd = patientRepository.findById(request.getPatientId()).orElse(null);

        if (patientToAdd == null) {
            log.warn("Failed to create. Patient id {} not found", request.getPatientId());
            throw new ResourceNotFoundException("Patient id with " + request.getPatientId() + " not found");
        }

        if (medicalHistoryRepository.existsByPatientId(request.getPatientId())) {
            log.warn("Failed to create. Medical history with patient id {} is already exists", request.getPatientId());
            throw new IllegalArgumentException(
                    "Medical history with patient id " + request.getPatientId() + " is already exists.");
        }

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setAnamnesis(request.getAnamnesis());
        medicalHistory.setBodyCheck(request.getBodyCheck());
        medicalHistory.setTherapy(request.getTherapy());
        medicalHistory.setDiagnose(request.getDiagnose());
        medicalHistory.setPatient(patientToAdd);

        MedicalHistory savedMedicalHistory = medicalHistoryRepository.save(medicalHistory);
        log.info("Medical history with patient id {} successfully created", savedMedicalHistory.getPatient().getId());

        return convertToMedicalHistoryResponse(savedMedicalHistory);
    }

    private MedicalHistoryResponse convertToMedicalHistoryResponse(MedicalHistory medicalHistory) {
        return new MedicalHistoryResponse(
                medicalHistory.getId(),
                medicalHistory.getAnamnesis(),
                medicalHistory.getBodyCheck(),
                medicalHistory.getDiagnose(),
                medicalHistory.getTherapy(),
                medicalHistory.getPatient().getId(),
                medicalHistory.getCreatedAt(),
                medicalHistory.getUpdatedAt());
    }

}
