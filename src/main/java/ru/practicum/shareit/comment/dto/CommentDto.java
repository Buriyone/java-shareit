package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO-класс комментария, для {@link Comment}.
 */
@Data
@Builder(toBuilder = true)
public class CommentDto {
    private int id;
    @NotBlank(message = "Текст комментария не может отсутствовать или быть пустым.", groups = {Create.class})
    private String text;
    private Item item;
    private User author;
    private LocalDateTime created;
}
