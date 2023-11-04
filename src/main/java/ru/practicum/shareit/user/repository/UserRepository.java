package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

/**
 * Хранилище для {@link User}
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailContainingIgnoreCase(String email);
}
