package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    public final ItemService itemService;

    /**
     * Обрабатывает запрос на регистрацию и добавление.
     */
    @PostMapping
    public ItemDto add(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.add(itemDto, userId);
    }

    /**
     * Обрабатывает запрос на обновление данных.
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable int itemId,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    /**
     * Обрабатывает запрос на предоставление вещи по уникальному идентификатору.
     */
    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable int itemId,
                       @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getById(itemId, userId);
    }

    /**
     * Обрабатывает запросы на предоставление списка вещей по поисковому запросу.
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.search(text, userId);
    }

    /**
     * Обрабатывает запросы на предоставления списка вещей пользователя.
     */
    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAll(userId);
    }
}
