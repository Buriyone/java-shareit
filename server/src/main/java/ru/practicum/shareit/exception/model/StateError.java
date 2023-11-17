package ru.practicum.shareit.exception.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель ошибки параметра.
 */
@Data
@Builder(toBuilder = true)
public class StateError {
    private String error;
}
