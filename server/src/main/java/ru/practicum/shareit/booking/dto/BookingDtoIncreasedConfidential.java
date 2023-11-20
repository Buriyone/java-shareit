package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.assistant.Status;

import java.time.LocalDateTime;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * DTO-класс для {@link Booking} конфиденциальный.
 */
@Data
@Builder(toBuilder = true)
public class BookingDtoIncreasedConfidential {
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
    private int bookerId;
    private Status status;
}
