package healthclinic.health_clinic.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import healthclinic.health_clinic.exception.ResourceNotFoundException;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

}
