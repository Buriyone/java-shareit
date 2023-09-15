package ru.practicum.shareit.exception.model;

/**
 * Ошибка конфликта. Возникает если данные уже были заняты другим пользователем.
 * Код ошибки 409.
 */
public class ConflictException extends RuntimeException {
	public ConflictException(String message) {
		super(message);
	}
}
