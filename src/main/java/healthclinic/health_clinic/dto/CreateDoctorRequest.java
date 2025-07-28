package healthclinic.health_clinic.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorRequest {

    private Address address;

    private CreateUserRequest user;

    @NotBlank(message = "{doctor.fullname.notblank}")
    @Size(min = 3, message = "{doctor.fullname.size}")
    private String fullName;

    @NotBlank(message = "{doctor.sip.notblank}")
    @Size(min = 16, max = 16, message = "{doctor.sip.size}")
    private String sip;

    @NotNull(message = "{doctor.dateOfBirth.notnull}")
    private LocalDate dateOfBirth;

    @NotNull(message = "{doctor.age.notnull}")
    @Min(value = 0, message = "{doctor.age.min}")
    @Max(value = 999, message = "{doctor.age.max}")
    private Integer age;

    @NotBlank(message = "{doctor.phone.notblank}")
    @Size(min = 9, max = 14, message = "{doctor.phone.size}")
    private String phone;

    @NotBlank(message = "{doctor.gender.notblank}")
    @Size(max = 6, message = "{doctor.gender.size}")
    private String gender;

    @NotBlank(message = "{doctor.placeofbirth.notblank}")
    @Size(min = 3, message = "{doctor.placeofbirth.size}")
    private String placeOfBirth;
}
