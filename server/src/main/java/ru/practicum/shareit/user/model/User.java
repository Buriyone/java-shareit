package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder(toBuilder = true)
public class User {
    /**
     * Уникальный идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Имя или логин.
     */
    @NotBlank(message = "Имя или логин не может быть пустым или содержать пробелы.")
    @Size(max = 255, message = "Имя превышает допустимое количество символов.")
    private String name;
    /**
     * Электронная почта.
     */
    @Column(unique = true)
    @NotBlank(message = "Электронная почта не может быть пустой или содержать пробелы.")
    @Email(message = "Некорректно указана электронная почта.")
    private String email;
}
