package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.PageAppropriator.PageAppropriator.pageAppropriator;
import static ru.practicum.shareit.validation.Validator.pageValidator;

/**
 * Реализация сервиса {@link ItemRequestService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    /**
     * Предоставляет доступ к хранилищу для {@link ItemRequest}
     */
    private final ItemRequestRepository itemRequestRepository;
    /**
     * Предоставляет доступ к сервису пользователей.
     */
    private final UserService userService;
    /**
     * Предоставляет доступ к хранилищу для {@link User}
     */
    private final UserRepository userRepository;
    /**
     * Предоставляет доступ к хранилищу для {@link Item}.
     */
    private final ItemRepository itemRepository;
    /**
     * Список мапперов.
     */
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    /**
     * Сервисный метод регистрации запросов пользователей.
     * @param itemRequestDto DTO-объект описывающий вещь для запроса.
     * @param userId уникальный идентификатор пользователя который осуществляет регистрацию запроса.
     * @return возвращает зарегистрированный DTO-объект.
     */
    @Override
    @Transactional
    public ItemRequestDto add(ItemRequestDto itemRequestDto, int userId) {
        log.info("Поступил запрос на регистрацию запроса вещи пользователем с id: {}.", userId);
        itemRequestDto.setRequestor(userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с id: %d не найден.", userId))));
        itemRequestDto.setCreated(LocalDateTime.now());
        log.info("Запрос успешно зарегистрирован.");
        return itemRequestMapper.toItemRequestDto(itemRequestRepository
                .save(itemRequestMapper.toItemRequest(itemRequestDto)));
    }

    /**
     * Сервисный метод предоставляет список запросов на аренду конкретного пользователя.
     * Обогащает DTO-объекты данными об ответах на запрос.
     * Генерирует {@link NotFoundException} если пользователь не был обнаружен.
     * @param userId уникальный идентификатор пользователя для которого осуществляется предоставление данных.
     * @return возвращает список запросов в формате {@link ItemRequestDto}.
     */
    @Override
    public List<ItemRequestDto> get(int userId) {
        log.info("Поступил запрос на предоставления списка запросов для пользователя с id: {}.", userId);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", userId));
        } else {
            log.info("Список запросов успешно предоставлен пользователю с id {}", userId);
            return itemRequestRepository.findByRequestorId(userId, Sort.by(Sort.Direction.DESC, "created"))
                    .stream()
                    .map(itemRequestMapper::toItemRequestDto)
                    .peek(itemRequestDto -> itemRequestDto
                            .setItems(itemRepository.findByRequestId(itemRequestDto.getId()).stream()
                                    .map(itemMapper::toItemDtoForRequest)
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Сервисный метод для получения списка запросов пользователей.
     * Предоставление списка осуществляется постранично.
     * Генерирует {@link NotFoundException} если пользователь не обнаружен.
     * Генерирует {@link ValidationException} если значение from отрицательное или если size меньше 1.
     * @param from индекс первого элемента.
     * @param size количество элементов для отображения.
     * @param userId уникальный идентификатор пользователя осуществляющего запрос.
     * @return возвращает список запросов пользователей в формате {@link ItemRequestDto}.
     */
    @Override
    public List<ItemRequestDto> getAll(int from, int size, int userId) {
        log.info("Поступил запрос на предоставление списка запросов пользователей.");
        log.info("Список запросов пользователей успешно предоставлен.");
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", userId));
        }
        pageValidator(from, size);
        return itemRequestRepository.findAllByRequestorIdNot(userId, PageRequest.of(pageAppropriator(from, size), size,
                        Sort.by(Sort.Direction.DESC, "created")))
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(itemRequestDto -> itemRequestDto
                        .setItems(itemRepository.findByRequestId(itemRequestDto.getId())
                                .stream()
                                .map(itemMapper::toItemDtoForRequest)
                                .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    /**
     * Сервисный метод для получения запроса по уникальному идентификатору.
     * Генерирует {@link NotFoundException} если пользователь или запрос не найдены.
     * @param requestId уникальный идентификатор запроса по которому осуществляется поиск.
     * @param userId уникальный пользователь для которого осуществляется поиск.
     * @return возвращает список запросов в формате {@link ItemRequestDto}.
     */
    @Override
    public ItemRequestDto getById(int requestId, int userId) {
        log.info("Поступил запрос на предоставление запроса по уникальному идентификатору.");
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь с id: %d не найден.", userId));
        } else {
            return itemRequestRepository.findById(requestId)
                    .stream()
                    .map(itemRequestMapper::toItemRequestDto)
                    .peek(itemRequestDto -> itemRequestDto.setItems(itemRepository.findByRequestId(requestId)
                            .stream()
                            .map(itemMapper::toItemDtoForRequest)
                            .collect(Collectors.toList())))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String
                            .format("Запрос с id: %d не найден.", requestId)));
        }
    }

    @Override
    public Boolean itemRequestChecker(int requestId) {
        return itemRequestRepository.existsById(requestId);
    }
}
