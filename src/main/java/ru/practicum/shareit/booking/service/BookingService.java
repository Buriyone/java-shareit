package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import ru.practicum.shareit.booking.model.Booking;

/**
 * Интерфейс сервиса для работы с сущностью {@link Booking}.
 */
public interface BookingService {
    BookingDto add(BookingDto bookingDto, int userId);

    BookingDto statusAppropriator(int bookingId, boolean approve, int userId);

    BookingDto get(int bookingId, int userId);

    List<BookingDto> getAll(String param, int userId,int from, int size);

    List<BookingDto> getByUser(String param, int userId, int from, int size);
}
