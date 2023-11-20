package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {
    private final TestEntityManager entityManager;
    private final ItemRepository itemRepository;
    private final User user1 = User.builder()
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .description("description")
            .created(LocalDateTime.now())
            .build();
    private final User user2 = User.builder()
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final Item item = Item.builder()
            .name("item")
            .description("description")
            .available(true)
            .build();

    @BeforeEach
    public void saver() {
        entityManager.persist(user1);
        itemRequest.setRequestor(user1);
        entityManager.persist(itemRequest);
        entityManager.persist(user2);
        item.setOwner(user2);
        item.setRequest(itemRequest);
        entityManager.persist(item);
    }

    @Test
    public void findItemByOwnerIdTest() {
        List<Item> testList = this.itemRepository.findItemByOwnerId(user2.getId(),
                PageRequest.of(0, 20));
        assertThat(testList.get(0)).isEqualTo(item);
        assertEquals(item, testList.get(0), "Вещь отличается");
    }

    @Test
    public void searchTest() {
        List<Item> testList = this.itemRepository.search("deSCrIpTioN",
                PageRequest.of(0, 20)).toList();
        assertEquals(item, testList.get(0), "Вещь отличается");
    }

    @Test
    public void findByRequestIdTest() {
        List<Item> testList = this.itemRepository.findByRequestId(itemRequest.getId());
        assertEquals(item, testList.get(0), "Вещь отличается");
    }

    @AfterEach
    public void cleaner() {
        entityManager.clear();
    }
}
