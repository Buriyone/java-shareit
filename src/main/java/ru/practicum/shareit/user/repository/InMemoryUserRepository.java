package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс репозитория пользователей.
 */
public interface InMemoryUserRepository {
    User add(User user);

    User update(User user);

    void delete(int userId);

    User getById(int userId);

    List<User> getAll();

    Boolean userChecker(int id);

    Boolean emailValidation(String email);

    Boolean emailAvailability(String email, int id);
}
