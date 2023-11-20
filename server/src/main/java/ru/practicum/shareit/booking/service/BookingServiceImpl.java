package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.assistant.State;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.PageAppropriator.PageAppropriator.pageAppropriator;

/**
 * Реализация интерфейса {@link BookingService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    /**
     * Предоставляет доступ к хранилищу {@link Booking}.
     */
    private final BookingRepository bookingRepository;
    /**
     * Предоставляет доступ к хранилищу {@link Item}.
     */
    private final ItemRepository itemRepository;
    /**
     * Предоставляет доступ к хранилищу {@link User}.
     */
    private final UserRepository userRepository;
    /**
     * Предоставляет доступ к сервису для работы с {@link User}.
     */
    private final UserService userService;
    /**
     * Маппер для конвертирования сущностей.
     */
    private final BookingMapper bookingMapper;

    /**
     * Метод регистрирует и сохраняет бронирование в {@link BookingRepository}.
     * Генерирует {@link ValidationException} если были нарушены требования.
     * Генерирует {@link NotFoundException} если сущность не была обнаружена.
     * @param bookingDto DTO-объект бронирования.
     * @param userId уникальный идентификатор пользователя который осуществляет бронирование.
     * @return возвращает обработанный DTO-объект с присвоенным уникальным идентификатором.
     */
    @Override
    @Transactional
    public BookingDto add(BookingDto bookingDto, int userId) {
        log.info("Поступил запрос на регистрацию бронирования.");
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Время окончания бронирования не может быть раньше начала бронирования " +
                    "или совпадать с ним.");
        }
        bookingDto.setItem(itemRepository.findById(bookingDto.getItemId()).stream()
                .peek(item -> {
                    if (item.getAvailable().equals(false)) {
                        throw new ValidationException("Вещь не доступна для бронирования.");
                    } else if (item.getOwner().getId() == userId) {
                        throw new NotFoundException("Владелец вещи не может оформлять бронирование.");
                    }
                })
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Вещь c id: %d не обнаружена.",
                        bookingDto.getItemId()))));
        bookingDto.setBooker(userRepository.findById(userId).stream()
                .findFirst().orElseThrow(() -> new NotFoundException(String
                        .format("Пользователь c id: %d не обнаружен.", userId))));
        bookingDto.setStatus(Status.WAITING);
        log.info("Бронирование зарегистрировано.");
        return bookingMapper.toBookingDto(bookingRepository.save(bookingMapper.toBooking(bookingDto)));
    }

    /**
     * Метод обработки статуса бронирования владельцем вещи.
     * Генерирует {@link NotFoundException} если пользователь не обнаружен или вещи не принадлежит пользователю.
     * Генерирует {@link ValidationException} если бронирование было обработано ранее.
     * @param bookingId уникальный идентификатор вещи.
     * @param approve статус бронирования.
     * @param userId уникальный идентификатор владельца вещи.
     * @return возвращает итоговый DTO-объект с присвоенным статусом.
     */
    @Override
    @Transactional
    public BookingDto statusAppropriator(int bookingId, boolean approve, int userId) {
        log.info("Поступил запрос на обработку статуса бронирования пользователем с id: {}.", userId);
        return bookingRepository.findById(bookingId).stream()
                .peek(booking -> {
                    if (!userService.userChecker(userId)) {
                        throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
                    } else if (booking.getItem().getOwner().getId() != userId) {
                        throw new NotFoundException(String
                                .format("Вещь с id: %d не принадлежит пользователю с id: %d.",
                                        booking.getItem().getId(), userId));
                    } else if (!booking.getStatus().equals(Status.WAITING)) {
                        throw new ValidationException("Бронирование уже было обработано.");
                    } else {
                        booking.setStatus(approve ? Status.APPROVED : Status.REJECTED);
                    }
                }).peek(booking -> {
                    if (booking.getStatus().equals(Status.REJECTED)) {
                        log.info("В бронировании отказано.");
                    } else if (booking.getStatus().equals(Status.APPROVED)) {
                        log.info("Бронирование подтверждено.");
                    }
                })
                .map(bookingMapper::toBookingDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String
                        .format("Бронирование c id: %d не обнаружено.", bookingId)));
    }

    /**
     * Метод предоставления данные о бронировании для владельца вещи или для пользователя осуществившего бронирование.
     * Генерирует {@link NotFoundException} если не обнаружено бронирование или пользователь,
     * или если пользователь не является автором бронирования или владельцем вещи.
     * @param bookingId уникальный идентификатор бронирования.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает DTO-объект бронирования.
     */
    @Override
    public BookingDto get(int bookingId, int userId) {
        log.info("Поступил запрос на предоставление данных о бронировании пользователем с id: {}", userId);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        }
        return bookingRepository.findById(bookingId).stream()
                .peek(booking -> {
                    if (booking.getBooker().getId() != userId
                            && booking.getItem().getOwner().getId() != userId) {
                        throw new NotFoundException(String.format("Пользователь с id: %d не является автором " +
                                "бронирования или владельцем вещи.", userId));
                    }
                    log.info("Данные о бронировании c id: {} предоставлены пользователю с id: {}", bookingId, userId);
                })
                .map(bookingMapper::toBookingDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String
                        .format("Бронирование с id: %d не обнаружено.", bookingId)));
    }

    /**
     * Метод предоставления списка бронирований по параметру. По умолчанию предоставляет все вещи.
     * Генерирует {@link NotFoundException} если пользователь не был обнаружен.
     * Возвращает результат постранично.
     * @param from индекс первого элемента.
     * @param size количество элементов для отображения.
     * @param param переданный параметр-статус.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает список бронирований.
     */
    @Override
    public List<BookingDto> getAll(String param, int userId, int from, int size) {
        log.info("Поступил запрос на предоставление всех бронирований " +
                "пользователя c id: {} по параметру: {}", userId, param);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        } else {
            Pageable pageable = PageRequest.of(pageAppropriator(from, size), size,
                    Sort.by(Sort.Direction.DESC, "start"));
            LocalDateTime currentTime = LocalDateTime.now();
            switch (State.valueOf(param)) {
                case CURRENT:
                    return converter(bookingRepository.findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(userId,
                            currentTime, currentTime, pageable));
                case PAST:
                    return converter(bookingRepository.findBookingByBookerIdAndEndIsBefore(userId,
                            currentTime, pageable));
                case FUTURE:
                    return converter(bookingRepository.findBookingByBookerIdAndStartIsAfter(userId,
                            currentTime, pageable));
                case WAITING:
                    return converter(bookingRepository.findBookingByBookerIdAndStatus(userId,
                            Status.WAITING, pageable));
                case REJECTED:
                    return converter(bookingRepository.findBookingByBookerIdAndStatus(userId,
                            Status.REJECTED, pageable));
                default:
                    return converter(bookingRepository.findBookingByBookerId(userId, pageable));
            }
        }
    }

    /**
     * Метод предоставления списка бронирований по параметру для владельца вещи. По умолчания предоставляет все вещи.
     * Генерирует {@link NotFoundException} если пользователь не был обнаружен.
     * @param from индекс первого элемента.
     * @param size количество элементов для отображения.
     * @param param переданный параметр-статус.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает список бронирований.
     */
    @Override
    public List<BookingDto> getByUser(String param, int userId, int from, int size) {
        log.info("Поступил запрос на предоставление всех бронирований " +
                "для владельца c id: {} по параметру: {}", userId, param);
        if (!userService.userChecker(userId)) {
            throw new NotFoundException(String.format("Пользователь c id: %d не обнаружен.", userId));
        } else {
            Pageable pageable = PageRequest.of(pageAppropriator(from, size), size,
                    Sort.by(Sort.Direction.DESC, "start"));
            LocalDateTime currentTime = LocalDateTime.now();
            switch (State.valueOf(param)) {
                case CURRENT:
                    return converter(bookingRepository.findBookingByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId,
                            currentTime, currentTime, pageable));
                case PAST:
                    return converter(bookingRepository.findBookingByItemOwnerIdAndEndIsBefore(userId,
                            currentTime, pageable));
                case FUTURE:
                    return converter(bookingRepository.findBookingByItemOwnerIdAndStartIsAfter(userId,
                            currentTime, pageable));
                case WAITING:
                    return converter(bookingRepository.findBookingByItemOwnerIdAndStatus(userId,
                            Status.WAITING, pageable));
                case REJECTED:
                    return converter(bookingRepository.findBookingByItemOwnerIdAndStatus(userId,
                            Status.REJECTED, pageable));
                default:
                    return converter(bookingRepository.findBookingByItemOwnerId(userId, pageable));
            }
        }
    }

    /**
     * Метод сортировки и конвертирования списка бронирований в DTO-объекты.
     * @param page итоговая страница бронирований.
     * @return возвращает отсортированный и конвертированный список.
     */
    private List<BookingDto> converter(Page<Booking> page) {
        return page.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}