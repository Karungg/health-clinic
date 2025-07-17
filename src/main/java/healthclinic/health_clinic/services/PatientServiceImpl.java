package healthclinic.health_clinic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreatePatientResponse;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.PatientRepository;
import healthclinic.health_clinic.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CreatePatientResponse createPatient(CreatePatientRequest request) {
        log.info("Attempting to create patient with fullName {}", request.getFullName());

        Patient patient = new Patient();
        patient.setFullName(request.getFullName());
        patient.setNik(request.getNik());
        patient.setAge(request.getAge());
        patient.setBloodType(request.getBloodType());
        patient.setGender(request.getGender());
        patient.setHeight(request.getHeight());
        patient.setPhone(request.getPhone());
        patient.setPlaceOfBirth(request.getPlaceOfBirth());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setWeight(request.getWeight());

        Address address = new Address();
        address.setCity(request.getAddress().getCity());
        address.setPostalCode(request.getAddress().getPostalCode());
        address.setStreet(request.getAddress().getStreet());

        patient.setAddress(address);

        User user = userRepository.findByIdEquals(request.getUserId()).orElse(null);
        patient.setUser(user);

        Patient savedPatient = patientRepository.save(patient);

        return convertToPatientResponse(savedPatient);
    }

    private CreatePatientResponse convertToPatientResponse(Patient patient) {
        return new CreatePatientResponse(
                patient.getId(),
                patient.getFullName(),
                patient.getNik(),
                patient.getDateOfBirth(),
                patient.getAge(),
                patient.getAddress(),
                patient.getPhone(),
                patient.getGender(),
                patient.getJob(),
                patient.getPlaceOfBirth(),
                patient.getWeight(),
                patient.getHeight(),
                patient.getBloodType(),
                patient.getUser().getId(),
                patient.getCreatedAt(),
                patient.getUpdatedAt());
    }

}
