package healthclinic.health_clinic.dto;

import java.time.LocalDate;

import healthclinic.health_clinic.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequest {

    private Address address;

    private User user;

    @NotBlank(message = "{patient.fullname.notblank}")
    @Size(min = 3, message = "{patient.fullname.size}")
    private String fullName;

    @NotBlank(message = "{patient.nik.notblank}")
    @Size(min = 16, max = 16, message = "{patient.nik.size}")
    private String nik;

    @NotNull(message = "{patient.dateOfBirth.notnull}")
    private LocalDate dateOfBirth;

    @NotBlank(message = "{patient.age.notblank}")
    @Size(max = 3, message = "{patient.age.size}")
    private String age;

    @NotBlank(message = "{patient.phone.notblank}")
    @Size(min = 9, max = 14, message = "{patient.phone.size}")
    private String phone;

    @NotBlank(message = "{patient.gender.notblank}")
    @Size(max = 6, message = "{patient.gender.size}")
    private String gender;

    @NotBlank(message = "{patient.job.notblank}")
    @Size(min = 3, message = "{patient.job.size}")
    private String job;

    @NotBlank(message = "{patient.placeofbirth.notblank}")
    @Size(min = 3, message = "{patient.placeofbirth.size}")
    private String placeOfBirth;

    @NotBlank(message = "{patient.weight.notblank}")
    @Size(max = 3, message = "{patient.weight.size}")
    private String weight;

    @NotBlank(message = "{patient.height.notblank}")
    @Size(max = 3, message = "{patient.height.size}")
    private String height;

    @NotBlank(message = "{patient.bloodtype.notblank}")
    @Size(max = 2, message = "{patient.bloodtype.size}")
    private String bloodType;
}
