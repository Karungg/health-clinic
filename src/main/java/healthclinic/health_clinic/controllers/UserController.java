package healthclinic.health_clinic.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import healthclinic.health_clinic.dto.CreateUserRequest;
import healthclinic.health_clinic.dto.CreateUserResponse;
import healthclinic.health_clinic.services.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreateUserResponse>> getUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @PostMapping(path = "/api/users", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@ModelAttribute @Valid CreateUserRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(v -> v.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors.toString());
        }

        CreateUserResponse response = userService.createUser(request);

        return ResponseEntity.ok().body("User with username " + response.getUsername() + " successfully created");
    }

    @PutMapping(path = "/api/users/{userId}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@PathVariable(name = "userId", required = true) UUID userId,
            @ModelAttribute @Valid CreateUserRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(v -> v.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors.toString());
        }

        CreateUserResponse response = userService.updateUser(userId, request);

        return ResponseEntity.ok().body("User with username " + response.getUsername() + " successfully updated");
    }

    @DeleteMapping(path = "/api/users/{userId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId", required = true) UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
