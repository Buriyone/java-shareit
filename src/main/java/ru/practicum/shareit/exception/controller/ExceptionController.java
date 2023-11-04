package ru.practicum.shareit.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.*;

/**
 * Контроллер ошибок.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {
	/**
	 * Отлавливает ошибку {@link NotFoundException}.
	 * @return возвращает сведения об ошибке.
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse notFoundException(final NotFoundException exception) {
		log.info("Ошибка поиска. {}", exception.getMessage());
		return ErrorResponse.builder()
				.actionError("Ошибка поиска.")
				.description(exception.getMessage())
				.build();
	}

	/**
	 * Отлавливает ошибку {@link ValidationException}
	 * @return возвращает сведения об ошибке.
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse validationException(final ValidationException exception) {
		log.info("Ошибка валидации. {}", exception.getMessage());
		return ErrorResponse.builder()
				.actionError("Ошибка валидации.")
				.description(exception.getMessage())
				.build();
	}

	/**
	 * Отлавливает ошибку {@link ConflictException}
	 * @return возвращает сведения об ошибке.
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse conflictException(final ConflictException exception) {
		log.info("Конфликт запроса. {}", exception.getMessage());
		return ErrorResponse.builder()
				.actionError("Конфликт запроса.")
				.description(exception.getMessage())
				.build();
	}

	/**
	 * Отлавливает ошибку {@link ForbiddenException}.
	 * @return возвращает сведения об ошибке.
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse forbiddenException(final ForbiddenException exception) {
		log.info("В доступе отказано. {}", exception.getMessage());
		return ErrorResponse.builder()
				.actionError("В доступе отказано.")
				.description(exception.getMessage())
				.build();
	}

	/**
	 * Отлавливает ошибку {@link javax.validation.ValidationException}.
	 * @return возвращает сведения об ошибке.
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse javaxValidation(final javax.validation.ValidationException exception) {
		log.info("Ошибка валидации. {}", exception.getMessage());
		return ErrorResponse.builder()
				.actionError("Ошибка валидации.")
				.description(exception.getMessage())
				.build();
	}

	/**
	 * Отлавливает ошибку {@link StateException}
	 * @return возвращает сведения об ошибке.
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public StateError stateException(final StateException exception) {
		log.info("Ошибка параметра. {}", exception.getMessage());
		return StateError.builder()
				.error(exception.getMessage())
				.build();
	}
}
