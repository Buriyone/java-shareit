package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentMapperTest {
    private final CommentMapper commentMapper;
    private final LocalDateTime time = LocalDateTime.now();
    private final User user = User.builder()
            .id(1)
            .name("user")
            .email("email@yandex.ru")
            .build();
    private final Item item = Item.builder()
            .id(1)
            .name("name")
            .description("description")
            .available(true)
            .owner(user)
            .build();
    private final Comment comment = Comment.builder()
            .id(1)
            .text("comment")
            .author(user)
            .item(item)
            .created(time)
            .build();
    private final CommentDto commentDto = CommentDto.builder()
            .id(1)
            .text("comment")
            .author(user)
            .item(item)
            .created(time)
            .build();

    @Test
    public void toCommentTest() {
        Comment testComment = commentMapper.toComment(commentDto);
        assertEquals(commentDto.getId(), testComment.getId(),
                "Id отличается.");
        assertEquals(commentDto.getText(), testComment.getText(),
                "Текст комментария отличается.");
        assertEquals(user, testComment.getAuthor(),
                "Автор отличается.");
        assertEquals(item, testComment.getItem(),
                "Вещь отличается отличается.");
        assertEquals(time, testComment.getCreated(),
                "Время создания отличается.");
    }

    @Test
    public void toCommentDtoIncreasedConfidentialTest() {
        CommentDtoIncreasedConfidential testComment = commentMapper.toCommentDtoIncreasedConfidential(comment);
        assertEquals(commentDto.getId(), testComment.getId(),
                "Id отличается.");
        assertEquals(commentDto.getText(), testComment.getText(),
                "Текст комментария отличается.");
        assertEquals(user.getName(), testComment.getAuthorName(),
                "Автор отличается.");
        assertEquals(time, testComment.getCreated(),
                "Время создания отличается.");
    }
}
