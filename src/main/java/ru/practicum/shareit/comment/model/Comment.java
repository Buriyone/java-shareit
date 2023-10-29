package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс комментария.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@Builder(toBuilder = true)
public class Comment {
    /**
     * Уникальный идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Текст комментария.
     */
    private String text;
    /**
     * Вещь которую пользователь брал в аренду.
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    /**
     * Автор комментария.
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    /**
     * Время создания комментария.
     */
    @Column(name = "created_time")
    private LocalDateTime created;
}
