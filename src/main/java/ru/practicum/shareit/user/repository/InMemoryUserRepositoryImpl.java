package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * Класс-хранилище для {@link User} реализующий интерфейс {@link UserRepository}.
 */
@Component
public class InMemoryUserRepositoryImpl implements UserRepository {
    /**
     * Хранение пользователей реализовано в хэш-таблице,
     * ключом является уникальный идентификатор, а значением пользователь.
     */
    private final Map<Integer, User> userStorage = new HashMap<>();
    /**
     * Уникальный идентификатор для присвоения.
     */
    private int id = 1;

    /**
     * Метод регистрации и добавления в хранилище.
     * Операция (id++) - увеличивает значение уникального идентификатора в поле класса на одну единицу.
     * @param user принимает пользователя в формате {@link User}
     *             которого необходимо зарегистрировать и отправить на хранение.
     * @return возвращает пользователя в формате {@link User} с присвоенным уникальным идентификатором.
     */
    @Override
    public User add(User user) {
        user.setId(id);
        userStorage.put(id, user);
        id++;
        return user;
    }

    /**
     * Метод обновления данных пользователя.
     * Обновляет значения имени и электронной почты.
     * @param user пользователь, данные которого требуется обновить.
     * @return возвращает обновленного пользователя в формате {@link User}.
     */
    @Override
    public User update(User user) {
        return userStorage.values().stream()
                .filter(user1 -> user1.getId() == user.getId())
                .peek(user1 -> {
                    if (user.getName() != null) {
                        user1.setName(user.getName());
                    }
                    if (user.getEmail() != null) {
                        user1.setEmail(user.getEmail());
                    }
                    userStorage.put(user1.getId(), user1);
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Метод удаления пользователя.
     * @param id уникальный идентификатор пользователя, которого требуется удалить.
     */
    @Override
    public void delete(int id) {
        userStorage.remove(id);
    }

    /**
     * Метод поиска и получения пользователя по уникальному идентификатору.
     * @param id уникальный идентификатор пользователя.
     * @return возвращает пользователя в формате {@link User}.
     */
    @Override
    public User getById(int id) {
        return userStorage.get(id);
    }

    /**
     * Метод предоставления всех пользователей.
     * @return возвращает список всех пользователей.
     */
    @Override
    public List<User> getAll() {
        return new ArrayList<>(userStorage.values());
    }

    /**
     * Метод проверки наличия пользователя в системе.
     * @param id уникальный идентификатор пользователя.
     * @return возвращает значение (true) если результат поиска успешный,
     * в противном случае возвращает значение (false).
     */
    @Override
    public Boolean userChecker(int id) {
        return userStorage.containsKey(id);
    }

    /**
     * Метод проверки валидности электронной почты.
     * @param email проверяемая электронная почта.
     * @return возвращает true если электронная почта уже занята, если почта свободна возвращает false.
     */
    @Override
    public Boolean emailValidation(String email) {
        return userStorage.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    /**
     * Метод проверки доступности электронной почты для смены пользователем.
     * @param email проверяемая электронная почта.
     * @return возвращает true если электронная почта уже занята, если почта свободна возвращает false.
     */
    @Override
    public Boolean emailAvailability(String email, int id) {
        return userStorage.values().stream().anyMatch(user -> user.getEmail().equals(email) && user.getId() != id);
    }
}
