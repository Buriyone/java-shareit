package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс репозитория вещей.
 */

public interface InMemoryItemRepository {
    Item add(Item item);

    Item update(Item item, int itemId);

    void deleteAllItemByUserId(int id);

    Item getById(int id);

    List<Item> getAll(int userId);

    Boolean itemChecker(int id);

    Boolean itemByUserChecker(int itemId, int userId);

    List<Item> search(String text);
}
