package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс вещи для {@link Item} под {@link ItemRequestDto}.
 */
@Data
@Builder(toBuilder = true)
public class ItemDtoForRequest {
    @NotNull(message = "Уникальный идентификатор не может отсутствовать.", groups = {Create.class})
    private int id;
    @NotBlank(message = "Название вещи не может быть пустым или содержать пробелы.", groups = {Create.class})
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым или содержать пробелы.", groups = {Create.class})
    private String description;
    @NotNull(message = "Статус не может отсутствовать.", groups = {Create.class})
    private Boolean available;
    @NotNull(message = "Уникальный идентификатор запроса не может отсутствовать.", groups = {Create.class})
    private int requestId;
}
