package com.example.http_codes_examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    // 201 Created
    // A method for creating a new resource, such as a new user
    // If the user is successfully created, a 201 status is returned
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    // 401 Unauthorized
    // A method for accessing a protected resource, where it is necessary to verify if the user is authenticated
    // If the user is not authenticated, a 401 status is returned
    @GetMapping("/secure-data")
    public ResponseEntity<String> getSecureData(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authService.isAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        return ResponseEntity.ok("Secure data accessed");
    }

    //405 Method Not Allowed
    // A method for updating a resource
    // If the user tries to use the wrong HTTP method, such as GET instead of PUT, a 405 status is returned
    @PutMapping("/update-user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/update-user/{id}")
    public ResponseEntity<String> updateUserNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("GET method not allowed for this endpoint");
    }

    // 409 Conflict
    // A method for creating a new resource, where a conflict might occur, for example,
    // trying to create a user with a username that already exists
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    // 500 Internal Server Error
    // A method that causes an internal server error due to an unexpected exception during the processing of the request
    @GetMapping("/process-data")
    public ResponseEntity<String> processData() {
        try {
            // some processing logic
            int result = 10 / 0; // This will cause an ArithmeticException
            return ResponseEntity.ok("Data processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
        }
    }

}
