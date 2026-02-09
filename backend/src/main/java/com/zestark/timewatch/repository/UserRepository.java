package com.zestark.timewatch.repository;

import com.zestark.timewatch.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 *
 * <p>Provides database access methods for user-related operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Finds a user by username.
   *
   * @param username the username to search for
   * @return an Optional containing the user if found, empty otherwise
   */
  Optional<User> findByUsername(String username);

  /**
   * Finds a user by email.
   *
   * @param email the email to search for
   * @return an Optional containing the user if found, empty otherwise
   */
  Optional<User> findByEmail(String email);

  /**
   * Checks if a user with the given username exists.
   *
   * @param username the username to check
   * @return true if a user with the username exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Checks if a user with the given email exists.
   *
   * @param email the email to check
   * @return true if a user with the email exists, false otherwise
   */
  boolean existsByEmail(String email);
}
