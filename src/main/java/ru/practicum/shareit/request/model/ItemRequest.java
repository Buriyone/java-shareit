package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс, отвечающий за запрос вещи.
 */
@Data
@Builder(toBuilder = true)
public class ItemRequest {
    /**
     * Уникальный идентификатор запроса.
     */
    private int id;
    /**
     * Текст запроса, содержащий описание требуемой вещи.
     */
    private String description;
    /**
     * Пользователь, создавший запрос.
     */
    private User requestor;
    /**
     * Дата и время создания запроса.
     */
    private LocalDateTime created;
}
