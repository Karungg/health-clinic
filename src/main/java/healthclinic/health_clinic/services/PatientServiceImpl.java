package healthclinic.health_clinic.services;

import java.util.List;
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

    @Transactional
    public PatientResponse updatePatient(CreatePatientRequest request, UUID patientId) {
        log.info("Attempting to update patient with full name {}", request.getFullName());

        Patient patientToUpdate = patientRepository.findById(patientId).orElseThrow(() -> {
            log.warn("Failed to update. Patient with ID {} not found", patientId);
            throw new ResourceNotFoundException("Patient with ID " + patientId + " not found");
        });

        validateUniquenessForUpdate(request, patientToUpdate.getUser().getId(), patientToUpdate.getId());

        mapDtoToEntity(request, patientToUpdate);

        User userToUpdate = patientToUpdate.getUser();
        userToUpdate.setUsername(request.getUser().getUsername());
        if (userToUpdate.getPassword() != null && !request.getUser().getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(request.getUser().getPassword()));
        }

        Patient updatedPatient = patientRepository.save(patientToUpdate);
        log.info("Patient with ID {} successfully updated", updatedPatient.getId());

        return convertToPatientResponse(updatedPatient);
    }

    @Transactional
    public void deletePatient(UUID patientId) {
        patientRepository.findById(patientId).orElseThrow(() -> {
            log.warn("Failed to delete. Patient with ID {} not found", patientId);
            throw new ResourceNotFoundException("Patient with ID " + patientId + " not found");
        });

        patientRepository.deleteById(patientId);
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

    private void validateUniquenessForUpdate(CreatePatientRequest request, UUID userId, UUID patientId) {
        if (userRepository.existsByUsernameAndIdNot(request.getUser().getUsername(), userId)) {
            log.warn("Failed to update patient, username {} is already exists", request.getUser().getUsername());
            throw new IllegalArgumentException("Username " + request.getUser().getUsername() + " is already taken.");
        }

        if (patientRepository.existsByNikAndIdNot(request.getNik(), patientId)) {
            log.warn("Failed to update patient, nik {} is already exists", request.getNik());
            throw new IllegalArgumentException("nik " + request.getNik() + " is already taken.");
        }

        if (patientRepository.existsByPhoneAndIdNot(request.getPhone(), patientId)) {
            log.warn("Failed to update patient, phone {} is already exists", request.getPhone());
            throw new IllegalArgumentException("phone " + request.getPhone() + " is already taken.");
        }
    }

    private void mapDtoToEntity(CreatePatientRequest request, Patient patientToUpdate) {
        patientToUpdate.setFullName(request.getFullName());
        patientToUpdate.setNik(request.getNik());
        patientToUpdate.setDateOfBirth(request.getDateOfBirth());
        patientToUpdate.setAge(request.getAge());
        patientToUpdate.setPhone(request.getPhone());
        patientToUpdate.setGender(request.getGender());
        patientToUpdate.setJob(request.getJob());
        patientToUpdate.setPlaceOfBirth(request.getPlaceOfBirth());
        patientToUpdate.setWeight(request.getWeight());
        patientToUpdate.setHeight(request.getHeight());
        patientToUpdate.setBloodType(request.getBloodType());

        Address address = new Address();
        address.setCity(request.getAddress().getCity());
        address.setStreet(request.getAddress().getStreet());
        address.setPostalCode(request.getAddress().getPostalCode());

        patientToUpdate.setAddress(address);
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
