package com.kitchensaver.backend.Repo;

import com.kitchensaver.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// connection to database to get user information
public interface UserRepo  extends JpaRepository<Users, Long> {


    /**
     * Method to find a user by their email.
     *
     * @param email The user's email.
     * @return The user if found, or nothing if not found.
     */
    Optional<Users> findByEmail(String email);
}
