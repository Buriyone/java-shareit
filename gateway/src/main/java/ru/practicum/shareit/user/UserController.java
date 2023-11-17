package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

/**
 * Клиентский контроллер для работы с пользователями.
 */
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    /**
     * Предоставляет доступ к сервису работы с пользователями.
     */
    private final UserClient userClient;

    /**
     * Обрабатывает запрос на получение списка всех пользователей.
     */
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Получен запрос на предоставление списка всех пользователей.");
        return userClient.getAll();
    }

    /**
     * Обрабатывает запрос на регистрацию и добавление пользователя.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@Validated(Create.class)
                                      @RequestBody UserRequestDto userRequestDto) {
        log.info("Получен запрос на регистрацию пользователя.");
        return userClient.add(userRequestDto);
    }

    /**
     * Обрабатывает запрос на обновление данных пользователя.
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated(Update.class)
                                         @RequestBody UserRequestDto userRequestDto,
                                         @PathVariable int userId) {
        return userClient.update(userId, userRequestDto);
    }

    /**
     * Обрабатывает запросы удаления пользователя.
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int userId) {
        userClient.delete(userId);
    }

    /**
     * Обрабатывает запросы предоставления пользователя по уникальному идентификатору.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable int userId) {
        return userClient.get(userId);
    }
}
