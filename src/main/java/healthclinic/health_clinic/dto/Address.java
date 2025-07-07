package healthclinic.health_clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Address {

    @Size(min = 3, message = "{address.city.size}")
    @NotBlank(message = "{address.city.notblank}")
    private String city;

    @Size(min = 3, message = "{address.postalcode.size}")
    @NotBlank(message = "{address.postalcode.notblank}")
    private Integer postalCode;

    @Size(min = 3, message = "{address.street.size}")
    @NotBlank(message = "{address.street.notblank}")
    private String street;

}
