package com.kitchensaver.backend.Service;

import com.kitchensaver.backend.DTO.LoginRequest;
import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.model.User;
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
     * This method registers a new user.
     *
     * @param request The user's details.
     * @return A message saying if the registration was successful or not.
     */
    public String registerUser(UserRequest request) {

        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        // Check if email is already taken
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        // Create a new user and fill in their details
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCell(request.getCell());
        user.setOffice(request.getOffice());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt the password

        // Save the user in the database
        userRepo.save(user);
        return "User registered successfully!";
    }


    /**
     * This method handles user login.
     *
     * @param request The user's login details.
     * @return A message saying if login was successful or not.
     */
    public Map<String, String> loginUser(LoginRequest request) {
        Optional<User> userOptional = userRepo.findByEmail(request.getEmail()); // Find user by email

        // If user is not found, return an error message
        if (userOptional.isEmpty()) {
            return Map.of("message", "User not found!");
        }

        User user = userOptional.get();
        // Check if the password is correct
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return Map.of("message", "Invalid credentials!");
        }

        // Return success message and the user's role
        return Map.of(
                "message", "Login successful!",
                "role", user.getRole()
        );
    }

}
