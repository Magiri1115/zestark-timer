package com.zestark.timewatch.service;

import com.zestark.timewatch.domain.model.User;
import com.zestark.timewatch.exception.DuplicateResourceException;
import com.zestark.timewatch.exception.ResourceNotFoundException;
import com.zestark.timewatch.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for User-related business logic.
 *
 * <p>Handles user creation, retrieval, and management operations.
 */
@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Creates a new user.
   *
   * @param username the username for the new user
   * @param email the email for the new user
   * @param passwordHash the hashed password for the new user
   * @return the created user
   * @throws DuplicateResourceException if username or email already exists
   */
  public User createUser(String username, String email, String passwordHash) {
    if (userRepository.existsByUsername(username)) {
      throw new DuplicateResourceException("Username already exists: " + username);
    }
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateResourceException("Email already exists: " + email);
    }

    User newUser = new User(username, email, passwordHash);
    return userRepository.save(newUser);
  }

  /**
   * Finds a user by ID.
   *
   * @param userId the ID of the user to find
   * @return the found user
   * @throws ResourceNotFoundException if user is not found
   */
  @Transactional(readOnly = true)
  public User findUserById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
  }

  /**
   * Finds a user by username.
   *
   * @param username the username to search for
   * @return the found user
   * @throws ResourceNotFoundException if user is not found
   */
  @Transactional(readOnly = true)
  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with username: "
            + username));
  }

  /**
   * Finds a user by email.
   *
   * @param email the email to search for
   * @return the found user
   * @throws ResourceNotFoundException if user is not found
   */
  @Transactional(readOnly = true)
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
  }

  /**
   * Gets all users.
   *
   * @return a list of all users
   */
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Updates a user's email.
   *
   * @param userId the ID of the user to update
   * @param newEmail the new email address
   * @return the updated user
   * @throws ResourceNotFoundException if user is not found
   * @throws DuplicateResourceException if new email already exists
   */
  public User updateUserEmail(UUID userId, String newEmail) {
    User existingUser = findUserById(userId);
    if (!existingUser.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
      throw new DuplicateResourceException("Email already exists: " + newEmail);
    }
    existingUser.setEmail(newEmail);
    return userRepository.save(existingUser);
  }

  /**
   * Deletes a user.
   *
   * @param userId the ID of the user to delete
   * @throws ResourceNotFoundException if user is not found
   */
  public void deleteUser(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User not found with ID: " + userId);
    }
    userRepository.deleteById(userId);
  }
}
