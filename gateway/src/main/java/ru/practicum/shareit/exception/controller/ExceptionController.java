package ru.practicum.shareit.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.ErrorResponse;
import ru.practicum.shareit.exception.model.StateError;
import ru.practicum.shareit.exception.model.StateException;
import ru.practicum.shareit.exception.model.ValidationException;

/**
 * Контроллер ошибок.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {
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
