package healthclinic.health_clinic.services;

import java.util.List;

import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreatePatientResponse;

public interface PatientService {
    CreatePatientResponse createPatient(CreatePatientRequest request);

    List<CreatePatientResponse> findAllPatients();
}
