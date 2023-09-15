package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс пользователя, копирует поля {@link User}.
 */
@Data
@Builder(toBuilder = true)
public class UserDto {
    private int id;
    @NotNull(message = "Имя или логин не может отсутствовать.")
    @NotBlank(message = "Имя или логин не может быть пустым или содержать пробелы.")
    private String name;
    @NotNull(message = "Электронная почта не может отсутствовать.")
    @Email(message = "Некорректно указана электронная почта.")
    private String email;
}
