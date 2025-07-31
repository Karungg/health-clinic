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
    public GenericResponse<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return GenericResponse.badRequest(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericResponse<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return GenericResponse.notFound(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<Map<String, List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();

        System.out.println(exception.getBindingResult().getAllErrors().toString());

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        return GenericResponse.badRequest(errors);
    }
}
