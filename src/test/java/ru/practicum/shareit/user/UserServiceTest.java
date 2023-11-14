package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class UserServiceTest {
    private final UserService userService;
    private final UserDto userDto = UserDto.builder().name("Ivan").email("ivan@yandex.ru").build();

    @Test
    public void addTest() {
        UserDto testUser = userService.add(userDto);
        assertTrue(testUser.getId() != 0, "Пользователь не был зарегистрирован.");
        assertEquals(userDto.getName(), testUser.getName(), "Имена пользователей отличаются.");
        assertEquals(userDto.getEmail(), testUser.getEmail(), "Электронная почта пользователей отличается");
        try {
            userService.add(UserDto.builder().name("Evanescence").email("ivan@yandex.ru").build());
        } catch (Exception e) {
            assertEquals(ConflictException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Электронная почта уже занята.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
    }

    @Test
    public void updateTest() {
        UserDto testUser = userService.add(userDto);
        testUser.setName("Van");
        userService.update(testUser, testUser.getId());
        assertEquals(userService.get(testUser.getId()).getName(), "Van",
                "Имя не сходится с обновленным");
        try {
            userService.update(testUser, 9999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 9999 не найден.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
        try {
            userService.update(UserDto.builder().name("Evanescence").email("ivan@yandex.ru").build(), testUser.getId());
        } catch (Exception e) {
            assertEquals(ConflictException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Электронная почта уже занята.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
    }

    @Test
    public void deleteTest() {
        UserDto testUser = userService.add(userDto);
        assertFalse(userService.getAll().isEmpty(), "Пользователь не был зарегистрирован.");
        try {
            userService.delete(9999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 9999 не найден.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
        userService.delete(testUser.getId());
        assertTrue(userService.getAll().isEmpty(), "Пользователь не был удавлен.");
    }

    @Test
    public void getTest() {
        UserDto testUser = userService.add(userDto);
        assertEquals(testUser, userService.get(testUser.getId()), "Пользователи не идентичны.");
        try {
            userService.get(9999);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass(), "Тип ошибки отличается.");
            assertEquals("Пользователь с id: 9999 не найден.", e.getMessage(),
                    "Сообщения об ошибке отличаются.");
        }
    }

    @Test
    public void getAllTest() {
        userService.add(userDto);
        assertEquals(userService.getAll().size(), 1, "Пользователь 1 не был добавлен.");
        userService.add(UserDto.builder().name("Nokia").email("nokia@yandex.ru").build());
        assertEquals(userService.getAll().size(), 2, "Пользователь 2 не был добавлен.");
    }

    @Test
    public void userCheckerTest() {
        UserDto testUser = userService.add(userDto);
        assertTrue(userService.userChecker(testUser.getId()), "пользователь не обнаружен.");
        assertFalse(userService.userChecker(999), "пользователь обнаружен.");
    }
}
