package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIncreasedConfidential;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingMapperTest {
    private final LocalDateTime start = LocalDateTime.now().plusMinutes(1);
    private final LocalDateTime end = LocalDateTime.now().plusMinutes(2);
    private final User user1 = User.builder()
            .id(1)
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final Item item = Item.builder()
            .id(1)
            .name("name")
            .description("description")
            .available(true)
            .owner(user1)
            .build();
    private final User user2 = User.builder()
            .id(2)
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final Booking booking = Booking.builder()
            .id(1)
            .start(start)
            .end(end)
            .status(Status.APPROVED)
            .booker(user2)
            .item(item)
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1)
            .start(start)
            .end(end)
            .status(Status.APPROVED)
            .bookerId(2)
            .booker(user2)
            .itemId(1)
            .item(item)
            .build();
    private final BookingMapper bookingMapper;

    @Test
    public void toBookingTest() {
        Booking testBooking = bookingMapper.toBooking(bookingDto);
        assertEquals(1, testBooking.getId(),
                "Id отличается.");
        assertEquals(start, testBooking.getStart(),
                "Время начала отличается.");
        assertEquals(end, testBooking.getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.APPROVED, testBooking.getStatus(),
                "Статус отличается.");
        assertEquals(user2, testBooking.getBooker(),
                "Автор бронирования отличается.");
        assertEquals(item, testBooking.getItem(),
                "Вещь отличается.");
    }

    @Test
    public void toBookingDtoTest() {
        BookingDto testBooking = bookingMapper.toBookingDto(booking);
        assertEquals(1, testBooking.getId(),
                "Id отличается.");
        assertEquals(start, testBooking.getStart(),
                "Время начала отличается.");
        assertEquals(end, testBooking.getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.APPROVED, testBooking.getStatus(),
                "Статус отличается.");
        assertEquals(user2, testBooking.getBooker(),
                "Автор бронирования отличается.");
        assertEquals(item, testBooking.getItem(),
                "Вещь отличается.");
    }

    @Test
    public void toBookingDtoIncreasedConfidentialTest() {
        BookingDtoIncreasedConfidential testBooking = bookingMapper.toBookingDtoIncreasedConfidential(booking);
        assertEquals(1, testBooking.getId(),
                "Id отличается.");
        assertEquals(start, testBooking.getStart(),
                "Время начала отличается.");
        assertEquals(end, testBooking.getEnd(),
                "Время окончания отличается.");
        assertEquals(Status.APPROVED, testBooking.getStatus(),
                "Статус отличается.");
        assertEquals(2, testBooking.getBookerId(),
                "Автор бронирования отличается.");
        assertEquals(1, testBooking.getItemId(),
                "Вещь отличается.");
    }
}
