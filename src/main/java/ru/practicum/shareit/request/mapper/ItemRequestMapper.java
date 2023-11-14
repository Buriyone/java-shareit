package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

/**
 * Маппер для работы с {@link ItemRequest} и {@link ItemRequestDto}.
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);
}
