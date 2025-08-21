package healthclinic.health_clinic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.GenericResponse;
import healthclinic.health_clinic.dto.UserResponse;
import healthclinic.health_clinic.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/api/users")
    public ResponseEntity<GenericResponse<List<UserResponse>>> getUsers() {

        GenericResponse<List<UserResponse>> response = GenericResponse.ok(userService.findAllUsers());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/api/users/{userId}")
    public ResponseEntity<GenericResponse<UserResponse>> getUserById(
            @PathVariable(name = "userId", required = true) UUID userId) {

        GenericResponse<UserResponse> response = GenericResponse.ok(userService.getUserById(userId));

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/api/users")
    public ResponseEntity<GenericResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {

        UserResponse createdUser = userService.createUser(request);

        GenericResponse<UserResponse> response = GenericResponse.created(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/api/users/{userId}")
    public ResponseEntity<GenericResponse<UserResponse>> updateUser(
            @PathVariable(name = "userId", required = true) UUID userId,
            @RequestBody @Valid CreateUserRequest request) {

        UserResponse updatedUser = userService.updateUser(userId, request);

        GenericResponse<UserResponse> response = GenericResponse.ok(updatedUser);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(path = "/api/users/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "userId", required = true) UUID userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
