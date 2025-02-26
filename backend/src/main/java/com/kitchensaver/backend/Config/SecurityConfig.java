package com.kitchensaver.backend.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// This class sets up security settings for the application
@Configuration
public class SecurityConfig {


    //controls who can access the app
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Turns off CSRF protection (helps with API security)
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register", "/api/user/login").permitAll() // Allow registration without authentication
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN") // Only Admins can access
                        .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "CABINET_MAKER_INSTALLER") // Both roles can access
                        .anyRequest().authenticated()
                )
                // This just says we don't need to save security info between requests
                .securityContext(securityContext -> securityContext.requireExplicitSave(false))
                // Makes the app stateless, meaning no sessions are stored (good for API security)
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS));

        return http.build();
    }

    //password encoder to safely store passwords
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
