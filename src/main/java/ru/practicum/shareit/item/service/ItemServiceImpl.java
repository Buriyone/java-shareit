package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

/**
 * Реализация сервиса контроллера {@link ItemService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    /**
     * Предоставляет доступ к хранилищу пользователей.
     */
    private final UserRepository userRepository;
    /**
     * Предоставляет доступ к хранилищу вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Сервисный метод регистрации и добавления.
     * Проверяет входящие данные пользователя.
     * Генерирует {@link NotFoundException} если хозяин не обнаружен в системе.
     * Конвертирует входящие данные в {@link Item}.
     * @param itemDto dto-объект вещи которую добавляет пользователь.
     * @param userId  уникальный идентификатор пользователя.
     * @return возвращает вещь в формате {@link ItemDto} с приобретенным уникальным идентификатором.
     */
    @Override
    public ItemDto add(ItemDto itemDto, int userId) {
        log.info("Поступил запрос на регистрацию и добавление вещи.");
        if (userRepository.userChecker(userId).equals(false)) {
            throw new NotFoundException("Пользователь не обнаружен.");
        } else {
            itemDto.setOwner(userRepository.getById(userId));
            log.info("Вещь успешно зарегистрирована и добавлена");
            return toItemDto(itemRepository.add(toItem(itemDto)));
        }
    }

    /**
     * Сервисный метод обновления вещи конкретного пользователя.
     * Проводит проверку на наличие пользователя в системе.
     * Генерирует {@link ValidationException} если данные указаны некорректно или вещи не была зарегистрирована.
     * Генерирует {@link NotFoundException} если пользователь или вещь не обнаружены в системе.
     * Генерирует {@link ForbiddenException} если вещи не принадлежит пользователю.
     * @param itemDto dto-объект содержащий данные для обновления.
     * @param userId  уникальный идентификатор пользователя (хозяина вещи).
     * @return возвращает вещь в формате {@link ItemDto} с обновленными данными.
     */
    @Override
    public ItemDto update(ItemDto itemDto, int itemId, int userId) {
        log.info("Поступил запрос на обновление вещи пользователем с id: {}.", userId);
        if ((itemDto.getName() != null && itemDto.getName().isBlank())
                || (itemDto.getDescription() != null && itemDto.getDescription().isBlank())) {
            throw new ValidationException("Некорректно указаны данные.");
        } else if (userRepository.userChecker(userId).equals(false)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        } else if (itemId == 0) {
            throw new ValidationException("Вещь не зарегистрирована.");
        } else if (itemRepository.itemChecker(itemId).equals(false)) {
            throw new NotFoundException(String.format("Вещь с id: %d не обнаружена.", itemId));
        } else if (itemRepository.itemByUserChecker(itemId, userId).equals(false)) {
            throw new ForbiddenException(String.format("Вещь не принадлежит пользователю с id: %d.", userId));
        } else {
            log.info("Вещь пользователя с id: {} успешно обновлена.", userId);
            return toItemDto(itemRepository.update(toItem(itemDto), itemId));
        }
    }

    /**
     * Сервисный метод получения вещи по уникальному идентификатору.
     * Генерирует {@link NotFoundException} если вещь не обнаружена в системе.
     * @param itemId уникальный идентификатор вещи.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает вещь в формате {@link ItemDto} в случае если такова имеется.
     */
    @Override
    public ItemDto getById(int itemId, int userId) {
        log.info("Поступил запрос пользователя с id: {} на поиск вещи c id: {}.", userId, itemId);
        if (itemRepository.itemChecker(itemId).equals(false)) {
            throw new NotFoundException("Вещь не обнаружена.");
        } else {
            log.info("Вещь с id: {} успешно предоставлена пользователю с id: {}.", itemId, userId);
            return toItemDto(itemRepository.getById(itemId));
        }
    }

    /**
     * Сервисный метод-поисковик.
     * @param text   принимает запрос в текстовом формате.
     * @param userId принимает уникальный идентификатор пользователя осуществляющего запрос.
     * @return возвращает список доступных для аренды вещей подходящих под поисковый запрос в формате {@link ItemDto}.
     */
    @Override
    public List<ItemDto> search(String text, int userId) {
        log.info("Поступил запрос на поиск вещей по запросу пользователем с id: {}.", userId);
        log.info("Список вещей удовлетворяющий запросу предоставлен.");
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemRepository.search(text).stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Сервисный метод поиска и предоставления вещей пользователя.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает список вещей в формате {@link ItemDto}
     */
    @Override
    public List<ItemDto> getAll(int userId) {
        log.info("Поступил запрос на предоставление вещей пользователя с id: {}.", userId);
        log.info("Список вещей пользователя с id: {} успешно предоставлен.", userId);
        return itemRepository.getAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
