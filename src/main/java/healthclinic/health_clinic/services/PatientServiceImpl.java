package healthclinic.health_clinic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public CreatePatientResponse createPatient(CreatePatientRequest request) {
        log.info("Attempting to create patient with fullName {}", request.getFullName());

        validateUnique(request);

        User user = new User();
        user.setUsername(request.getUser().getUsername());
        user.setPassword(passwordEncoder.encode(request.getUser().getPassword()));
        User savedUser = userRepository.save(user);

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
        patient.setJob(request.getJob());
        patient.setUser(savedUser);

        Address address = new Address();
        address.setCity(request.getAddress().getCity());
        address.setPostalCode(request.getAddress().getPostalCode());
        address.setStreet(request.getAddress().getStreet());
        patient.setAddress(address);

        Patient savedPatient = patientRepository.save(patient);

        return convertToPatientResponse(savedPatient);
    }

    @Transactional(readOnly = true)
    public List<CreatePatientResponse> findAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::convertToPatientResponse)
                .collect(Collectors.toList());
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

    private void validateUnique(CreatePatientRequest request) {
        userRepository.findByUsername(request.getUser().getUsername()).ifPresent(value -> {
            log.warn("Patient created failed. Username {} is already exists.", value.getUsername());
            throw new IllegalArgumentException("Username " + value.getUsername() + " is already taken.");
        });

        patientRepository.findByNik(request.getNik()).ifPresent(value -> {
            log.warn("Patient created failed. NIK {} is already exists.", value.getNik());
            throw new IllegalArgumentException("NIK " + value.getNik() + " is already taken.");
        });

        patientRepository.findByPhone(request.getPhone()).ifPresent(value -> {
            log.warn("Patient created failed. Phone {} is already exists.", value.getPhone());
            throw new IllegalArgumentException("Phone " + value.getPhone() + " is already taken.");
        });
    }

}
