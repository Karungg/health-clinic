package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreatePatientResponse;

public interface PatientService {
    CreatePatientResponse createPatient(CreatePatientRequest request);

    CreatePatientResponse updatePatient(CreatePatientRequest request, UUID patientId);

    List<CreatePatientResponse> findAllPatients();
}
