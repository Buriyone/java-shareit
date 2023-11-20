package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * DTO-класс для {@link Booking}.
 */
@Data
@Builder(toBuilder = true)
public class BookingDto {
    private int id;
    @NotNull(message = "Время начала бронирования не может отсутствовать.")
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом.")
    private LocalDateTime start;
    @NotNull(message = "Время окончания бронирования не может отсутствовать.")
    @Future(message = "Время окончания бронирования не может быть в прошлом.")
    private LocalDateTime end;
    @NotNull(message = "Уникальный идентификатор не может отсутствовать.")
    @Positive(message = "Уникальный идентификатор не может быть отрицательным.")
    private int itemId;
    private Item item;
    private int bookerId;
    private User booker;
    private Status status;
}
