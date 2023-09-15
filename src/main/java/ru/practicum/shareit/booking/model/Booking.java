package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс бронирования.
 */
@Data
@Builder(toBuilder = true)
public class Booking {
    /**
     * Уникальный идентификатор бронирования.
     */
    private int id;
    /**
     * Дата и время начала бронирования.
     */
    private LocalDateTime start;
    /**
     * Дата и время окончания бронирования.
     */
    private LocalDateTime end;
    /**
     * Вещь, которую бронирует пользователь.
     */
    private Item item;
    /**
     * Пользователь, который осуществляет бронирование.
     */
    private User booker;
    /**
     * Статус бронирования.
     */
    private Status status;
}
