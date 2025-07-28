package healthclinic.health_clinic.services;

import java.util.List;
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
                doctor.getUser().getId(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt());
    }

}
