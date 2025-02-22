package com.kitchensaver.backend.Controller;

import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest request) {
        String response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}