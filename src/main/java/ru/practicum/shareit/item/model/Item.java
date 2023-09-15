package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * Класс вещи.
 */
@Data
@Builder(toBuilder = true)
public class Item {
    /**
     * Уникальный идентификатор.
     */
    private int id;
    /**
     * Краткое название.
     */
    private String name;
    /**
     * Развернутое описание.
     */
    private String description;
    /**
     * Статус о том, доступна или нет вещь для аренды.
     */
    private Boolean available;
    /**
     * Владелец вещи.
     */
    private User owner;
    /**
     * Если вещь была создана по запросу другого пользователя,
     * то в этом поле будет храниться ссылка на соответствующий запрос.
     */
    private ItemRequest request;
}
