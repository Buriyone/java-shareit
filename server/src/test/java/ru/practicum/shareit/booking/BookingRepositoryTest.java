package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    private final TestEntityManager entityManager;
    private final BookingRepository bookingRepository;
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = start.plusHours(1);
    private final Pageable pageable = PageRequest.of(0, 20,
            Sort.by(Sort.Direction.DESC, "start"));
    private final User user1 = User.builder()
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final Item item = Item.builder()
            .name("item")
            .description("description")
            .available(true)
            .build();
    private final User user2 = User.builder()
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final Booking booking = Booking.builder()
            .status(Status.WAITING)
            .start(start)
            .end(end)
            .build();

    @BeforeEach
    public void saver() {
        this.entityManager.persist(user1);
        item.setOwner(user1);
        this.entityManager.persist(item);
        this.entityManager.persist(user2);
        booking.setItem(item);
        booking.setBooker(user2);
    }

    @Test
    public void findBookingByBookerIdAndStartIsBeforeAndEndIsAfterTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime.minusSeconds(30));
        booking.setEnd(currentTime.plusSeconds(30));
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(user2.getId(), currentTime,
                        currentTime, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByBookerIdAndEndIsBeforeTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime.minusHours(2));
        booking.setEnd(currentTime.minusHours(1));
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByBookerIdAndEndIsBefore(user2.getId(), currentTime, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByBookerIdAndStartIsAfterTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime.plusHours(1));
        booking.setEnd(currentTime.plusHours(2));
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByBookerIdAndStartIsAfter(user2.getId(), currentTime, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByBookerIdAndStatusWaitingTest() {
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByBookerIdAndStatus(user2.getId(), Status.WAITING, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByBookerIdAndStatusRejectedTest() {
        booking.setStatus(Status.REJECTED);
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByBookerIdAndStatus(user2.getId(), Status.REJECTED, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByBookerIdTest() {
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByBookerId(user2.getId(), pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfterTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime.minusSeconds(30));
        booking.setEnd(currentTime.plusSeconds(30));
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfter(user1.getId(), currentTime,
                        currentTime, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByItemOwnerIdAndEndIsBeforeTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime.minusHours(2));
        booking.setEnd(currentTime.minusHours(1));
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByItemOwnerIdAndEndIsBefore(user1.getId(), currentTime, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByItemOwnerIdAndStartIsAfterTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime.plusHours(1));
        booking.setEnd(currentTime.plusHours(2));
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByItemOwnerIdAndStartIsAfter(user1.getId(), currentTime, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByItemOwnerIdAndStatusWaitingTest() {
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByItemOwnerIdAndStatus(user1.getId(), Status.WAITING, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByItemOwnerIdAndStatusRejectedTest() {
        booking.setStatus(Status.REJECTED);
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByItemOwnerIdAndStatus(user1.getId(), Status.REJECTED, pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findBookingByItemOwnerIdTest() {
        this.entityManager.persist(booking);
        List<Booking> testList = bookingRepository
                .findBookingByItemOwnerId(user1.getId(), pageable).toList();
        assertEquals(booking, testList.get(0), "Бронирования отличаются.");
        assertEquals(item, testList.get(0).getItem(), "Вещи отличаются");
        assertEquals(user2, testList.get(0).getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findTopByItemIdAndStartIsBeforeAndStatusTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStatus(Status.APPROVED);
        booking.setStart(currentTime.minusHours(2));
        booking.setEnd(currentTime.minusHours(1));
        this.entityManager.persist(booking);
        Booking testBooking = bookingRepository
                .findTopByItemIdAndStartIsBeforeAndStatus(item.getId(),currentTime, Status.APPROVED,
                        Sort.by(Sort.Direction.DESC, "start")).orElse(new Booking());
        assertThat(testBooking).isEqualTo(booking);
        assertEquals(booking, testBooking, "Бронирования отличаются.");
        assertEquals(item, testBooking.getItem(), "Вещи отличаются");
        assertEquals(user2, testBooking.getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findTopByItemIdAndStartIsAfterAndStatusTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStatus(Status.APPROVED);
        booking.setStart(currentTime.plusHours(1));
        booking.setEnd(currentTime.plusHours(2));
        this.entityManager.persist(booking);
        Booking testBooking = bookingRepository
                .findTopByItemIdAndStartIsAfterAndStatus(item.getId(),currentTime, Status.APPROVED,
                        Sort.by(Sort.Direction.DESC, "start")).orElse(new Booking());
        assertEquals(booking, testBooking, "Бронирования отличаются.");
        assertEquals(item, testBooking.getItem(), "Вещи отличаются");
        assertEquals(user2, testBooking.getBooker(), "Автор бронирования отличается.");
    }

    @Test
    public void findTopByItemIdAndBookerIdAndStatusAndEndIsBeforeTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStatus(Status.APPROVED);
        booking.setStart(currentTime.minusHours(2));
        booking.setEnd(currentTime.minusHours(1));
        this.entityManager.persist(booking);
        Booking testBooking = bookingRepository
                .findTopByItemIdAndBookerIdAndStatusAndEndIsBefore(item.getId(), user2.getId(),
                        Status.APPROVED, currentTime).orElse(new Booking());
        assertEquals(booking, testBooking, "Бронирования отличаются.");
        assertEquals(item, testBooking.getItem(), "Вещи отличаются");
        assertEquals(user2, testBooking.getBooker(), "Автор бронирования отличается.");
    }

    @BeforeEach
    public void cleaner() {
        this.entityManager.clear();
    }
}
