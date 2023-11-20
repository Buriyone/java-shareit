package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO-класс пользователя.
 */
@Data
@Builder(toBuilder = true)
public class UserDto {
    private int id;
    @NotBlank(message = "Имя или логин не может быть пустым или содержать пробелы.")
    @Size(max = 255, message = "Имя превышает допустимое количество символов.")
    private String name;
    @NotBlank(message = "Электронная почта не может быть пустой или содержать пробелы.")
    @Email(message = "Некорректно указана электронная почта.")
    private String email;
}
