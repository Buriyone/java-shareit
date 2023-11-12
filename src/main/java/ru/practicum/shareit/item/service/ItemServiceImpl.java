package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.validation.Validator.pageValidator;

/**
 * Реализация сервиса {@link ItemService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemServiceImpl implements ItemService {
    /**
     * Предоставляет доступ к хранилищу для {@link Item}.
     */
    private final ItemRepository itemRepository;
    /**
     * Предоставляет доступ к сервису для работы с {@link User}.
     */
    private final UserService userService;
    /**
     * Предоставляет доступ к хранилищу для {@link Booking}
     */
    private final BookingRepository bookingRepository;
    /**
     * Предоставляет доступ к хранилищу для {@link Comment}.
     */
    private final CommentRepository commentRepository;
    /**
     * Предоставляет доступ к хранилищу для {@link ItemRequest}.
     */
    private final ItemRequestRepository itemRequestRepository;
    /**
     * Список мапперов для конвертирования сущностей.
     */
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    /**
     * Сервисный метод регистрации и добавления.
     * Проверяет входящие данные пользователя.
     * Генерирует {@link NotFoundException} если владелец не обнаружен в системе.
     * Конвертирует входящие данные в {@link Item}.
     * @param itemDto dto-объект вещи которую добавляет пользователь.
     * @param userId  уникальный идентификатор пользователя.
     * @return возвращает вещь в формате {@link ItemDtoIncreasedConfidential} с приобретенным уникальным идентификатором.
     */
    @Override
    @Transactional
    public ItemDtoIncreasedConfidential add(ItemDto itemDto, int userId) {
        log.info("Поступил запрос на регистрацию и добавление вещи.");
        if (userService.userChecker(userId).equals(false)) {
            throw new NotFoundException("Пользователь не обнаружен.");
        } else {
            itemDto.setOwner(userMapper.toUser(userService.get(userId)));
            if (itemDto.getRequestId() != 0) {
                itemDto.setRequest(itemRequestRepository.findById(itemDto.getRequestId())
                        .orElseThrow(() -> new NotFoundException(String.format("Запрос с id: %d не обнаружен.",
                                itemDto.getRequestId()))));
            }
            log.info("Вещь успешно зарегистрирована и добавлена");
            return itemMapper.toItemDtoIncreasedConfidential(itemRepository.save(itemMapper.toItem(itemDto)));
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
     * @return возвращает вещь в формате {@link ItemDtoIncreasedConfidential} с обновленными данными.
     */
    @Override
    @Transactional
    public ItemDtoIncreasedConfidential update(ItemDto itemDto, int itemId, int userId) {
        log.info("Поступил запрос на обновление вещи пользователем с id: {}.", userId);
        if ((itemDto.getName() != null && itemDto.getName().isBlank())
                || (itemDto.getDescription() != null && itemDto.getDescription().isBlank())) {
            throw new ValidationException("Некорректно указаны данные.");
        } else if (userService.userChecker(userId).equals(false)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        } else if (itemId == 0) {
            throw new ValidationException("Вещь не зарегистрирована.");
        }
        return itemRepository.findById(itemId).stream()
                .peek(item -> {
                    if (item.getOwner().getId() != userId) {
                        throw new ForbiddenException(String
                                .format("Вещь c id: %d не принадлежит пользователю с id: %d.", itemId, userId));
                    } else {
                        if (itemDto.getName() != null) {
                            item.setName(itemDto.getName());
                        }
                        if (itemDto.getDescription() != null) {
                            item.setDescription(itemDto.getDescription());
                        }
                        if (itemDto.getAvailable() != null) {
                            item.setAvailable(itemDto.getAvailable());
                        }
                        itemRepository.save(item);
                        log.info("Вещь пользователя с id: {} успешно обновлена.", userId);
                    }
                })
                .map(itemMapper::toItemDtoIncreasedConfidential)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь c id %d не обнаружена.", itemId)));
    }

    /**
     * Сервисный метод получения вещи по уникальному идентификатору.
     * Работает в двух режимах:
     * 1) Предоставление данных для владельца вещи.
     * 2) Предоставления данных для остальных пользователей.
     * Владельцу предоставляются данные о прошлых и ближайших бронированиях.
     * Генерирует {@link NotFoundException} если вещь или пользователь не обнаружены в системе.
     * @param itemId уникальный идентификатор вещи.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает вещь в формате {@link ItemDtoIncreasedConfidential} в случае если такова имеется.
     */
    @Override
    @Transactional
    public ItemDtoIncreasedConfidential getById(int itemId, int userId) {
        log.info("Поступил запрос пользователя с id: {} на поиск вещи c id: {}.", userId, itemId);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        }
        return itemRepository.findById(itemId).stream()
                .map(itemMapper::toItemDtoIncreasedConfidential)
                .peek(itemDto -> {
                    if (itemDto.getOwner().getId() == userId) {
                        bookingSetter(itemDto);
                        log.info("Вещь с id: {} успешно предоставлена владельцу.", itemId);
                    } else {
                        log.info("Вещь с id: {} успешно предоставлена пользователю с id: {}.", itemId, userId);
                    }
                })
                .map(this::commentSetter)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь c id %d не обнаружена.", itemId)));
    }

    /**
     * Сервисный метод-поисковик, возвращает результат постранично.
     * Генерирует {@link NotFoundException} если пользователь не был найден.
     * @param from индекс первого элемента.
     * @param size количество элементов для отображения.
     * @param text   принимает запрос в текстовом формате.
     * @param userId принимает уникальный идентификатор пользователя осуществляющего запрос.
     * @return возвращает список доступных для аренды вещей
     * подходящих под поисковый запрос в формате {@link ItemDtoIncreasedConfidential}.
     */
    @Override
    public List<ItemDtoIncreasedConfidential> search(String text, int userId, int from, int size) {
        log.info("Поступил запрос на поиск вещей по запросу пользователем с id: {}.", userId);
        pageValidator(from, size);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        }
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemRepository.search(text, PageRequest.of(from > 0 ? from / size : 0, size))
                    .stream()
                    .filter(item -> item.getAvailable().equals(true))
                    .map(itemMapper::toItemDtoIncreasedConfidential)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Сервисный метод поиска и предоставления всех вещей пользователя постранично.
     * Генерирует {@link NotFoundException} если пользователь не был найден.
     * @param from индекс первого элемента.
     * @param size количество элементов для отображения.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает список вещей в формате {@link ItemDtoIncreasedConfidential}
     */
    @Override
    @Transactional
    public List<ItemDtoIncreasedConfidential> getAll(int userId, int from, int size) {
        log.info("Поступил запрос на предоставление вещей пользователя с id: {}.", userId);
        pageValidator(from, size);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        }
        return itemRepository.findItemByOwnerId(userId, PageRequest.of(from > 0 ? from / size : 0, size))
                .stream()
                .map(itemMapper::toItemDtoIncreasedConfidential)
                .map(this::bookingSetter)
                .map(this::commentSetter)
                .collect(Collectors.toList());
    }

    /**
     * Метод проверки наличия вещи в хранилище.
     * @param itemId уникальный идентификатор вещи.
     * @return возвращает булевой ответ True - если найдена, False - если не найдена.
     */
    @Override
    public boolean itemChecker(int itemId) {
        return itemRepository.existsById(itemId);
    }

    /**
     * Сервисный метод поиска и присвоения последнего и следующего бронирования вещи.
     * @param itemDto DTO-объект вещи для которой осуществляется присвоение данных.
     * @return возвращает DTO-объект вещи с присвоенными данными.
     */
    private ItemDtoIncreasedConfidential bookingSetter(ItemDtoIncreasedConfidential itemDto) {
        bookingRepository.findTopByItemIdAndStartIsBeforeAndStatus(itemDto.getId(),
                LocalDateTime.now(), Status.APPROVED,
                Sort.by(Sort.Direction.DESC, "start"))
                .ifPresent(booking -> itemDto.setLastBooking(bookingMapper.toBookingDtoIncreasedConfidential(booking)));
        bookingRepository.findTopByItemIdAndStartIsAfterAndStatus(itemDto.getId(),
                LocalDateTime.now(), Status.APPROVED, Sort.by("start"))
                .ifPresent(booking -> itemDto.setNextBooking(bookingMapper.toBookingDtoIncreasedConfidential(booking)));
        return itemDto;
    }

    /**
     * Сервисный метод добавления комментария.
     * Генерирует {@link NotFoundException} если пользователь или вещь не были обнаружены.
     * Генерирует {@link ValidationException} если пользователь не брал вещь в аренду.
     * @param itemId уникальный идентификатор вещи.
     * @param userId уникальный идентификатор пользователя.
     * @param commentDto DTO-объект комментария.
     * @return возвращает конфиденциальный DTO-объект комментария с присвоенным уникальным идентификатором.
     */
    @Override
    @Transactional
    public CommentDtoIncreasedConfidential addComment(int itemId, int userId, CommentDto commentDto) {
        log.info("Поступил запрос на добавление комментария пользователем с id: {} к вещи с id: {}", userId, itemId);
        if (!itemChecker(itemId)) {
            throw new NotFoundException(String.format("Вещь c id: %d не обнаружена.", itemId));
        } else if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        } else {
            bookingRepository.findTopByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, userId, Status.APPROVED,
                            LocalDateTime.now()).stream()
                    .peek(booking -> {
                        commentDto.setItem(booking.getItem());
                        commentDto.setAuthor(booking.getBooker());
                        commentDto.setCreated(LocalDateTime.now());
                        log.info("Комментарий успешно добавлен пользователем с id: {}", userId);
                    }).findFirst().orElseThrow(() -> new ValidationException(String
                            .format("Пользователь с id: %d не брал в аренду вещь с id: %d", userId, itemId)));
            log.info("Комментарий успешно добавлен пользователем с id: {}", userId);
            return commentMapper.toCommentDtoIncreasedConfidential(commentRepository
                    .save(commentMapper.toComment(commentDto)));
        }
    }

    /**
     * Сервисный метод поиска и присвоения данных о комментариях.
     * @param itemDto DTO-объект вещи.
     * @return возвращает обработанный DTO-объект вещи.
     */
    private ItemDtoIncreasedConfidential commentSetter(ItemDtoIncreasedConfidential itemDto) {
        itemDto.setComments(commentRepository.findByItemId(itemDto.getId()).stream()
                .map(commentMapper::toCommentDtoIncreasedConfidential)
                .collect(Collectors.toList()));
        return itemDto;
    }
}
