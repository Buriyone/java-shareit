package ru.practicum.shareit.exception.model;

/**
 * Ошибка поиска. Возникает если данные запроса отсутствуют в базе.
 * Код ошибки 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
