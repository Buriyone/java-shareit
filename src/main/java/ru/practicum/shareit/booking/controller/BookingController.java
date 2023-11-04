package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.assistant.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.Create;

import java.util.List;

/**
 * Контроллер для работы с бронированиями.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    /**
     * Предоставляет доступ к сервису бронирований.
     */
    private final BookingService bookingService;
    /**
     * Константа заголовка.
     */
    private static final String USER_ID = "X-Sharer-User-Id";

    /**
     * Обрабатывает запрос на регистрацию бронирования.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto add(@Validated(Create.class)
                          @RequestBody BookingDto bookingDto,
                          @RequestHeader(USER_ID) int userId) {
        return bookingService.add(bookingDto, userId);
    }

    /**
     * Обрабатывает запросы на подтверждение или отклонение бронирования.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto approved(@PathVariable int bookingId,
                               @RequestParam boolean approved,
                               @RequestHeader(USER_ID) int userId) {
        return bookingService.statusAppropriator(bookingId, approved, userId);
    }

    /**
     * Обрабатывает запросы на предоставление данных конкретного бронирования.
     */
    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable int bookingId,
                          @RequestHeader(USER_ID) int userId) {
        return bookingService.get(bookingId, userId);
    }

    /**
     * Обрабатывает запрос на предоставление списка в рамках: {@link State}.
     */
    @GetMapping
    private List<BookingDto> getAll(@RequestParam(defaultValue = "ALL") String state,
                                    @RequestHeader(USER_ID) int userId) {
        return bookingService.getAll(state, userId);
    }

    /**
     * Обрабатывает запрос на предоставления списка бронирований для владельца в рамках {@link State}.
     */
    @GetMapping("/owner")
    private List<BookingDto> getByUser(@RequestParam(defaultValue = "ALL") String state,
                                       @RequestHeader(USER_ID) int userId) {
        return bookingService.getByUser(state, userId);
    }
}
