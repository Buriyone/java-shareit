package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.List;

/**
 * Контроллер для работы с вещами.
 */
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    /**
     * Предоставляет доступ к сервису работы с вещами.
     */
    private final ItemService itemService;
    /**
     * Константа заголовка.
     */
    private static final String USER_ID = "X-Sharer-User-Id";

    /**
     * Обрабатывает запрос на регистрацию и добавление.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto add(@Validated(Create.class)
                       @RequestBody ItemDto itemDto,
                       @RequestHeader(USER_ID) int userId) {
        return itemService.add(itemDto, userId);
    }

    /**
     * Обрабатывает запрос на обновление данных.
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@Validated(Update.class)
                          @RequestBody ItemDto itemDto,
                          @PathVariable int itemId,
                          @RequestHeader(USER_ID) int userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    /**
     * Обрабатывает запрос на предоставление вещи по уникальному идентификатору.
     */
    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable int itemId,
                       @RequestHeader(USER_ID) int userId) {
        return itemService.getById(itemId, userId);
    }

    /**
     * Обрабатывает запросы на предоставление списка вещей по поисковому запросу.
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestHeader(USER_ID) int userId) {
        return itemService.search(text, userId);
    }

    /**
     * Обрабатывает запросы на предоставления списка вещей пользователя.
     */
    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(USER_ID) int userId) {
        return itemService.getAll(userId);
    }

    /**
     * Обрабатывает запросы на создание комментария.
     */
    @PostMapping("/{itemId}/comment")
    public CommentDtoIncreasedConfidential addComment(@Validated(Create.class)
                                                      @RequestBody CommentDto commentDto,
                                                      @PathVariable int itemId,
                                                      @RequestHeader(USER_ID) int userId) {
        return itemService.addComment(itemId, userId, commentDto);
    }
}
