package healthclinic.health_clinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private String fullName;
        private String nik;
        private LocalDate dateOfBirth;
        private Integer age;
        private Integer weight;
        private Integer height;
        private String gender;
        private String job;
        private String placeOfBirth;
        private String bloodType;
        private String phone;
        private String street;
        private String city;
        private String postalCode;
        private String username;
        private String password;

        @BeforeEach
        void setUp() {
                fullName = "Budi";
                nik = "3271052711040001";
                dateOfBirth = LocalDate.of(2004, 11, 27);
                age = 20;
                weight = 65;
                height = 172;
                gender = "Pria";
                job = "Mahasiswa";
                placeOfBirth = "Bogor";
                bloodType = "O";
                phone = "081234567890";
                street = "Jl. Dramaga Raya No. 10";
                city = "Bogor";
                postalCode = "16680";
                username = "budi";
                password = "password";
        }

        @Test
        void getPatients() throws Exception {
                mockMvc.perform(get("/api/patients"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString("[]")));
        }

        @Test
        void createPatientSuccess() throws Exception {
                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)

                                                .param("fullName", fullName)
                                                .param("nik", nik)
                                                .param("dateOfBirth", dateOfBirth.toString())
                                                .param("age", age.toString())
                                                .param("gender", gender)
                                                .param("job", job)
                                                .param("placeOfBirth", placeOfBirth)
                                                .param("weight", weight.toString())
                                                .param("height", height.toString())
                                                .param("bloodType", bloodType)
                                                .param("phone", phone)

                                                .param("address.street", street)
                                                .param("address.city", city)
                                                .param("address.postalCode", postalCode)

                                                .param("user.username", username)
                                                .param("user.password", password))
                                .andExpectAll(
                                                status().isOk(),
                                                content().string(Matchers.containsString(
                                                                "Patient with name " + fullName
                                                                                + " successfully created.")));
        }

        @Test
        void createPatientError() throws Exception {
                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)

                                                // Full name is null
                                                .param("nik", nik)
                                                .param("dateOfBirth", dateOfBirth.toString())
                                                .param("age", age.toString())
                                                .param("gender", gender)
                                                .param("job", job)
                                                .param("placeOfBirth", placeOfBirth)
                                                .param("weight", weight.toString())
                                                .param("height", height.toString())
                                                .param("bloodType", bloodType)
                                                .param("phone", phone)

                                                .param("address.street", street)
                                                .param("address.city", city)
                                                .param("address.postalCode", postalCode)

                                                .param("user.username", username)
                                                .param("user.password", password))
                                .andExpectAll(
                                                status().isBadRequest(),
                                                content().string(Matchers.containsString(
                                                                "Nama lengkap harus diisi")));
        }

}
