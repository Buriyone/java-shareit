package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.IgnoredForTests;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс бронирования.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
@Builder(toBuilder = true)
public class Booking {
    /**
     * Уникальный идентификатор бронирования.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Дата и время начала бронирования.
     */
    @Column(name = "start_date")
    @NotNull(message = "Время начала бронирования не может отсутствовать.")
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом.",
            groups = {IgnoredForTests.class})
    private LocalDateTime start;
    /**
     * Дата и время окончания бронирования.
     */
    @Column(name = "end_date")
    @NotNull(message = "Время окончания бронирования не может отсутствовать.")
    @FutureOrPresent(message = "Время окончания бронирования не может быть в прошлом.",
            groups = {IgnoredForTests.class})
    private LocalDateTime end;
    /**
     * Вещь, которую бронирует пользователь.
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull(message = "Вещь для бронирования не может отсутствовать.")
    private Item item;
    /**
     * Пользователь, который осуществляет бронирование.
     */
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    /**
     * Статус бронирования.
     */
    @Enumerated(EnumType.STRING)
    private Status status;
}
