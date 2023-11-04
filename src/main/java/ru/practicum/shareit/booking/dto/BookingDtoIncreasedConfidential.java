package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.assistant.Status;

import java.time.LocalDateTime;
import ru.practicum.shareit.booking.model.Booking;

/**
 * DTO-класс для {@link Booking} конфиденциальный.
 */
@Data
@Builder(toBuilder = true)
public class BookingDtoIncreasedConfidential {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private int bookerId;
    private Status status;
}
