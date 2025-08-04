package healthclinic.health_clinic.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.groups.Tuple.tuple;

import jakarta.validation.ConstraintViolation;

@SpringBootTest
public class createPatientRequestTest {

    @Autowired
    private Validator validator;

    @Test
    void createPatient() {
        Address alamatPasien = new Address();
        alamatPasien.setStreet("Jl. Siliwangi No. 45");
        alamatPasien.setCity("Bogor");
        alamatPasien.setPostalCode("16132");

        CreateUserRequest dataUser = new CreateUserRequest();
        dataUser.setUsername("dewi.lestari");
        dataUser.setPassword("Rahasia123!");

        CreatePatientRequest request = new CreatePatientRequest();

        request.setFullName("Dewi Lestari");
        request.setNik("3271015507980003");
        request.setDateOfBirth(LocalDate.of(1998, 7, 15));
        request.setAge(27);
        request.setPhone("081312345678");
        request.setGender("Wanita");
        request.setJob("Desainer Grafis");
        request.setPlaceOfBirth("Bogor");
        request.setWeight(58);
        request.setHeight(162);
        request.setBloodType("B");

        request.setAddress(alamatPasien);
        request.setUser(dataUser);

        Set<ConstraintViolation<CreatePatientRequest>> violations = validator.validate(request);
        Assertions.assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("gender", "Jenis kelamin harus diantara : MALE, FEMALE"),
                        tuple("bloodType", "Golongan darah harus diantara : A+, A-, B+, B-, AB+, AB-, O+, O-"));
    }

}
