package healthclinic.health_clinic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.dto.DoctorResponse;
import healthclinic.health_clinic.models.Doctor;
import healthclinic.health_clinic.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<DoctorResponse> findAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDoctorResponse)
                .collect(Collectors.toList());
    }

    private DoctorResponse convertToDoctorResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSip(),
                doctor.getDateOfBirth(),
                doctor.getPlaceOfBirth(),
                doctor.getAge(),
                doctor.getGender(),
                doctor.getPhone(),
                doctor.getAddress(),
                doctor.getUser(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt());
    }

}
