package healthclinic.health_clinic.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreatePatientRequest;
import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.PatientResponse;
import healthclinic.health_clinic.exception.ResourceNotFoundException;
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

    @Transactional(readOnly = true)
    public List<PatientResponse> findAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::convertToPatientResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse createPatient(CreatePatientRequest request) {
        log.info("Attempting to create patient with full name {}", request.getFullName());

        validateUniqueness(request.getUser().getUsername(), request.getNik(), request.getPhone());

        User savedUser = createAndSaveUser(request.getUser());

        Patient patient = createObjectPatient(request);
        Address address = createObjectAddress(request.getAddress());

        patient.setAddress(address);
        patient.setUser(savedUser);

        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient with full name {} successfully created", savedPatient.getFullName());

        return convertToPatientResponse(savedPatient);
    }

    private User createAndSaveUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    private Patient createObjectPatient(CreatePatientRequest request) {
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

        return patient;
    }

    private Address createObjectAddress(Address addressRequest) {
        Address address = new Address();
        address.setCity(addressRequest.getCity());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setStreet(addressRequest.getStreet());

        return address;
    }

    private void validateUniqueness(String username, String nik, String phone) {
        userRepository.findByUsernameEquals(username).ifPresent(value -> {
            log.warn("Patient created failed. Username {} is already exists.", value.getUsername());
            throw new IllegalArgumentException("Username " + value.getUsername() + " is already taken.");
        });

        patientRepository.findByNikEquals(nik).ifPresent(value -> {
            log.warn("Patient created failed. NIK {} is already exists.", value.getNik());
            throw new IllegalArgumentException("NIK " + value.getNik() + " is already taken.");
        });

        patientRepository.findByPhoneEquals(phone).ifPresent(value -> {
            log.warn("Patient created failed. Phone {} is already exists.", value.getPhone());
            throw new IllegalArgumentException("Phone " + value.getPhone() + " is already taken.");
        });
    }

    private PatientResponse convertToPatientResponse(Patient patient) {
        return new PatientResponse(
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
