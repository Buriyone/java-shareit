package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс-хранилище для {@link Item} реализующий интерфейс {@link ItemRepository}.
 */
@Component
public class InMemoryItemRepositoryImpl implements ItemRepository {
    /**
     * Хранение вещей реализовано в хэш-таблице,
     * ключом является уникальный идентификатор, а значением вещь.
     */
    private final Map<Integer, Item> itemStorage = new HashMap<>();
    /**
     * Уникальный идентификатор для присвоения.
     */
    private int id = 1;

    /**
     * Метод регистрации и добавления в хранилище.
     * Операция (id++) - увеличивает значение уникального идентификатора в поле класса на одну единицу.
     * @param item принимает вещь в формате {@link Item} которую необходимо зарегистрировать и отправить на хранение.
     * @return возвращает вещь с присвоенным уникальным идентификатором.
     */
    @Override
    public Item add(Item item) {
        item.setId(id);
        itemStorage.put(id, item);
        id++;
        return item;
    }

    /**
     * Метод обновления данных вещи в хранилище.
     * Обновляются значения имени, описания, статуса доступности.
     * @param item вещь, данные которое требуется обновить.
     * @return возвращает вещь с обновленными данными.
     */
    @Override
    public Item update(Item item, int itemId) {
        return itemStorage.values().stream()
                .filter(item1 -> item1.getId() == itemId)
                .peek(item1 -> {
                    if (item.getName() != null) {
                        item1.setName(item.getName());
                    }
                    if (item.getDescription() != null) {
                        item1.setDescription(item.getDescription());
                    }
                    if (item.getAvailable() != null) {
                        item1.setAvailable(item.getAvailable());
                    }
                    itemStorage.put(itemId, item1);
                })
                .findFirst().orElse(null);
    }

    /**
     * Метод удаления всех вещей конкретного пользователя.
     * @param id уникальный идентификатор пользователя, вещи которого подлежат удалению.
     */
    @Override
    public void deleteAllItemByUserId(int id) {
        itemStorage.values().removeIf(item -> item.getOwner().getId() == id);
    }

    /**
     * Метод поиска и получения вещи по уникальному идентификатору.
     * @param id уникальный идентификатор.
     * @return возвращает найденную вещь.
     */
    @Override
    public Item getById(int id) {
        return itemStorage.get(id);
    }

    /**
     * Метод-поисковик. Принимает поисковый запрос и осуществляет поиск в хранилище.
     * @param text поисковый запрос.
     * @return возвращает список вещей которые отвечают поисковому запросу и доступны к аренде.
     */
    @Override
    public List<Item> search(String text) {
        return itemStorage.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        return true;
                    } else return item.getDescription().toLowerCase().contains(text.toLowerCase());
                }).collect(Collectors.toList());
    }

    /**
     * Метод ищет и предоставляет все вещи пользователя.
     * @param userId уникальный идентификатор пользователя.
     * @return возвращает список вещей.
     */
    @Override
    public List<Item> getAll(int userId) {
        return itemStorage.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Метод проверки наличия сущности в хранилище.
     * @param id идентификатор вещи.
     * @return возвращает значение (true) если результат поиска успешный,
     * в противном случае возвращает значение (false).
     */
    @Override
    public Boolean itemChecker(int id) {
        return itemStorage.containsKey(id);
    }

    /**
     * Метод проверки хозяина вещи.
     * @param itemId идентификатор вещи.
     * @param userId идентификатор потенциального хозяина.
     * @return возвращает значение (true) если результат поиска подтверждает собственность,
     * в противном случае возвращает значение (false).
     */
    @Override
    public Boolean itemByUserChecker(int itemId, int userId) {
        return itemStorage.get(itemId).getOwner().getId() == userId;
    }
}
