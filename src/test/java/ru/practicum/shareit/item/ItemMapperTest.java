package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapperTest {
    private final LocalDateTime time = LocalDateTime.now();
    private final User user1 = User.builder()
            .id(1)
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final User user2 = User.builder()
            .id(2)
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1)
            .description("requestDescription")
            .requestor(user2)
            .created(time)
            .build();
    private final Item item = Item.builder()
            .id(1)
            .name("name")
            .description("description")
            .available(true)
            .owner(user1)
            .request(itemRequest)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1)
            .name("name")
            .description("description")
            .available(true)
            .owner(user1)
            .request(itemRequest)
            .build();
    private final ItemMapper itemMapper;

    @Test
    public void toItemTest() {
        Item testItem = itemMapper.toItem(itemDto);
        assertEquals(itemDto.getId(), testItem.getId(),
                "Id отличается.");
        assertEquals(itemDto.getName(), testItem.getName(),
                "Имя отличается.");
        assertEquals(itemDto.getDescription(), testItem.getDescription(),
                "Описание отличается.");
        assertEquals(itemDto.getAvailable(), testItem.getAvailable(),
                "Статус отличается.");
        assertEquals(itemDto.getOwner(), testItem.getOwner(),
                "Владелец отличается.");
        assertEquals(itemDto.getRequest(), testItem.getRequest(),
                "Запрос отличается.");
    }

    @Test
    public void toItemDtoIncreasedConfidentialTest() {
        ItemDtoIncreasedConfidential testItem = itemMapper.toItemDtoIncreasedConfidential(item);
        assertEquals(item.getId(), testItem.getId(),
                "Id отличается.");
        assertEquals(item.getName(), testItem.getName(),
                "Имя отличается.");
        assertEquals(item.getDescription(), testItem.getDescription(),
                "Описание отличается.");
        assertEquals(item.getAvailable(), testItem.getAvailable(),
                "Статус отличается.");
        assertEquals(item.getOwner(), testItem.getOwner(),
                "Владелец отличается.");
        assertEquals(item.getRequest().getId(), testItem.getRequestId(),
                "Id запроса отличается.");
    }

    @Test
    public void toItemDtoForRequestTest() {
        ItemDtoForRequest testItem = itemMapper.toItemDtoForRequest(item);
        assertEquals(item.getId(), testItem.getId(),
                "Id отличается.");
        assertEquals(item.getName(), testItem.getName(),
                "Имя отличается.");
        assertEquals(item.getDescription(), testItem.getDescription(),
                "Описание отличается.");
        assertEquals(item.getAvailable(), testItem.getAvailable(),
                "Статус отличается.");
        assertEquals(item.getRequest().getId(), testItem.getRequestId(),
                "Id запроса отличается.");
    }
}
