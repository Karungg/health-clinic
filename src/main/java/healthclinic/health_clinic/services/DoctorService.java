package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;

import healthclinic.health_clinic.dto.CreateDoctorRequest;
import healthclinic.health_clinic.dto.DoctorResponse;

public interface DoctorService {
    List<DoctorResponse> findAllDoctors();

    DoctorResponse getDoctorById(UUID doctorId);

    DoctorResponse createDoctor(CreateDoctorRequest request);

    DoctorResponse updateDoctor(CreateDoctorRequest request, UUID doctorId);

    void deleteDoctor(UUID doctorId);
}
