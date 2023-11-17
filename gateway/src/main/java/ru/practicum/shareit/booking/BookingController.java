package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.validation.Create;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    /**
     * Предоставляет доступ к сервису бронирований.
     */
    private final BookingClient bookingClient;
    /**
     * Константа заголовка.
     */
    private static final String USER_ID = "X-Sharer-User-Id";

    /**
     * Обрабатывает запрос на регистрацию бронирования.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@Validated(Create.class)
                                      @RequestBody BookingDto bookingDto,
                                      @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на регистрацию бронирования.");
        return bookingClient.add(bookingDto, userId);
    }

    /**
     * Обрабатывает запросы на подтверждение или отклонение бронирования.
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> statusAppropriator(@PathVariable int bookingId,
                                                     @RequestParam boolean approved,
                                                     @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на подтверждение или отклонение бронирования бронирования.");
        return bookingClient.statusAppropriator(bookingId, approved, userId);
    }

    /**
     * Обрабатывает запросы на предоставление данных конкретного бронирования.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@PathVariable int bookingId,
                                      @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на предоставление данных конкретного бронирования.");
        return bookingClient.get(bookingId, userId);
    }

    /**
     * Обрабатывает запрос на предоставление списка бронирований.
     */
    @GetMapping
    private ResponseEntity<Object> getAll(@RequestParam(defaultValue = "ALL") String state,
                                          @RequestHeader(USER_ID) long userId,
                                          @RequestParam (defaultValue = "0") int from,
                                          @RequestParam (defaultValue = "20") int size) {
        log.info("Поступил запрос на предоставление списка бронирований.");
        return bookingClient.getAll(state, userId, from, size);
    }

    /**
     * Обрабатывает запрос на предоставления списка бронирований для владельца.
     */
    @GetMapping("/owner")
    private ResponseEntity<Object> getByUser(@RequestParam(defaultValue = "ALL") String state,
                                             @RequestHeader(USER_ID) int userId,
                                             @RequestParam (defaultValue = "0") int from,
                                             @RequestParam (defaultValue = "20") int size) {
        log.info("Поступил запрос на предоставление списка бронирований для владельца.");
        return bookingClient.getByUser(state, userId, from, size);
    }
}
