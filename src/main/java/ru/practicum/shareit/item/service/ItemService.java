package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс сервиса контроллера обработки запросов связанных с {@link Item} и {@link ItemDto}.
 */
public interface ItemService {
    ItemDto add(ItemDto itemDto, int userId);

    ItemDto update(ItemDto itemDto, int itemId, int userId);

    ItemDto getById(int itemId, int userId);

    List<ItemDto> search(String text, int userId);

    List<ItemDto> getAll(int userId);

    boolean itemChecker(int itemId);

    CommentDtoIncreasedConfidential addComment(int itemId, int userId, CommentDto commentDto);
}
