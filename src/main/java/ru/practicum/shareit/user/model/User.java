package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * Класс пользователя.
 */
@Data
@Builder(toBuilder = true)
public class User {
    /**
     * Уникальный идентификатор.
     */
    private int id;
    /**
     * Имя или логин.
     */
    private String name;
    /**
     * Электронная почта.
     */
    @Email(message = "Email введен некорректно.")
    private String email;
}
