package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

/**
 * Маппер для работы с {@link Item}, {@link ItemDto}, {@link ItemDtoIncreasedConfidential} и {@link ItemDtoForRequest}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemDto itemDto);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDtoIncreasedConfidential toItemDtoIncreasedConfidential(Item item);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDtoForRequest toItemDtoForRequest(Item item);
}
