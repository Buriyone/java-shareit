package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestMapperTest {
    private final LocalDateTime time = LocalDateTime.now();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1)
            .created(time)
            .description("description")
            .requestor(User.builder().id(1).name("name").email("email@yandex.ru").build())
            .build();
    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1)
            .created(time)
            .description("description")
            .requestor(User.builder().id(1).name("name").email("email@yandex.ru").build())
            .build();
    private final ItemRequestMapper itemRequestMapper;

    @Test
    public void toItemRequestTest() {
        ItemRequest testItemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        assertEquals(testItemRequest.getId(), itemRequest.getId(),
                "id отличаются.");
        assertEquals(testItemRequest.getCreated(), itemRequest.getCreated(),
                "Время создания отличается.");
        assertEquals(testItemRequest.getDescription(), itemRequest.getDescription(),
                "Описание отличается.");
        assertEquals(testItemRequest.getRequestor(), itemRequest.getRequestor(),
                "Пользователи отличается.");
    }

    @Test
    public void toItemRequestDtoTest() {
        ItemRequestDto testItemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(testItemRequestDto.getId(), itemRequestDto.getId(),
                "id отличаются.");
        assertEquals(testItemRequestDto.getCreated(), itemRequestDto.getCreated(),
                "Время создания отличается.");
        assertEquals(testItemRequestDto.getDescription(), itemRequestDto.getDescription(),
                "Описание отличается.");
        assertEquals(testItemRequestDto.getRequestor(), itemRequestDto.getRequestor(),
                "Пользователи отличается.");
    }
}
