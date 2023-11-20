package ru.practicum.shareit.exception.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель ошибки.
 */
@Data
@Builder(toBuilder = true)
public class ErrorResponse {
	/**
	 * Тип ошибки.
	 */
	private String actionError;
	/**
	 * Описание ошибки.
	 */
	private String description;
}
