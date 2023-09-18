package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

/**
 * Реализация сервиса {@link UserService}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * Предоставляет доступ к хранилищу пользователей.
     */
    private final UserRepository userRepository;
    /**
     * Предоставляет доступ к хранилищу вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Метод регистрации и добавления пользователя.
     * Генерирует {@link ValidationException} если пользователь при регистрации не указал имя или почту.
     * Генерирует {@link ConflictException} если почта уже занята.
     * @param userDto Dto-объект пользователя.
     * @return возвращает зарегистрированного пользователя
     * с присвоенным уникальны идентификатором в формате {@link UserDto}
     */
    @Override
    public UserDto add(UserDto userDto) {
        log.info("Поступил запрос на регистрацию и добавление пользователя.");
        if (userDto.getName() == null || userDto.getName().isBlank() || userDto.getEmail() == null) {
            throw new ValidationException("Имя и электронная почта не могут отсутствовать или содержать пробелы.");
        } else if (userRepository.emailValidation(userDto.getEmail()).equals(true)) {
            throw new ConflictException("Электронная почта уже занята.");
        } else {
            log.info("Пользователь успешно зарегистрирован и добавлен.");
            return toUserDto(userRepository.add(toUser(userDto)));
        }
    }

    /**
     * Сервисный метод обновления данных пользователя.
     * Генерирует {@link NotFoundException} если пользователь не найден.
     * Генерирует {@link ConflictException} если электронная почта занята.
     * @param userDto Dto-объект пользователя.
     * @param id уникальный идентификатор пользователя.
     * @return возвращает пользователя с обновленными данными в формате {@link UserDto}.
     */
    @Override
    public UserDto update(UserDto userDto, int id) {
        log.info("Поступил запрос на обновление данных пользователя.");
        if (userRepository.userChecker(id).equals(false)) {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", id));
        } else if (userRepository.emailAvailability(userDto.getEmail(), id).equals(true)) {
            throw new ConflictException("Электронная почта уже занята.");
        } else {
            userDto.setId(id);
            log.info("Данные пользователя успешно обновлены.");
            return toUserDto(userRepository.update(toUser(userDto)));
        }
    }

    /**
     * Сервисный метод удаления пользователя.
     * Генерирует {@link NotFoundException} если пользователь не найден.
     * Удаляет из системы как пользователя, так и все его вещи.
     * @param id уникальный идентификатор пользователя подлежащего удалению.
     */
    @Override
    public void delete(int id) {
        log.info("Поступил запрос на удаление пользователя.");
        if (userRepository.userChecker(id).equals(false)) {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", id));
        } else {
            log.info("Пользователь успешно удален.");
            itemRepository.deleteAllItemByUserId(id);
            userRepository.delete(id);
        }
    }

    /**
     * Сервисный метод поиска пользователя по уникальному идентификатору.
     * Генерирует {@link NotFoundException} если пользователь не найден.
     * @param id уникальный идентификатор пользователя.
     * @return возвращает пользователя в формате {@link UserDto}.
     */
    @Override
    public UserDto get(int id) {
        log.info("Поступил запрос на предоставление пользователя по уникальному идентификатору.");
        if (userRepository.userChecker(id).equals(false)) {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", id));
        } else {
            log.info("Пользователь успешно предоставлены.");
            return toUserDto(userRepository.getById(id));
        }
    }

    /**
     * Сервисный метод предоставления всех пользователей.
     * @return возвращает список пользователей в формате {@link UserDto}.
     */
    @Override
    public List<UserDto> getAll() {
        log.info("Поступил запрос на предоставление списка всех пользователей.");
        log.info("Список пользователей успешно предоставлен.");
        return userRepository.getAll().stream()
                .map(UserMapper::toUserDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
    }
}
