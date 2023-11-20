package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса {@link UserService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    /**
     * Предоставляет доступ к хранилищу для {@link User}.
     */
    private final UserRepository userRepository;
    /**
     * Маппер для конвертирования сущностей.
     */
    private final UserMapper mapper;

    /**
     * Метод регистрации и добавления пользователя.
     * Генерирует {@link ConflictException} если почта уже занята.
     * @param userDto Dto-объект пользователя.
     * @return возвращает зарегистрированного пользователя
     * с присвоенным уникальны идентификатором в формате {@link UserDto}
     */
    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        log.info("Поступил запрос на регистрацию и добавление пользователя.");
        try {
            User user = userRepository.save(mapper.toUser(userDto));
            log.info("Пользователь {} успешно зарегистрирован и добавлен c id: {}", user.getName(), user.getId());
            return mapper.toUserDto(user);
        } catch (Exception e) {
            throw new ConflictException("Электронная почта уже занята.");
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
    @Transactional
    public UserDto update(UserDto userDto, int id) {
        log.info("Поступил запрос на обновление данных пользователя.");
        return userRepository.findById(id).stream()
                .peek(user -> {
                    if (userDto.getName() != null && !userDto.getName().isBlank()) {
                        user.setName(userDto.getName());
                    }
                    if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
                        if (userRepository.findByEmailContainingIgnoreCase(userDto.getEmail()).isPresent()
                                && !user.getEmail().equals(userDto.getEmail())) {
                            throw new ConflictException("Электронная почта уже занята.");
                        } else {
                            user.setEmail(userDto.getEmail());
                        }
                    }
                    userRepository.save(user);
                    log.info("Данные пользователя успешно обновлены.");
                })
                .map(mapper::toUserDto)
                .findFirst().orElseThrow(()
                        -> new NotFoundException(String.format("Пользователь с id: %d не найден.", id)));
    }

    /**
     * Сервисный метод удаления пользователя.
     * Генерирует {@link NotFoundException} если пользователь не найден.
     * Удаляет из системы как пользователя, так и все его вещи.
     * @param id уникальный идентификатор пользователя подлежащего удалению.
     */
    @Override
    @Transactional
    public void delete(int id) {
        log.info("Поступил запрос на удаление пользователя.");
        if (userChecker(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", id));
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
        return mapper.toUserDto(userRepository.findById(id).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с id: %d не найден.", id))));
    }

    /**
     * Сервисный метод предоставления всех пользователей.
     * @return возвращает список пользователей в формате {@link UserDto}.
     */
    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Сервисный метод проверки наличия зарегистрированного пользователя.
     * @param id уникальный идентификатор пользователя.
     * @return возвращает булевое значение True - если пользователь обнаружен, False - если пользователь не обнаружен.
     */
    @Override
    public Boolean userChecker(int id) {
        return userRepository.existsById(id);
    }
}
