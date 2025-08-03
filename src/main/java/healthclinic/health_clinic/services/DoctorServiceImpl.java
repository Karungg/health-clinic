package healthclinic.health_clinic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthclinic.health_clinic.Enums.Role;
import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.dto.CreateDoctorRequest;
import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.DoctorResponse;
import healthclinic.health_clinic.exception.ResourceNotFoundException;
import healthclinic.health_clinic.models.Doctor;
import healthclinic.health_clinic.models.User;
import healthclinic.health_clinic.repository.DoctorRepository;
import healthclinic.health_clinic.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<DoctorResponse> findAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDoctorResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        log.info("Attempting to create doctor with name {}", request.getFullName());

        validateUniqueness(request.getSip(), request.getPhone(), request.getUser().getUsername());

        User savedUser = createUserAndSave(request.getUser());
        Address address = createAddressObject(request.getAddress());
        Doctor doctor = createDoctorObject(request);

        doctor.setAddress(address);
        doctor.setUser(savedUser);

        doctorRepository.save(doctor);
        log.info("Doctor with name {} successfully created", doctor.getFullName());

        return convertToDoctorResponse(doctor);
    }

    @Transactional
    public DoctorResponse updateDoctor(CreateDoctorRequest request, UUID doctorId) {
        log.info("Attempting to create doctor with name {}", request.getFullName());

        Doctor doctorToUpdate = doctorRepository.findById(doctorId).orElseThrow(() -> {
            log.warn("Failed to update. Doctor with ID {} not found", doctorId);
            throw new ResourceNotFoundException("Doctor with ID " + doctorId + " not found");
        });

        validateUniquenessForUpdate(request, doctorToUpdate.getUser().getId(), doctorToUpdate.getId());

        mapDtoToEntity(request, doctorToUpdate);

        User userToUpdate = doctorToUpdate.getUser();
        userToUpdate.setUsername(request.getUser().getUsername());
        if (userToUpdate.getPassword() != null && !request.getUser().getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(request.getUser().getPassword()));
        }

        Doctor updatedDoctor = doctorRepository.save(doctorToUpdate);
        log.info("Doctor with ID {} successfully updated", updatedDoctor.getId());

        return convertToDoctorResponse(updatedDoctor);
    }

    @Transactional
    public void deleteDoctor(UUID doctorId) {
        log.info("Attempting to delete doctor with ID {}", doctorId);

        if (!doctorRepository.existsById(doctorId)) {
            log.info("Failed to delete, doctor with ID {} is doesn't exists.", doctorId);
            throw new ResourceNotFoundException("Doctor with ID " + doctorId + " not found.");
        }

        doctorRepository.deleteById(doctorId);
        log.info("Doctor with ID {} successfully deleted", doctorId);
    }

    private User createUserAndSave(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_DOCTOR);
        return userRepository.save(user);
    }

    private Address createAddressObject(Address request) {
        Address address = new Address();
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setStreet(request.getStreet());

        return address;
    }

    private Doctor createDoctorObject(CreateDoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setAge(request.getAge());
        doctor.setFullName(request.getFullName());
        doctor.setGender(request.getGender());
        doctor.setPhone(request.getPhone());
        doctor.setPlaceOfBirth(request.getPlaceOfBirth());
        doctor.setSip(request.getSip());
        doctor.setDateOfBirth(request.getDateOfBirth());

        return doctor;
    }

    private void validateUniqueness(String sip, String phone, String username) {
        doctorRepository.findBySipEquals(sip).ifPresent(d -> {
            log.info("Failed to create doctor. SIP : {} is already exists", sip);
            throw new IllegalArgumentException("SIP " + sip + " is already taken.");
        });

        doctorRepository.findByPhoneEquals(phone).ifPresent(d -> {
            log.info("Failed to create doctor. Phone : {} is already exists", phone);
            throw new IllegalArgumentException("Phone " + phone + " is already taken.");
        });

        userRepository.findByUsernameEquals(username).ifPresent(d -> {
            log.info("Failed to create doctor. Username : {} is already exists", username);
            throw new IllegalArgumentException("Username " + username + " is already taken.");
        });
    }

    private void validateUniquenessForUpdate(CreateDoctorRequest request, UUID userId, UUID doctorId) {
        if (userRepository.existsByUsernameAndIdNot(request.getUser().getUsername(), userId)) {
            log.warn("Failed to update doctor, username {} is already exists", request.getUser().getUsername());
            throw new IllegalArgumentException("Username " + request.getUser().getUsername() + " is already taken.");
        }

        if (doctorRepository.existsBySipAndIdNot(request.getSip(), doctorId)) {
            log.warn("Failed to update doctor, sip {} is already exists", request.getSip());
            throw new IllegalArgumentException("nik " + request.getSip() + " is already taken.");
        }

        if (doctorRepository.existsByPhoneAndIdNot(request.getPhone(), doctorId)) {
            log.warn("Failed to update doctor, phone {} is already exists", request.getPhone());
            throw new IllegalArgumentException("phone " + request.getPhone() + " is already taken.");
        }
    }

    private void mapDtoToEntity(CreateDoctorRequest request, Doctor doctorToUpdate) {
        doctorToUpdate.setFullName(request.getFullName());
        doctorToUpdate.setSip(request.getSip());
        doctorToUpdate.setDateOfBirth(request.getDateOfBirth());
        doctorToUpdate.setAge(request.getAge());
        doctorToUpdate.setPhone(request.getPhone());
        doctorToUpdate.setGender(request.getGender());
        doctorToUpdate.setPlaceOfBirth(request.getPlaceOfBirth());

        Address address = new Address();
        address.setCity(request.getAddress().getCity());
        address.setStreet(request.getAddress().getStreet());
        address.setPostalCode(request.getAddress().getPostalCode());

        doctorToUpdate.setAddress(address);
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
