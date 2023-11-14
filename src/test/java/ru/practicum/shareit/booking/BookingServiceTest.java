package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.StateException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
public class BookingServiceTest {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private User user1 = User.builder()
            .name("name1")
            .email("email1@yandex.ru")
            .build();
    private Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .build();
    private User user2 = User.builder()
            .name("name2")
            .email("email2@yandex.ru")
            .build();
    private final LocalDateTime start = LocalDateTime.now().plusHours(48);
    private final LocalDateTime end = start.plusHours(49);
    private BookingDto bookingDto;

    @BeforeEach
    public void saver() {
        user1 = userRepository.save(user1);
        item.setOwner(user1);
        item = itemRepository.save(item);
        user2 = userRepository.save(user2);
        bookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(item.getId())
                .build();
    }

    @Test
    public void addValidationTest() {
        BookingDto testBooking = bookingDto;
        try {
            testBooking.setEnd(start.minusHours(1));
            bookingService.add(testBooking, user2.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Время окончания бронирования не может быть раньше начала бронирования " +
                            "или совпадать с ним.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            testBooking.setEnd(start);
            bookingService.add(testBooking, user2.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Время окончания бронирования не может быть раньше начала бронирования " +
                            "или совпадать с ним.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            item.setAvailable(false);
            item = itemRepository.save(item);
            testBooking.setItemId(item.getId());
            testBooking.setStart(start);
            testBooking.setEnd(end);
            bookingService.add(testBooking, user2.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Вещь не доступна для бронирования.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            item.setAvailable(true);
            item = itemRepository.save(item);
            testBooking.setItemId(item.getId());
            testBooking.setStart(start);
            testBooking.setEnd(end);
            bookingService.add(testBooking, user1.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Владелец вещи не может оформлять бронирование.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            testBooking.setItemId(999);
            bookingService.add(testBooking, user2.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Вещь c id: 999 не обнаружена.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            testBooking.setItemId(item.getId());
            bookingService.add(testBooking, 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void addTest() {
        BookingDto testBooking = bookingService.add(bookingDto, user2.getId());
        assertTrue(testBooking.getId() != 0,
                "Бронирование не было зарегистрировано.");
        assertNotNull(testBooking.getItem(),
                "Вещь не была присвоена.");
        assertEquals(item, testBooking.getItem(),
                "Вещи отличаются.");
        assertNotNull(testBooking.getBooker(),
                "Автор бронирования не был назначен.");
        assertEquals(user2, testBooking.getBooker(),
                "Автор бронирования отличается.");
        assertNotNull(testBooking.getStatus(),
                "Статус не был установлен.");
        assertEquals(Status.WAITING, testBooking.getStatus(),
                "Статус отличается.");
    }

    @Test
    public void statusAppropriatorValidationTest() {
        BookingDto testBooking = bookingService.add(bookingDto, user2.getId());
        try {
            bookingService.statusAppropriator(999, true, user1.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Бронирование c id: 999 не обнаружено.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.statusAppropriator(testBooking.getId(), true, 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.statusAppropriator(testBooking.getId(), true, user2.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            String message = String.format("Вещь с id: %d не принадлежит пользователю с id: %d.",
                    item.getId(), user2.getId());
            assertEquals(message, e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.statusAppropriator(testBooking.getId(), true, user1.getId());
            bookingService.statusAppropriator(testBooking.getId(), true, user1.getId());
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Бронирование уже было обработано.", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void statusAppropriatorTest() {
        BookingDto registrationBooking1 = bookingService.add(bookingDto, user2.getId());
        BookingDto testBooking = bookingService.statusAppropriator(registrationBooking1.getId(),
                true, user1.getId());
        assertEquals(Status.APPROVED, testBooking.getStatus(),
                "Статус отличается.");
        BookingDto registrationBooking2 = bookingService.add(bookingDto, user2.getId());
        BookingDto testBooking2 = bookingService.statusAppropriator(registrationBooking2.getId(),
                false, user1.getId());
        assertEquals(Status.REJECTED, testBooking2.getStatus(),
                "Статус отличается.");
    }

    @Test
    public void getValidationTest() {
        BookingDto expectedBooking = bookingService.add(bookingDto, user2.getId());
        User imposter = userRepository.save(User.builder()
                .name("imposter")
                .email("imposter@yandex.ru")
                .build());
        try {
            bookingService.get(expectedBooking.getId(), 999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.get(999, user2.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Бронирование с id: 999 не обнаружено.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.get(expectedBooking.getId(), imposter.getId());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            String message = String.format("Пользователь с id: %d не является автором " +
                    "бронирования или владельцем вещи.", imposter.getId());
            assertEquals(message, e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getTest() {
        BookingDto registrationBooking = bookingService.add(bookingDto, user2.getId());
        BookingDto testBookingByBooker = bookingService.get(registrationBooking.getId(), user2.getId());
        assertEquals(registrationBooking.getId(), testBookingByBooker.getId(),
                "Id отличается.");
        assertEquals(start, testBookingByBooker.getStart(),
                "Время старта отличается.");
        assertEquals(end, testBookingByBooker.getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, testBookingByBooker.getStatus(),
                "Статус отличается.");
        assertEquals(user2, testBookingByBooker.getBooker(),
                "Автор бронирования отличается.");
        BookingDto testBookingByItemOwner = bookingService.get(registrationBooking.getId(), user1.getId());
        assertEquals(registrationBooking.getId(), testBookingByItemOwner.getId(),
                "Id отличается.");
        assertEquals(start, testBookingByItemOwner.getStart(),
                "Время старта отличается.");
        assertEquals(end, testBookingByItemOwner.getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, testBookingByItemOwner.getStatus(),
                "Статус отличается.");
        assertEquals(user2, testBookingByItemOwner.getBooker(),
                "Автор бронирования отличается.");
    }

    @Test
    public void getAllValidationTest() {
        bookingService.add(bookingDto, user2.getId());
        try {
            bookingService.getAll("ALL", 999, 0, 20);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.getAll("ALL", user2.getId(), -1, 20);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение from не может быть отрицательным.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.getAll("ALL", user2.getId(), 0, 0);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение size не может быть меньше 1.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.getAll("imposter", user2.getId(), 0, 20);
        } catch (Exception e) {
            assertEquals(StateException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Unknown state: UNSUPPORTED_STATUS", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getAllTest() {
        BookingDto registrationBooking = bookingService.add(bookingDto, user2.getId());
        List<BookingDto> allBookings = bookingService.getAll("ALL", user2.getId(), 0, 20);
        assertEquals(registrationBooking.getId(), allBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(start, allBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(end, allBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, allBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, allBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        List<BookingDto> waitingBookings = bookingService.getAll("WAITING", user2.getId(), 0, 20);
        assertEquals(registrationBooking.getId(), waitingBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(start, waitingBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(end, waitingBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, waitingBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, waitingBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        bookingService.statusAppropriator(registrationBooking.getId(), false, user1.getId());
        List<BookingDto> rejectedBookings = bookingService.getAll("REJECTED", user2.getId(), 0, 20);
        assertEquals(registrationBooking.getId(), rejectedBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(start, rejectedBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(end, rejectedBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.REJECTED, rejectedBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, rejectedBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        BookingDto currentBooking = bookingService.add(BookingDto.builder()
                .start(LocalDateTime.now().minusSeconds(30))
                .end(LocalDateTime.now().plusSeconds(30))
                .itemId(item.getId())
                .build(), user2.getId());
        List<BookingDto> currentBookings = bookingService.getAll("CURRENT",
                user2.getId(), 0, 20);
        assertEquals(currentBooking.getId(), currentBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(currentBooking.getStart(), currentBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(currentBooking.getEnd(), currentBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, currentBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, currentBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        BookingDto pastBooking = bookingService.add(BookingDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusHours(23))
                .itemId(item.getId())
                .build(), user2.getId());
        List<BookingDto> pastBookings = bookingService.getAll("PAST",
                user2.getId(), 0, 20);
        assertEquals(pastBooking.getId(), pastBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(pastBooking.getStart(), pastBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(pastBooking.getEnd(), pastBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, pastBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, pastBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        BookingDto futureBooking = bookingService.add(BookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(item.getId())
                .build(), user2.getId());
        List<BookingDto> futureBookings = bookingService.getAll("FUTURE",
                user2.getId(), 0, 20);
        assertEquals(futureBooking.getId(), futureBookings.get(1).getId(),
                "Id отличается.");
        assertEquals(futureBooking.getStart(), futureBookings.get(1).getStart(),
                "Время старта отличается.");
        assertEquals(futureBooking.getEnd(), futureBookings.get(1).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, futureBookings.get(1).getStatus(),
                "Статус отличается.");
        assertEquals(user2, futureBookings.get(1).getBooker(),
                "Автор бронирования отличается.");
        allBookings = bookingService.getAll("ALL", user2.getId(), 0, 20);
        assertEquals(4, allBookings.size(),
                "Число бронирований отличается.");
        allBookings = bookingService.getAll("ALL", user1.getId(), 0, 20);
        assertEquals(0, allBookings.size(),
                "Число бронирований отличается.");
    }

    @Test
    public void getByUserValidationTest() {
        bookingService.add(bookingDto, user2.getId());
        try {
            bookingService.getByUser("ALL", 999, 0, 20);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь c id: 999 не обнаружен.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.getByUser("ALL", user2.getId(), -1, 20);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение from не может быть отрицательным.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.getByUser("ALL", user2.getId(), 0, 0);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение size не может быть меньше 1.", e.getMessage(),
                    "Описание ошибки отличается");
        }
        try {
            bookingService.getByUser("imposter", user2.getId(), 0, 20);
        } catch (Exception e) {
            assertEquals(StateException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Unknown state: UNSUPPORTED_STATUS", e.getMessage(),
                    "Описание ошибки отличается");
        }
    }

    @Test
    public void getByUserTest() {
        BookingDto registrationBooking = bookingService.add(bookingDto, user2.getId());
        List<BookingDto> allBookings = bookingService.getByUser("ALL", user1.getId(), 0, 20);
        assertEquals(registrationBooking.getId(), allBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(start, allBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(end, allBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, allBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, allBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        List<BookingDto> waitingBookings = bookingService.getByUser("WAITING", user1.getId(), 0, 20);
        assertEquals(registrationBooking.getId(), waitingBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(start, waitingBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(end, waitingBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, waitingBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, waitingBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        bookingService.statusAppropriator(registrationBooking.getId(), false, user1.getId());
        List<BookingDto> rejectedBookings = bookingService.getByUser("REJECTED", user1.getId(), 0, 20);
        assertEquals(registrationBooking.getId(), rejectedBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(start, rejectedBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(end, rejectedBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.REJECTED, rejectedBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, rejectedBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        BookingDto currentBooking = bookingService.add(BookingDto.builder()
                .start(LocalDateTime.now().minusSeconds(30))
                .end(LocalDateTime.now().plusSeconds(30))
                .itemId(item.getId())
                .build(), user2.getId());
        List<BookingDto> currentBookings = bookingService.getByUser("CURRENT",
                user1.getId(), 0, 20);
        assertEquals(currentBooking.getId(), currentBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(currentBooking.getStart(), currentBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(currentBooking.getEnd(), currentBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, currentBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, currentBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        BookingDto pastBooking = bookingService.add(BookingDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusHours(23))
                .itemId(item.getId())
                .build(), user2.getId());
        List<BookingDto> pastBookings = bookingService.getByUser("PAST",
                user1.getId(), 0, 20);
        assertEquals(pastBooking.getId(), pastBookings.get(0).getId(),
                "Id отличается.");
        assertEquals(pastBooking.getStart(), pastBookings.get(0).getStart(),
                "Время старта отличается.");
        assertEquals(pastBooking.getEnd(), pastBookings.get(0).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, pastBookings.get(0).getStatus(),
                "Статус отличается.");
        assertEquals(user2, pastBookings.get(0).getBooker(),
                "Автор бронирования отличается.");
        BookingDto futureBooking = bookingService.add(BookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(item.getId())
                .build(), user2.getId());
        List<BookingDto> futureBookings = bookingService.getByUser("FUTURE",
                user1.getId(), 0, 20);
        assertEquals(futureBooking.getId(), futureBookings.get(1).getId(),
                "Id отличается.");
        assertEquals(futureBooking.getStart(), futureBookings.get(1).getStart(),
                "Время старта отличается.");
        assertEquals(futureBooking.getEnd(), futureBookings.get(1).getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.WAITING, futureBookings.get(1).getStatus(),
                "Статус отличается.");
        assertEquals(user2, futureBookings.get(1).getBooker(),
                "Автор бронирования отличается.");
        allBookings = bookingService.getByUser("ALL", user1.getId(), 0, 20);
        assertEquals(4, allBookings.size(),
                "Число бронирований отличается.");
        allBookings = bookingService.getByUser("ALL", user2.getId(), 0, 20);
        assertEquals(0, allBookings.size(),
                "Число бронирований отличается.");
    }
}