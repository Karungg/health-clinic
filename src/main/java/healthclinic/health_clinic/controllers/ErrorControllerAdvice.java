package healthclinic.health_clinic.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.lang.IllegalArgumentException;

import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, String> errors = Map.of("error", exception.getMessage());

        return errors;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<Map<String, List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();

        exception.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        return GenericResponse.badRequest(errors);
    }
}
