package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;

import java.util.List;

/**
 * Интерфейс сервиса контроллера обработки запросов связанных с {@link Item} и {@link ItemDto}.
 */
public interface ItemService {
    ItemDtoIncreasedConfidential add(ItemDto itemDto, int userId);

    ItemDtoIncreasedConfidential update(ItemDto itemDto, int itemId, int userId);

    ItemDtoIncreasedConfidential getById(int itemId, int userId);

    List<ItemDtoIncreasedConfidential> search(String text, int userId, int from, int size);

    List<ItemDtoIncreasedConfidential> getAll(int userId, int from, int size);

    boolean itemChecker(int itemId);

    CommentDtoIncreasedConfidential addComment(int itemId, int userId, CommentDto commentDto);
}
