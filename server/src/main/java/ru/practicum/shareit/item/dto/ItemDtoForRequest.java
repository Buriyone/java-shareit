package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс вещи для {@link Item} под {@link ItemRequestDto}.
 */
@Data
@Builder(toBuilder = true)
public class ItemDtoForRequest {
    @NotNull(message = "Уникальный идентификатор не может отсутствовать.")
    private int id;
    @NotBlank(message = "Название вещи не может быть пустым или содержать пробелы.")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым или содержать пробелы.")
    private String description;
    @NotNull(message = "Статус не может отсутствовать.")
    private Boolean available;
    @NotNull(message = "Уникальный идентификатор запроса не может отсутствовать.")
    private int requestId;
}
