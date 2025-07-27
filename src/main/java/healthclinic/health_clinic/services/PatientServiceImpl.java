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
import healthclinic.health_clinic.dto.CreatePatientResponse;
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
    public List<CreatePatientResponse> findAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::convertToPatientResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreatePatientResponse createPatient(CreatePatientRequest request) {
        log.info("Attempting to create patient with fullName {}", request.getFullName());

        validateUniqueness(request.getUser().getUsername(), request.getNik(), request.getPhone());

        User savedUser = createAndSaveUser(request.getUser());

        Patient patient = createObjectPatient(request);
        Address address = createObjectAddress(request);

        patient.setAddress(address);
        patient.setUser(savedUser);
        Patient savedPatient = patientRepository.save(patient);

        return convertToPatientResponse(savedPatient);
    }

    @Transactional
    public CreatePatientResponse updatePatient(CreatePatientRequest request, UUID patientId) {
        log.info("Attempting to update patient with fullName {}", request.getFullName());

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isEmpty()) {
            log.warn("Id patient {} not found", patientId);
            throw new ResourceNotFoundException("Id patient " + patientId + " not found");
        }

        if (patientRepository.existsByNikAndIdNot(request.getNik(), patientId)) {
            log.warn("Patient updated failed. NIK {} already exists.", request.getNik());
            throw new IllegalArgumentException("NIK " + request.getNik() + " is already taken.");
        }

        if (patientRepository.existsByPhoneAndIdNot(request.getPhone(), patientId)) {
            log.warn("Patient updated failed. Phone {} already exists.", request.getPhone());
            throw new IllegalArgumentException("Phone " + request.getPhone() + " is already taken.");
        }

        return convertToPatientResponse(updatedPatient);
    }

    private User createAndSaveUser(CreatePatientRequest.UserRequest request) {
        User user = new User();
        user.setUsername(requestUser.getUsername());
        user.setPassword(passwordEncoder.encode(requestUser.getPassword()));
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

    private Address createObjectAddress(CreatePatientRequest request) {
        Address address = new Address();
        address.setCity(request.getAddress().getCity());
        address.setPostalCode(request.getAddress().getPostalCode());
        address.setStreet(request.getAddress().getStreet());

        return address;
    }

    private void validateUniqueness(String username, String nik, String phone) {
        userRepository.findByUsername(username).ifPresent(value -> {
            log.warn("Patient created failed. Username {} is already exists.", value.getUsername());
            throw new IllegalArgumentException("Username " + value.getUsername() + " is already taken.");
        });

        patientRepository.findByNik(nik).ifPresent(value -> {
            log.warn("Patient created failed. NIK {} is already exists.", value.getNik());
            throw new IllegalArgumentException("NIK " + value.getNik() + " is already taken.");
        });

        patientRepository.findByPhone(phone).ifPresent(value -> {
            log.warn("Patient created failed. Phone {} is already exists.", value.getPhone());
            throw new IllegalArgumentException("Phone " + value.getPhone() + " is already taken.");
        });
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
