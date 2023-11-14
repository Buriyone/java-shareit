package ru.practicum.shareit.validation;

import ru.practicum.shareit.exception.model.ValidationException;

/**
 * Валидатор поступающих параметров.
 * Генерирует {@link ValidationException} если значение from отрицательное или если size меньше 1.
 */
public class Validator {
    public static void pageValidator(int from, int size) {
        if (from < 0) {
            throw new ValidationException("Значение from не может быть отрицательным.");
        } else if (size < 1) {
            throw new ValidationException("Значение size не может быть меньше 1.");
        }
    }
}
