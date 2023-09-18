package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

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
    private String email;
}
