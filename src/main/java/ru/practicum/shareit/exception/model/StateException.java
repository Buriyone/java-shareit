package ru.practicum.shareit.exception.model;

/**
 * Ошибка параметров. Возникает если был передан неподдерживаемый тип.
 * Код ошибки 400.
 */
public class StateException extends RuntimeException {
    public StateException(String message) {
        super(message);
    }
}
