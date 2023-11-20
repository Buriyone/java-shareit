package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.dto.BookingDtoIncreasedConfidential;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO-класс для {@link Item} повышенной конфиденциальности.
 */
@Data
@Builder(toBuilder = true)
public class ItemDtoIncreasedConfidential {
    private int id;
    @NotBlank(message = "Название вещи не может быть пустым или содержать пробелы.")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым или содержать пробелы.")
    private String description;
    @NotNull(message = "Статус не может отсутствовать.")
    private Boolean available;
    private User owner;
    private BookingDtoIncreasedConfidential lastBooking;
    private BookingDtoIncreasedConfidential nextBooking;
    private List<CommentDtoIncreasedConfidential> comments;
    private int requestId;
}
