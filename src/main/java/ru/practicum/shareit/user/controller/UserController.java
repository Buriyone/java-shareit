package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для работы с пользователями.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * Предоставляет доступ к сервису работы с пользователями.
     */
    public final UserService userService;

    /**
     * Обрабатывает запрос на получение списка всех пользователей.
     */
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    /**
     * Обрабатывает запрос на регистрацию и добавление пользователя.
     */
    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    /**
     * Обрабатывает запрос на обновление данных пользователя.
     */
    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable int userId) {
        return userService.update(userDto, userId);
    }

    /**
     * Обрабатывает запросы удаления пользователя.
     */
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }

    /**
     * Обрабатывает запросы предоставления пользователя по уникальному идентификатору.
     */
    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        return userService.get(userId);
    }
}
