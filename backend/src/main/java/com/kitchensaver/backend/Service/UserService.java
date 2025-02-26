package com.kitchensaver.backend.Service;

import com.kitchensaver.backend.DTO.LoginRequest;
import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.model.Role;
import com.kitchensaver.backend.model.Users;
import com.kitchensaver.backend.Repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;



// This class handles user-related tasks
@Service
public class UserService {
    private final UserRepo userRepo; // This helps save and find users in the database
    private final BCryptPasswordEncoder passwordEncoder; // This helps secure passwords



    // Setting up the service and password encoder
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    /**
     * This method is used to register a new user.
     *
     * @param request The user's details (e.g., name, email, password).
     * @return A message saying whether the registration was successful or not.
     */
    public String registerUser(UserRequest request) {

        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }


        // Check if email already exists in the database
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        // Create a new user object with the details from the request
        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCell(request.getCell());
        user.setOffice(request.getOffice());

        // Assign role (validate input)
        try {
            user.setRole(Role.valueOf(request.getRole().toUpperCase())); // Convert the role to uppercase and set it
        } catch (IllegalArgumentException e) {
            return "Invalid role! Allowed values: ADMIN, CABINET_MAKER_INSTALLER";
        }


        // Encrypt the password before saving it
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        // Save the new user to the database
        userRepo.save(user);
        return "User registered successfully!";
    }



    /**
     * This method handles user login by checking if the email and password are correct.
     *
     * @param request The login details (email and password).
     * @return A message saying whether the login was successful or not.
     */
    public Map<String, String> loginUser(LoginRequest request) {

        // Try to find the user by their email
        Optional<Users> userOptional = userRepo.findByEmail(request.getEmail()); // Find user by email


        // If the user is not found, return a message
        if (userOptional.isEmpty()) {
            return Map.of("message", "User not found!");
        }


        // If the user is found, check if the password matches
        Users user = userOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return Map.of("message", "Invalid credentials!");
        }


        // If login is successful, return a success message with the user's role
        return Map.of(
                "message", "Login successful!",
                "role", user.getRole().name()
        );
    }
}
