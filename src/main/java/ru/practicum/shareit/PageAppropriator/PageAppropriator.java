package ru.practicum.shareit.PageAppropriator;

/**
 * Устанавливает валидное значение page из from;
 */
public class PageAppropriator {
    public static int pageAppropriator(int from, int size) {
        return from > 0 ? from / size : 0;
    }
}
