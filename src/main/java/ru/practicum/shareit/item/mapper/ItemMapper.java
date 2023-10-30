package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * Маппер для работы с {@link Item} и {@link ItemDto}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemDto itemDto);
    ItemDto toItemDto(Item item);


    /*
    /**
     * Конвертирует {@link Item} в {@link ItemDto}.
     /*
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }
    */
/*
    /**
     * Конвертирует {@link ItemDto} в {@link Item}.
     */
    /*
    /*
    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .request(itemDto.getRequest())
                .build();
    }

     */
}
