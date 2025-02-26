package com.kitchensaver.backend.Controller;

import com.kitchensaver.backend.DTO.LoginRequest;
import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.Repo.UserRepo;
import com.kitchensaver.backend.Service.UserService;
import com.kitchensaver.backend.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


// This class is a controller that handles user-related actions
@RestController
@RequestMapping("/api/user") // all requests to "/api/user" will come here
public class UserController {
    private final UserService userService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    // Constructor-based dependency injection
    public UserController(UserService userService, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method is for user registration.
     *
     * @param request The user details sent from the frontend.
     * @return A message saying if registration was successful.
     */
    @PostMapping("/register") // This handles the POST request to "/api/user/register"
    public ResponseEntity<String> registerUser(@RequestBody UserRequest request) {
        // Calls the service to register the user and returns a response message
        String response = userService.registerUser(request);
        return ResponseEntity.ok(response); // Sends back the result of the registration
    }

    /**
     * This method is for user login.
     *
     * @param request The login details (email & password) sent from the frontend.
     * @return A response with login details (like a token).
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest request) {
        // Looks for the user by their email
        Optional<Users> userOptional = userRepo.findByEmail(request.getEmail());

        // If no user is found, return an error message
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found!"));
        }

        Users user = userOptional.get();
        // Checks if the password entered matches the password in the database
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials!"));
        }

        // If login is successful, send back a success message with the user's role
        return ResponseEntity.ok(Map.of(
                "message", "Login successful!",
                "role", user.getRole().name()
        ));
    }
}