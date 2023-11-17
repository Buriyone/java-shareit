package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {
    private final TestEntityManager entityManager;
    private final CommentRepository commentRepository;
    private final LocalDateTime time = LocalDateTime.now();
    private final User user1 = User.builder()
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final Item item = Item.builder()
            .name("item")
            .description("description")
            .available(true)
            .build();
    private final User user2 = User.builder()
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final Comment comment = Comment.builder()
            .text("comment")
            .created(time)
            .build();

    @BeforeEach
    public void saver() {
        entityManager.persist(user1);
        item.setOwner(user1);
        entityManager.persist(item);
        entityManager.persist(user2);
        comment.setAuthor(user2);
        comment.setItem(item);
        entityManager.persist(comment);
    }

    @Test
    public void findByItemIdTest() {
        List<Comment> testList = commentRepository.findByItemId(item.getId());
        assertThat(testList.get(0)).isEqualTo(comment);
    }

    @AfterEach
    public void cleaner() {
        entityManager.clear();
    }
}
