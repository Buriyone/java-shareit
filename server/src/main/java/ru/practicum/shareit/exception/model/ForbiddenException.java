package ru.practicum.shareit.exception.model;

/**
 * Отказ в доступе. Возникает если пользователь пытается получить доступ к чужим данным.
 * Код ошибки 403.
 */
public class ForbiddenException extends RuntimeException {
	public ForbiddenException(String message) {
		super(message);
	}
}
