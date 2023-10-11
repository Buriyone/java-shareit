package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO-класс пользователя, копирует поля {@link User}.
 */
@Data
@Builder(toBuilder = true)
public class UserDto {
    private int id;
    @NotBlank(message = "Имя или логин не может быть пустым или содержать пробелы.", groups = {Create.class})
    private String name;
    @NotBlank(message = "Электронная почта не может быть пустой или содержать пробелы.", groups = {Create.class})
    @Email(message = "Некорректно указана электронная почта.", groups = {Update.class, Create.class})
    private String email;
}
