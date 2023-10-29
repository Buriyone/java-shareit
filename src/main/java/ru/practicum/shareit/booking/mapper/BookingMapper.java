package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIncreasedConfidential;
import ru.practicum.shareit.booking.model.Booking;

/**
 * Маппер для работы с {@link Booking} и {@link BookingDto}.
 */
public class BookingMapper {
    /**
     * Конвертирует {@link Booking} в {@link BookingDto}.
     */
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .build();
    }

    /**
     * Конвертирует {@link BookingDto} в {@link Booking}.
     */
    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .item(bookingDto.getItem())
                .booker(bookingDto.getBooker())
                .build();
    }

    /**
     * Конвертирует {@link BookingDto} в {@link BookingDtoIncreasedConfidential}.
     */
    public static BookingDtoIncreasedConfidential toBookingDtoIncreasedConfidential(Booking booking) {
        return BookingDtoIncreasedConfidential.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }
}
