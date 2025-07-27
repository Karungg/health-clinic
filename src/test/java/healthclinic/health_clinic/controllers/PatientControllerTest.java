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
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void getPatients() throws Exception {
                mockMvc.perform(get("/api/patients"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(Matchers.containsString("[]")));
        }

        @Test
        void createPatientSuccess() throws Exception {
                String fullName = "Pasien 1";
                LocalDate dateOfBirth = LocalDate.of(2004, 11, 27);

                mockMvc.perform(
                                post("/api/patients")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                .accept(MediaType.APPLICATION_JSON_VALUE)

                                                .param("fullName", fullName)
                                                .param("nik", "1234567890123456")
                                                .param("dateOfBirth", dateOfBirth.toString())
                                                .param("age", "20")
                                                .param("gender", "Pria")
                                                .param("job", "Programmer")
                                                .param("placeOfBirth", "Bogor")
                                                .param("weight", "55")
                                                .param("height", "166")
                                                .param("bloodType", "O")
                                                .param("phone", "81234567890")

                                                .param("address.street", "Dramaga")
                                                .param("address.city", "Bogor")
                                                .param("address.postalCode", "16116")

                                                .param("user.username", "user 1")
                                                .param("user.password", "password"))
                                .andExpectAll(
                                                status().isOk(),
                                                content().string(Matchers.containsString(
                                                                "Patient with name " + fullName
                                                                                + " successfully created.")));
        }

}
