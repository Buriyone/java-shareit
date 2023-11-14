package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ItemRequestServiceTest {
    private User user = User.builder().name("user").email("email@yandex.ru").build();
    private Item item = Item.builder().name("item").description("itemDescription").available(true).build();
    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("Description").build();
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @BeforeEach
    public void start() {
        user = userRepository.save(user);
        item.setOwner(user);
        item = itemRepository.save(item);
    }

    @Test
    public void addValidationTest() {
        try {
            itemRequestService.add(itemRequestDto, 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 999 не найден.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void addTest() {
        ItemRequestDto itemRequestDtoTest = itemRequestService.add(itemRequestDto, user.getId());
        assertTrue(itemRequestDtoTest.getId() != 0, "Запрос не был зарегистрирован.");
        assertEquals(itemRequestDtoTest.getDescription(), itemRequestDto.getDescription(),
                "Описание отличается.");
        assertNotNull(itemRequestDtoTest.getCreated(), "Время не было установлено.");
        assertTrue(itemRequestDtoTest.getCreated().isAfter(LocalDateTime.now().minusSeconds(30))
                && itemRequestDtoTest.getCreated().isBefore(LocalDateTime.now().plusSeconds(30)),
                "Время установлено не корректно.");
        assertNotNull(itemRequestDtoTest.getRequestor(), "Пользователь не был назначен.");
        assertEquals(itemRequestDto.getRequestor(), user, "Некорректно назначен пользователь.");
    }

    @Test
    public void getValidationTest() {
        try {
            itemRequestService.get(999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 999 не найден.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getTest() {
        List<ItemRequestDto> itemRequestDtoTestList = itemRequestService.get(user.getId());
        assertTrue(itemRequestDtoTestList.isEmpty(), "Лист не пуст.");
        ItemRequestDto itemRequestDtoTest = itemRequestService.add(itemRequestDto, user.getId());
        itemRequestDtoTestList = itemRequestService.get(user.getId());
        assertFalse(itemRequestDtoTestList.isEmpty(), "Лист пуст.");
        itemRequestDtoTest.setItems(new ArrayList<>());
        assertEquals(itemRequestDtoTestList.get(0), itemRequestDtoTest, "Запросы отличаются.");
        assertTrue(itemRequestDtoTestList.get(0).getItems().isEmpty(), "Список с ответами не пуст.");
    }

    @Test
    public void getWithItemsTest() {
        ItemRequestDto itemRequestDtoTest = itemRequestService.add(itemRequestDto, user.getId());
        User testUser = userRepository.save(User.builder().name("testUser").email("testEmail@yandex.ru").build());
        ItemDto itemDto = ItemDto.builder().name("item").description("itemDescription")
                .available(true).requestId(itemRequestDtoTest.getId()).build();
        List<ItemRequestDto> itemRequestDtoTestList = itemRequestService.get(user.getId());
        assertTrue(itemRequestDtoTestList.get(0).getItems().isEmpty(), "Список ответов не пуст.");
        itemService.add(itemDto, testUser.getId());
        itemRequestDtoTestList = itemRequestService.get(user.getId());
        assertFalse(itemRequestDtoTestList.get(0).getItems().isEmpty(), "Список ответов пуст.");
    }

    @Test
    public void getAllValidationTest() {
        try {
            itemRequestService.getAll(0, 1, 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 999 не найден.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemRequestService.getAll(-1, 0, user.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение from не может быть отрицательным.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemRequestService.getAll(0, 0, user.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение size не может быть меньше 1.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getAllTest() {
        itemRequestService.add(itemRequestDto, user.getId());
        List<ItemRequestDto> testList = itemRequestService.getAll(0, 20, user.getId());
        assertTrue(testList.isEmpty(), "В списке присутствует запрос пользователя");
        User otherUser = User.builder().name("otherUser").email("otheruser@yandex.ru").build();
        otherUser = userRepository.save(otherUser);
        ItemRequestDto itemRequestByOtherUser = itemRequestService.add(itemRequestDto, otherUser.getId());
        testList = itemRequestService.getAll(0, 20, user.getId());
        assertFalse(testList.isEmpty(), "Список запросов пуст.");
        itemRequestByOtherUser.setItems(new ArrayList<>());
        assertEquals(testList.get(0), itemRequestByOtherUser, "Запросы отличаются.");
        assertTrue(testList.get(0).getItems().isEmpty(), "Список с ответами не пуст.");
    }

    @Test
    public void getAllWithItemsTest() {
        User otherUser = userRepository.save(User.builder().name("otherUser").email("otheruser@yandex.ru").build());
        ItemRequestDto itemRequestByOtherUser = itemRequestService.add(itemRequestDto, otherUser.getId());
        User otherUser2 = userRepository.save(User.builder().name("other2User").email("otheruser2@yandex.ru").build());
        ItemDto itemDto = ItemDto.builder().name("item").description("itemDescription")
                .available(true).requestId(itemRequestByOtherUser.getId()).build();
        List<ItemRequestDto> testList = itemRequestService.getAll(0, 20, user.getId());
        assertTrue(testList.get(0).getItems().isEmpty(), "Список запросов не пуст.");
        itemService.add(itemDto, otherUser2.getId());
        testList = itemRequestService.getAll(0, 20, user.getId());
        assertFalse(testList.get(0).getItems().isEmpty(), "Список запросов пуст.");
    }

    @Test
    public void getByIdValidationTest() {
        ItemRequestDto itemRequestByOtherUser = itemRequestService.add(itemRequestDto, user.getId());
        try {
            itemRequestService.getById(itemRequestByOtherUser.getId(), 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 999 не найден.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemRequestService.getById(999, user.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Запрос с id: 999 не найден.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getByIdTest() {
        ItemRequestDto itemRequestDtoTest = itemRequestService.add(itemRequestDto, user.getId());
        ItemRequestDto otherItemRequestDtoTest = itemRequestService.getById(itemRequestDtoTest.getId(), user.getId());
        assertNotNull(otherItemRequestDtoTest, "Запрос не был предоставлен.");
        assertEquals(itemRequestDtoTest.getId(), otherItemRequestDtoTest.getId(), "Id отличаются.");
        assertEquals(itemRequestDtoTest.getDescription(), otherItemRequestDtoTest.getDescription(),
                "Описания отличаются.");
        assertEquals(itemRequestDtoTest.getCreated(), otherItemRequestDtoTest.getCreated(),
                "Время создания отличаются.");
        assertEquals(itemRequestDtoTest.getRequestor(), otherItemRequestDtoTest.getRequestor(),
                "Создатель отличаются.");
    }

    @Test
    public void getByIdWithItems() {
        ItemRequestDto itemRequestDtoTest = itemRequestService.add(itemRequestDto, user.getId());
        itemRequestDtoTest.setItems(new ArrayList<>());
        User otherUser = userRepository.save(User.builder().name("otherUser").email("otheruser@yandex.ru").build());
        ItemDto itemDto = ItemDto.builder().name("item").description("itemDescription")
                .available(true).requestId(itemRequestDtoTest.getId()).build();
        ItemRequestDto otherItemRequestDtoTest = itemRequestService.getById(itemRequestDtoTest.getId(), user.getId());
        assertTrue(otherItemRequestDtoTest.getItems().isEmpty(), "Список не пуст.");
        itemService.add(itemDto, otherUser.getId());
        otherItemRequestDtoTest = itemRequestService.getById(itemRequestDtoTest.getId(), user.getId());
        assertFalse(otherItemRequestDtoTest.getItems().isEmpty(), "Список пуст.");
    }

    @Test
    public void itemRequestCheckerTest() {
        ItemRequestDto itemRequestDtoTest = itemRequestService.add(itemRequestDto, user.getId());
        assertFalse(itemRequestService.itemRequestChecker(999),
                "Несуществующий запрос обнаружен.");
        assertTrue(itemRequestService.itemRequestChecker(itemRequestDtoTest.getId()),
                "Запрос не обнаружен.");
    }
}
