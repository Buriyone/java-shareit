package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.comment.model.Comment;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO-класс для {@link Comment} конфиденциальный.
 */
@Data
@Builder(toBuilder = true)
public class CommentDtoIncreasedConfidential {
    private int id;
    @NotBlank(message = "Текст комментария не может отсутствовать или быть пустым.")
    private String text;
    private String authorName;
    private LocalDateTime created;
}
