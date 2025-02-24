package com.kitchensaver.backend.Controller;

import com.kitchensaver.backend.DTO.LoginRequest;
import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// This class is a controller that handles user-related actions
@Service
@RestController
@RequestMapping("/api/user")//all requests to "/api/user" will come here
public class UserController {
    private final UserService userService; //handles user-related logic


    // set up controller & connect it to the UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * This method is for user registration.
     *
     * @param request The user details sent from the frontend.
     * @return A message saying if registration was successful.
     */
    @PostMapping("/register")  // This runs when someone sends a POST request to "/api/user/register"
    public ResponseEntity<String> registerUser(@RequestBody UserRequest request) {
        String response = userService.registerUser(request); // Calls the service to register the user
        return ResponseEntity.ok(response); // Sends back a success message
    }


    /**
     * This method is for user login.
     *
     * @param request The login details (email & password) sent from the frontend.
     * @return A response with login details (like a token).
     */
    @PostMapping("/login") // This runs when someone sends a POST request to "/api/user/login"
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest request) {
        Map<String, String> response = userService.loginUser(request);  // Calls the service to check login details
        return ResponseEntity.ok(response); // Sends back login information
    }

}