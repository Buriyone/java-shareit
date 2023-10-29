package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
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
    private LocalDateTime start;
    /**
     * Дата и время окончания бронирования.
     */
    @Column(name = "end_date")
    private LocalDateTime end;
    /**
     * Вещь, которую бронирует пользователь.
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
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
