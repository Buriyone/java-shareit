package ru.practicum.shareit.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;

/**
 * Маппер для работы с  {@link Comment}, {@link CommentDto}, {@link CommentDtoIncreasedConfidential}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDtoIncreasedConfidential toCommentDtoIncreasedConfidential(Comment comment);
}
