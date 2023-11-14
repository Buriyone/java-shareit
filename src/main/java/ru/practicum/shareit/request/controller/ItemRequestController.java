package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.Create;
import java.util.List;

/**
 * Контроллер для работы с {@link ItemRequest}
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    /**
     * Предоставляет доступ к сервису работы с запросами.
     */
    private final ItemRequestService itemRequestService;
    /**
     * Константа заголовка.
     */
    private static final String USER_ID = "X-Sharer-User-Id";

    /**
     * Обрабатывает запрос на создание {@link ItemRequest}.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto add(@Validated(Create.class)
                              @RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader(USER_ID) int userId) {
        return itemRequestService.add(itemRequestDto, userId);
    }

    /**
     * Обрабатывает запрос на предоставление пользователю списка его {@link ItemRequestDto}.
     */
    @GetMapping
    public List<ItemRequestDto> get(@RequestHeader(USER_ID) int userId) {
        return itemRequestService.get(userId);
    }

    /**
     * Обрабатывает запрос на получение списка {@link ItemRequestDto}, созданных другими пользователями.
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestParam (defaultValue = "0") int from,
                                       @RequestParam (defaultValue = "20") int size,
                                       @RequestHeader(USER_ID) int userId) {
        return itemRequestService.getAll(from, size, userId);
    }

    /**
     * Обрабатывает запрос на предоставление {@link ItemRequestDto} по уникальному идентификатору.
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable int requestId,
                                  @RequestHeader(USER_ID) int userId) {
        return itemRequestService.getById(requestId, userId);
    }
}
