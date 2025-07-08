package healthclinic.health_clinic.services;

import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreatePatientResponse;

public interface PatientService {
    CreatePatientResponse createPatient(CreatePatientRequest request);
}
