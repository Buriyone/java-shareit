package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    @NotBlank(message = "Название вещи не может быть пустым или содержать пробелы.", groups = {Create.class})
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым или содержать пробелы.", groups = {Create.class})
    private String description;
    @NotNull(message = "Статус не может отсутствовать.", groups = {Create.class})
    private Boolean available;
    private int requestId;
}
