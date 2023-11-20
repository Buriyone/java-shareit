package ru.practicum.shareit.exception.model;

/**
 * Ошибка валидации. Возникает если входящие данные переданы некорректно.
 * Код ошибки 400.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
