package com.kitchensaver.backend.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// This class sets up security settings for the application
@Configuration
public class SecurityConfig {


    // This controls who can access the app
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Turns off extra security checks for testing
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Lets anyone use the app without logging in for testing purposes only

        return http.build(); // Saves the settings
    }


    // This helps keep passwords safe
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Turns passwords into scrambled text
    }
}
