package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

/**
 * Хранилище для {@link Comment}.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItemId(int itemId);
}
