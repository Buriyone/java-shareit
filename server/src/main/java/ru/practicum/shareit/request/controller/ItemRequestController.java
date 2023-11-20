package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import java.util.List;

import static ru.practicum.shareit.item.controller.ItemController.USER_ID;

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
     * Обрабатывает запрос на создание {@link ItemRequest}.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto add(@RequestBody ItemRequestDto itemRequestDto,
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
    public List<ItemRequestDto> getAll(@RequestParam int from,
                                       @RequestParam int size,
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
