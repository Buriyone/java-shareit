package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.assistant.State;
import ru.practicum.shareit.exception.model.StateException;
import ru.practicum.shareit.exception.model.ValidationException;

import java.util.Arrays;

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

    public static void idValidator(long id) {
        if (id == 0) {
            throw new ValidationException(String.format("Id: %d не зарегистрирован.",id));
        } else if (id < 0) {
            throw new ValidationException(String.format("Id: %d не может быть отрицательным.",id));
        }
    }

    public static void stateValidator(String state) {
        Arrays.stream(State.values())
                .filter(state1 -> state1.toString().equals(state))
                .findAny()
                .orElseThrow(() -> new StateException("Unknown state: UNSUPPORTED_STATUS"));
    }
}
