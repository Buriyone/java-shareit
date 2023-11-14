package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Хранилище для {@link Item}.
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findItemByOwnerId(int id, Pageable pageable);

    @Query("select i from Item i " +
            "where upper(i.name) like upper(concat('%', :text, '%')) " +
            "or upper(i.description) like upper(concat('%', :text, '%'))")
    Page<Item> search(@Param("text") String text, Pageable pageable);

    List<Item> findByRequestId(int requestId);
}
