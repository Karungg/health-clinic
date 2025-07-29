package healthclinic.health_clinic.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {

    private Integer code;
    private String status;
    private T Data;
    private Object errors;

    public static <T> GenericResponse<T> success(Integer code, String status, T data) {
        return GenericResponse.<T>builder()
                .code(code)
                .status(status)
                .Data(data)
                .build();
    }

    public static <T> GenericResponse<T> error(Integer code, String status, Object errors) {
        return GenericResponse.<T>builder()
                .code(code)
                .status(status)
                .errors(errors)
                .build();
    }

    public static <T> GenericResponse<T> ok(T data) {
        return success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> GenericResponse<T> created(T data) {
        return success(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), data);
    }

    public static <T> GenericResponse<T> badRequest(Map<String, List<String>> errors) {
        return error(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }
}
