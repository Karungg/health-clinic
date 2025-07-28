package healthclinic.health_clinic.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String fullName;
    private String sip;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private String placeOfBirth;
    private String phone;
    private String street;
    private String city;
    private String postalCode;
    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        fullName = "Dr. Miftah";
        sip = "123/abc/456/2023";
        dateOfBirth = LocalDate.of(2004, 11, 27);
        age = 20;
        gender = "Pria";
        placeOfBirth = "Bogor";
        phone = "081234567890";
        street = "Jl. Dramaga Raya No. 10";
        city = "Bogor";
        postalCode = "16680";
        username = "miftah";
        password = "password";
    }

    @Test
    void getDoctors() throws Exception {
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("[]")));
    }

    @Test
    void createDoctorSuccess() throws Exception {
        mockMvc.perform(
                post("/api/doctors")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)

                        .param("fullName", fullName)
                        .param("sip", sip)
                        .param("dateOfBirth", dateOfBirth.toString())
                        .param("age", age.toString())
                        .param("gender", gender)
                        .param("placeOfBirth", placeOfBirth)
                        .param("phone", phone)

                        .param("address.street", street)
                        .param("address.city", city)
                        .param("address.postalCode", postalCode)

                        .param("user.username", username)
                        .param("user.password", password))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(Matchers.containsString("Doctor with name " + fullName + " successfully created.")));
    }

}
