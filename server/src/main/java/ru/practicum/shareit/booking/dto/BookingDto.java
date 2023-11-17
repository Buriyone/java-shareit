package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Create;

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
    @NotNull(message = "Время начала бронирования не может отсутствовать.", groups = {Create.class})
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом.", groups = {Create.class})
    private LocalDateTime start;
    @NotNull(message = "Время окончания бронирования не может отсутствовать.", groups = {Create.class})
    @FutureOrPresent(message = "Время окончания бронирования не может быть в прошлом.", groups = {Create.class})
    private LocalDateTime end;
    @NotNull(message = "Уникальный идентификатор не может отсутствовать.", groups = {Create.class})
    @Positive(message = "Уникальный идентификатор не может быть отрицательным.", groups = {Create.class})
    private int itemId;
    private Item item;
    private int bookerId;
    private User booker;
    private Status status;
}
