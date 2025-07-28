package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.PatientResponse;

public interface PatientService {
    List<PatientResponse> findAllPatients();

    PatientResponse createPatient(CreatePatientRequest request);

    PatientResponse updatePatient(CreatePatientRequest request, UUID patientId);

    void deletePatient(UUID patientId);
}
