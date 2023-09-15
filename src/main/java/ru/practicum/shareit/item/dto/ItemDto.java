package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс вещи, копирует поля {@link Item}.
 */
@Data
@Builder(toBuilder = true)
public class ItemDto {
    private int id;
    @NotNull(message = "Название не может отсутствовать.")
    @NotBlank(message = "Название вещи не может быть пустым или содержать пробелы.")
    private String name;
    @NotNull(message = "Описание не может отсутствовать.")
    @NotBlank(message = "Описание вещи не может быть пустым или содержать пробелы.")
    private String description;
    @NotNull(message = "Статус не может отсутствовать.")
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
