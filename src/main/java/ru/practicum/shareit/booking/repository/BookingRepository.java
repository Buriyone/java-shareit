package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Хранилище для {@link Booking}.
 */
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(int userId,
                                                                     LocalDateTime time1,
                                                                     LocalDateTime time2,
                                                                     Sort sort);

    List<Booking> findBookingByBookerIdAndEndIsBefore(int userId,
                                                      LocalDateTime time,
                                                      Sort sort);

    List<Booking> findBookingByBookerIdAndStartIsAfter(int userId,
                                                       LocalDateTime time,
                                                       Sort sort);

    List<Booking> findBookingByBookerIdAndStatus(int userId,
                                                 Status status,
                                                 Sort sort);

    List<Booking> findBookingByBookerId(int userId,
                                        Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfter(int userId,
                                                                        LocalDateTime time1,
                                                                        LocalDateTime time2,
                                                                        Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsBefore(int userId,
                                                         LocalDateTime time,
                                                         Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStartIsAfter(int userId,
                                                          LocalDateTime time,
                                                          Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStatus(int userId,
                                                    Status status,
                                                    Sort sort);

    List<Booking> findBookingByItemOwnerId(int userId,
                                           Sort sort);

    Optional<Booking> findTopByItemIdAndStartIsBeforeAndStatus(int itemId,
                                                               LocalDateTime time,
                                                               Status status,
                                                               Sort sort);

    Optional<Booking> findTopByItemIdAndStartIsAfterAndStatus(int itemId,
                                                              LocalDateTime time,
                                                              Status status,
                                                              Sort sort);

    Optional<Booking> findTopByItemIdAndBookerIdAndStatusAndEndIsBefore(int itemId,
                                                                        int bookerId,
                                                                        Status status,
                                                                        LocalDateTime time);
}
