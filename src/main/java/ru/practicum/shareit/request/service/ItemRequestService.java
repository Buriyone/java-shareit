package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Интерфейс сервиса для обработки запросов связанных с {@link ItemRequest} и {@link ItemRequestDto}.
 */
public interface ItemRequestService {
    ItemRequestDto add(ItemRequestDto itemRequestDto, int userId);

    List<ItemRequestDto> get(int userId);

    List<ItemRequestDto> getAll(int from, int size, int userId);

    ItemRequestDto getById(int requestId, int userId);

    Boolean itemRequestChecker(int requestId);
}
