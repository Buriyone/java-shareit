package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Create;

/**
 * Клиентский контроллер для работы с пользовательскими запросами.
 */
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestClient requestClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    /**
     * Обрабатывает запрос на регистрацию {@link ItemRequestDto}.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@Validated(Create.class)
                                      @RequestBody ItemRequestDto itemRequestDto,
                                      @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на регистрацию запроса.");
        return requestClient.add(itemRequestDto, userId);
    }

    /**
     * Обрабатывает запрос на предоставление пользователю списка его {@link ItemRequestDto}.
     */
    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на предоставление пользователю списка его запросов.");
        return requestClient.get(userId);
    }

    /**
     * Обрабатывает запрос на получение списка {@link ItemRequestDto}, созданных другими пользователями.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam (defaultValue = "0") int from,
                                         @RequestParam (defaultValue = "20") int size,
                                         @RequestHeader(USER_ID) long userId) {
        log.info("Поступил запрос на предоставление списка запросов созданных другими пользователями.");
        return requestClient.getAll(from, size, userId);
    }

    /**
     * Обрабатывает запрос на предоставление {@link ItemRequestDto} по уникальному идентификатору.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable int requestId,
                                          @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на предоставление запроса по уникальному идентификатору.");
        return requestClient.getById(requestId, userId);
    }
}
