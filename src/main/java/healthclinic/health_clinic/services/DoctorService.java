package healthclinic.health_clinic.services;

import java.util.List;

import healthclinic.health_clinic.dto.CreateDoctorRequest;
import healthclinic.health_clinic.dto.DoctorResponse;

public interface DoctorService {
    List<DoctorResponse> findAllDoctors();

    DoctorResponse createDoctor(CreateDoctorRequest request);
}
