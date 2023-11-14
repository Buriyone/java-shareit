package ru.practicum.shareit.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.model.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.validation.Validator.pageValidator;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ValidatorTest {
    @Test
    public void pageValidatorTest() {
        try {
            pageValidator(-1, 20);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение from не может быть отрицательным.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
        try {
            pageValidator(0, 0);
        } catch (Exception e) {
            assertEquals(ValidationException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Значение size не может быть меньше 1.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
    }
}
