package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * Класс вещи.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
@Builder(toBuilder = true)
public class Item {
    /**
     * Уникальный идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Краткое название.
     */
    private String name;
    /**
     * Развернутое описание.
     */
    private String description;
    /**
     * Статус о том, доступна или нет вещь для аренды.
     */
    @Column(name = "is_available")
    private Boolean available;
    /**
     * Владелец вещи.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    /**
     * Если вещь была создана по запросу другого пользователя,
     * то в этом поле будет храниться ссылка на соответствующий запрос.
     */
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
