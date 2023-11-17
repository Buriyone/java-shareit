package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ItemServiceTest {
    private final UserRepository userRepository;
    private final ItemService itemService;
    public final ItemRequestService itemRequestService;
    private User user = User.builder().name("name").email("email@yandex.ru").build();
    private User user2 = User.builder().name("name2").email("email2@yandex.ru").build();
    private ItemDto itemDto = ItemDto.builder().name("name").description("description").available(true).build();
    private ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("description").build();
    private final BookingService bookingService;

    @BeforeEach
    public void start() {
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        itemRequestDto = itemRequestService.add(itemRequestDto, user2.getId());
    }

    @Test
    public void addValidationTest() {
        try {
            itemService.add(itemDto, 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemDto.setRequestId(999);
            itemService.add(itemDto, user.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Запрос с id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void addTest() {
        assertNull(itemDto.getOwner(), "Обнаружен неизвестный владелец.");
        assertEquals(0, itemDto.getId(), "id присутствует.");
        ItemDtoIncreasedConfidential testItem1 = itemService.add(itemDto, user.getId());
        assertNotNull(testItem1.getOwner(), "Пользователь не был назначен.");
        assertEquals(user, testItem1.getOwner(), "Владельцы отличаются.");
        assertTrue(testItem1.getId() > 0, "Вещь не была зарегистрирована.");
        itemDto.setRequestId(itemRequestDto.getId());
        itemService.add(itemDto, user.getId());
        ItemDtoIncreasedConfidential testItem2 = itemService.add(itemDto, user.getId());
        assertEquals(itemRequestDto.getId(), testItem2.getRequestId(), "Id запроса отличается.");
    }

    @Test
    public void updateValidationTest() {
        ItemDtoIncreasedConfidential testItem = itemService.add(itemDto, user.getId());
        try {
            itemDto = ItemDto.builder().name(" ").description("description").available(true).build();
            itemService.update(itemDto, testItem.getId(), user.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Некорректно указаны данные.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemDto = ItemDto.builder().name("name").description(" ").available(true).build();
            itemService.update(itemDto, testItem.getId(), user.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Некорректно указаны данные.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemDto = ItemDto.builder().name("name").description("description").available(true).build();
            itemService.update(itemDto, testItem.getId(), 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemDto = ItemDto.builder().name("name").description("description").available(true).build();
            itemService.update(itemDto, 999, user.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Вещь c id 999 не обнаружена.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemDto = ItemDto.builder().name("name").description("description").available(true).build();
            itemService.update(itemDto, 0, user.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Вещь не зарегистрирована.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemDto = ItemDto.builder().name("name").description("description").available(true).build();
            itemService.update(itemDto, testItem.getId(), user2.getId());
        } catch (Exception e) {
            assertEquals(ForbiddenException.class, e.getClass(), "Тип ошибки отличается.");
            String message = String.format("Вещь c id: %d не принадлежит пользователю с id: %d.",
                    testItem.getId(), user2.getId());
            assertEquals(message, e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void updateTest() {
        ItemDtoIncreasedConfidential registrationItem = itemService.add(itemDto, user.getId());
        itemDto = ItemDto.builder().name("newName").description("newDescription").available(false).build();
        ItemDtoIncreasedConfidential testItem = itemService.update(itemDto, registrationItem.getId(), user.getId());
        assertEquals("newName", testItem.getName(), "Имя отличается.");
        assertEquals("newDescription", testItem.getDescription(), "Описание отличается.");
        assertEquals(false, testItem.getAvailable(), "Статус отличается.");
    }

    @Test
    public void getByIdValidationTest() {
        ItemDtoIncreasedConfidential testItem = itemService.add(itemDto, user.getId());
        try {
            itemService.getById(testItem.getId(), 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.getById(999, user.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Вещь c id 999 не обнаружена.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getByIdTest() {
        ItemDtoIncreasedConfidential registrationItem = itemService.add(itemDto, user.getId());
        BookingDto registrationBookingDto = BookingDto.builder().itemId(registrationItem.getId())
                .start(LocalDateTime.now().minusDays(1)).end(LocalDateTime.now().minusHours(23)).build();
        BookingDto testLastBooking = bookingService.add(registrationBookingDto, user2.getId());
        BookingDto registrationBookingDto2 = BookingDto.builder().itemId(registrationItem.getId())
                .start(LocalDateTime.now().plusHours(23)).end(LocalDateTime.now().plusDays(1)).build();
        BookingDto testNextBooking = bookingService.add(registrationBookingDto2, user2.getId());
        bookingService.statusAppropriator(testLastBooking.getId(), true, user.getId());
        bookingService.statusAppropriator(testNextBooking.getId(), true, user.getId());
        CommentDto registrationComment = CommentDto.builder().text("text").build();
        CommentDtoIncreasedConfidential testComment = itemService.addComment(registrationItem.getId(), user2.getId(),
                registrationComment);
        ItemDtoIncreasedConfidential testItem1 = itemService.getById(registrationItem.getId(), user.getId());
        assertEquals(registrationItem.getId(), testItem1.getId(),
                "Id отличается.");
        assertEquals(registrationItem.getName(), testItem1.getName(),
                "Имя отличается.");
        assertEquals(registrationItem.getDescription(), testItem1.getDescription(),
                "Описание отличается.");
        assertEquals(registrationItem.getAvailable(), testItem1.getAvailable(),
                "Статус отличается.");
        assertEquals(registrationItem.getOwner(), testItem1.getOwner(),
                "Владелец отличается.");
        assertEquals(testComment.getText(), testItem1.getComments().get(0).getText(),
                "Комментарии отличается.");
        assertEquals(testLastBooking.getId(), testItem1.getLastBooking().getId(),
                "Последнее бронирование отличается.");
        assertEquals(testNextBooking.getId(), testItem1.getNextBooking().getId(),
                "Следующее бронирование отличается.");
        ItemDtoIncreasedConfidential testItem2 = itemService.getById(registrationItem.getId(), user2.getId());
        assertNull(testItem2.getLastBooking(),
                "Постороннему пользователю доступен просмотр последнего бронирования.");
        assertNull(testItem2.getNextBooking(),
                "Постороннему пользователю доступен просмотр следующего бронирования.");
    }

    @Test
    public void searchValidationTest() {
        itemService.add(itemDto, user.getId());
        try {
            itemService.search("name", user2.getId(), -1, 20);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение from не может быть отрицательным.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.search("name", user2.getId(), 0, 0);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение size не может быть меньше 1.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.search("name", 999, 0, 20);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void searchTest() {
        ItemDtoIncreasedConfidential testItem = itemService.add(itemDto, user.getId());
        List<ItemDtoIncreasedConfidential> testList = itemService.search("DeSCriPtiOn",
                user2.getId(), 0, 20);
        assertEquals(testItem, testList.get(0), "Вещь не была найдена.");
    }

    @Test
    public void getAllValidationTest() {
        itemService.add(itemDto, user.getId());
        try {
            itemService.getAll(999, 0, 20);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.getAll(user.getId(), -1, 20);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение from не может быть отрицательным.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.getAll(user.getId(), 0, 0);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение size не может быть меньше 1.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getAllTest() {
        ItemDtoIncreasedConfidential registrationItem = itemService.add(itemDto, user.getId());
        BookingDto registrationBookingDto = BookingDto.builder().itemId(registrationItem.getId())
                .start(LocalDateTime.now().minusDays(1)).end(LocalDateTime.now().minusHours(23)).build();
        BookingDto testLastBooking = bookingService.add(registrationBookingDto, user2.getId());
        BookingDto registrationBookingDto2 = BookingDto.builder().itemId(registrationItem.getId())
                .start(LocalDateTime.now().plusHours(23)).end(LocalDateTime.now().plusDays(1)).build();
        BookingDto testNextBooking = bookingService.add(registrationBookingDto2, user2.getId());
        bookingService.statusAppropriator(testLastBooking.getId(), true, user.getId());
        bookingService.statusAppropriator(testNextBooking.getId(), true, user.getId());
        CommentDto commentDto = CommentDto.builder().text("text").build();
        CommentDtoIncreasedConfidential testComment = itemService.addComment(registrationItem.getId(), user2.getId(),
                commentDto);
        itemService.add(itemDto, user2.getId());
        itemService.add(itemDto, user2.getId());
        itemService.add(itemDto, user2.getId());
        List<ItemDtoIncreasedConfidential> testList = itemService.getAll(user.getId(), 0, 20);
        assertEquals(1, testList.size(),
                "В список попали чужие вещи.");
        assertEquals(registrationItem.getId(), testList.get(0).getId(),
                "Id отличается.");
        assertEquals(registrationItem.getName(), testList.get(0).getName(),
                "Имя отличается.");
        assertEquals(registrationItem.getDescription(), testList.get(0).getDescription(),
                "Описание отличается.");
        assertEquals(registrationItem.getAvailable(), testList.get(0).getAvailable(),
                "Статус отличается.");
        assertEquals(registrationItem.getOwner(), testList.get(0).getOwner(),
                "Владелец отличается.");
        assertEquals(testComment.getText(), testList.get(0).getComments().get(0).getText(),
                "Комментарии отличается.");
        assertEquals(testLastBooking.getId(), testList.get(0).getLastBooking().getId(),
                "Последнее бронирование отличается.");
        assertEquals(testNextBooking.getId(), testList.get(0).getNextBooking().getId(),
                "Следующее бронирование отличается.");
    }

    @Test
    public void itemCheckerTest() {
        ItemDtoIncreasedConfidential registrationItem = itemService.add(itemDto, user.getId());
        itemService.itemChecker(registrationItem.getId());
        assertTrue(itemService.itemChecker(registrationItem.getId()), "вещь не обнаружена.");
        assertFalse(itemService.itemChecker(999), "Обнаружена неизвестная вещь");
    }

    @Test
    public void addCommentValidationTest() {
        User otherUser = User.builder().name("otherUser").email("othermail@yandex.ru").build();
        otherUser = userRepository.save(otherUser);
        ItemDtoIncreasedConfidential registrationItem = itemService.add(itemDto, user.getId());
        BookingDto registrationBookingDto = BookingDto.builder().itemId(registrationItem.getId())
                .start(LocalDateTime.now().minusDays(1)).end(LocalDateTime.now().minusHours(23)).build();
        BookingDto testLastBooking = bookingService.add(registrationBookingDto, user2.getId());
        bookingService.statusAppropriator(testLastBooking.getId(), true, user.getId());
        CommentDto commentDto = CommentDto.builder().text("text").build();
        try {
            itemService.addComment(999, user2.getId(), commentDto);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Вещь c id: 999 не обнаружена.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.addComment(registrationItem.getId(), 999, commentDto);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            itemService.addComment(registrationItem.getId(), otherUser.getId(), commentDto);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            String message = String.format("Пользователь с id: %d не брал в аренду вещь с id: %d",
                    otherUser.getId(), registrationItem.getId());
            assertEquals(message, e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void addCommentTest() {
        ItemDtoIncreasedConfidential registrationItem = itemService.add(itemDto, user.getId());
        BookingDto registrationBookingDto = BookingDto.builder().itemId(registrationItem.getId())
                .start(LocalDateTime.now().minusDays(1)).end(LocalDateTime.now().minusHours(23)).build();
        BookingDto testLastBooking = bookingService.add(registrationBookingDto, user2.getId());
        bookingService.statusAppropriator(testLastBooking.getId(), true, user.getId());
        CommentDto commentDto = CommentDto.builder().text("text").build();
        CommentDtoIncreasedConfidential testComment = itemService.addComment(registrationItem.getId(),
                user2.getId(), commentDto);
        assertTrue(testComment.getId() != 0,
                "Комментарий не был зарегистрирован.");
        assertEquals("text", testComment.getText(),
                "Текст комментария отличается.");
        assertEquals(user2.getName(), testComment.getAuthorName(),
                "Имя автора отличается.");
        assertNotNull(testComment.getCreated(),
                "Время создания комментария не было установлено.");
        assertTrue(testComment.getCreated().isAfter(LocalDateTime.now().minusSeconds(10))
                && testComment.getCreated().isBefore(LocalDateTime.now().plusSeconds(10)),
                "Установлено неверное время.");
    }
}