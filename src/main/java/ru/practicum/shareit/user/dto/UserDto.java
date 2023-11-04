package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

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
    @NotBlank(message = "Имя или логин не может быть пустым или содержать пробелы.", groups = {Create.class})
    @Size(max = 255, message = "Имя превышает допустимое количество символов.")
    private String name;
    @NotBlank(message = "Электронная почта не может быть пустой или содержать пробелы.", groups = {Create.class})
    @Email(message = "Некорректно указана электронная почта.", groups = {Update.class, Create.class})
    private String email;
}
