package healthclinic.health_clinic.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import healthclinic.health_clinic.dto.Address;
import healthclinic.health_clinic.models.Patient;
import healthclinic.health_clinic.models.User;

@DataJpaTest
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createPatientSuccess() {
        User user = new User();
        user.setUsername("User 1");
        user.setPassword("password");

        userRepository.save(user);

        Address address = new Address();
        address.setCity("Bogor");
        address.setPostalCode(16116);
        address.setStreet("Dramaga");
        LocalDate dateOfBirth = LocalDate.of(2004, 11, 27);

        Patient patient = new Patient();
        patient.setFullName("pasien 1");
        patient.setAge(20);
        patient.setBloodType("O");
        patient.setDateOfBirth(Date.valueOf(dateOfBirth));
        patient.setGender("Pria");
        patient.setHeight(170);
        patient.setJob("Programmer");
        patient.setNik("123456789123456");
        patient.setPhone("123456789");
        patient.setPlaceOfBirth("Bogor");
        patient.setWeight(55);
        patient.setUser(user);
        patient.setAddress(address);

        patientRepository.save(patient);

        List<Patient> patient2 = patientRepository.findAll();
        assertNotNull(patient2);
    }

}
