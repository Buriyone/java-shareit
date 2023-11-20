package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Класс, отвечающий за запрос вещи.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
@Builder(toBuilder = true)
public class ItemRequest {
    /**
     * Уникальный идентификатор запроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Текст запроса, содержащий описание требуемой вещи.
     */
    @NotBlank(message = "текст запроса не может отсутствовать.")
    private String description;
    /**
     * Пользователь, создавший запрос.
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;
    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created_time")
    private LocalDateTime created;
}
