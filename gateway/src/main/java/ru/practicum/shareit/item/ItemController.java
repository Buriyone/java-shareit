package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

/**
 * Контроллер для работы с вещами.
 */
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    /**
     * Предоставляет доступ к сервису вещей.
     */
    private final ItemClient itemClient;
    /**
     * Константа заголовка.
     */
    public static final String USER_ID = "X-Sharer-User-Id";

    /**
     * Обрабатывает запрос на регистрацию и добавление.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@Validated(Create.class)
                                      @RequestBody ItemDto itemDto,
                                      @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на регистрацию и добавление.");
        return itemClient.add(itemDto, userId);
    }

    /**
     * Обрабатывает запрос на обновление данных.
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Validated(Update.class)
                                         @RequestBody ItemDto itemDto,
                                         @PathVariable int itemId,
                                         @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на обновление данных.");
        return itemClient.update(itemDto, itemId, userId);
    }

    /**
     * Обрабатывает запрос на предоставление вещи по уникальному идентификатору.
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable int itemId,
                                      @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на предоставление вещи по уникальному идентификатору.");
        return itemClient.getById(itemId, userId);
    }

    /**
     * Обрабатывает запросы на предоставление списка вещей по поисковому запросу.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestHeader(USER_ID) long userId,
                                         @RequestParam (defaultValue = "0") int from,
                                         @RequestParam (defaultValue = "20") int size) {
        log.info("Поступил запрос на предоставление списка вещей по поисковому запросу.");
        return itemClient.search(text, userId, from, size);
    }

    /**
     * Обрабатывает запросы на предоставления списка вещей пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID) long userId,
                                         @RequestParam (defaultValue = "0") int from,
                                         @RequestParam (defaultValue = "20") int size) {
        log.info("Поступил запрос на предоставления списка вещей пользователя.");
        return itemClient.getAll(userId, from, size);
    }

    /**
     * Обрабатывает запросы на создание комментария.
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Validated(Create.class)
                                             @RequestBody CommentDto commentDto,
                                             @PathVariable int itemId,
                                             @RequestHeader(USER_ID) int userId) {
        log.info("Поступил запрос на создание комментария.");
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
