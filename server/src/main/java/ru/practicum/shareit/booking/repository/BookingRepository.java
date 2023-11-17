package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Хранилище для {@link Booking}.
 */
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(int userId,
                                                                     LocalDateTime start,
                                                                     LocalDateTime end,
                                                                     Pageable pageable);

    Page<Booking> findBookingByBookerIdAndEndIsBefore(int userId,
                                                      LocalDateTime currentTime,
                                                      Pageable pageable);

    Page<Booking> findBookingByBookerIdAndStartIsAfter(int userId,
                                                       LocalDateTime currentTime,
                                                       Pageable pageable);

    Page<Booking> findBookingByBookerIdAndStatus(int userId,
                                                 Status status,
                                                 Pageable pageable);

    Page<Booking> findBookingByBookerId(int userId,
                                        Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfter(int userId,
                                                                        LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndEndIsBefore(int userId,
                                                         LocalDateTime currentTime,
                                                         Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndStartIsAfter(int userId,
                                                          LocalDateTime currentTime,
                                                          Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndStatus(int userId,
                                                    Status status,
                                                    Pageable pageable);

    Page<Booking> findBookingByItemOwnerId(int userId,
                                           Pageable pageable);

    Optional<Booking> findTopByItemIdAndStartIsBeforeAndStatus(int itemId,
                                                               LocalDateTime currentTime,
                                                               Status status,
                                                               Sort sort);

    Optional<Booking> findTopByItemIdAndStartIsAfterAndStatus(int itemId,
                                                              LocalDateTime currentTime,
                                                              Status status,
                                                              Sort sort);

    Optional<Booking> findTopByItemIdAndBookerIdAndStatusAndEndIsBefore(int itemId,
                                                                        int bookerId,
                                                                        Status status,
                                                                        LocalDateTime currentTime);
}
