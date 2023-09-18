package ru.practicum.shareit.booking.assistant;

/**
 * Перечисление типов статусов бронирования.
 */
public enum Status {
    /**
     * Новое бронирование, ожидает одобрения.
     */
    WAITING,
    /**
     * Бронирование подтверждено владельцем.
     */
    APPROVED,
    /**
     * Бронирование отклонено владельцем.
     */
    REJECTED,
    /**
     * Бронирование отменено создателем.
     */
    CANCELED
}