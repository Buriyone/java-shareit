package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;

/**
 * Маппер для работы с  {@link Comment}, {@link CommentDto}, {@link CommentDtoIncreasedConfidential}.
 */
public class CommentMapper {
    /**
     * Конвертирует {@link CommentDto} в {@link Comment}.
     */
    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .author(commentDto.getAuthor())
                .item(commentDto.getItem())
                .created(commentDto.getCreated())
                .build();
    }

    /**
     * Конвертирует {@link Comment} в {@link CommentDto}.
     */
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor())
                .item(comment.getItem())
                .created(comment.getCreated())
                .build();
    }

    /**
     * Конвертирует {@link Comment} в {@link CommentDtoIncreasedConfidential}.
     */
    public static CommentDtoIncreasedConfidential toCommentDtoIncreasedConfidential(Comment comment) {
        return CommentDtoIncreasedConfidential.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
