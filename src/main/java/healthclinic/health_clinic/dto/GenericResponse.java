package healthclinic.health_clinic.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {

    private Integer code;
    private String status;
    private T Data;

    public static <T> GenericResponse<T> ok(T data) {
        return new GenericResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                data);
    }

    public static <T> GenericResponse<T> created(T data) {
        return new GenericResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                data);
    }
}
