package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTest {
    private final TestEntityManager entityManager;
    private final ItemRequestRepository itemRequestRepository;
    private final User user = User.builder().name("user").email("email@yandex.ru").build();
    private final ItemRequest itemRequest = ItemRequest.builder().description("description")
            .created(LocalDateTime.now()).build();
    private final User otherUser = User.builder().name("otherUser").email("otheremail@yandex.ru").build();
    private final ItemRequest otherItemRequest = ItemRequest.builder().description("otherDescription")
            .created(LocalDateTime.now()).build();

    @BeforeEach
    public void cleaner() {
        entityManager.clear();
    }

    @Test
    public void findByRequestorIdTest() {
        this.entityManager.persist(user);
        itemRequest.setRequestor(user);
        this.entityManager.persist(itemRequest);
        List<ItemRequest> testList = this.itemRequestRepository
                .findByRequestorId(user.getId(), Sort.by(Sort.Direction.DESC, "created"));
        assertThat(testList.get(0)).isEqualTo(itemRequest);
    }

    @Test
    public void findAllByRequestorIdNotTest() {
        this.entityManager.persist(user);
        itemRequest.setRequestor(user);
        this.entityManager.persist(itemRequest);
        this.entityManager.persist(otherUser);
        otherItemRequest.setRequestor(otherUser);
        this.entityManager.persist(otherItemRequest);
        Page<ItemRequest> page = itemRequestRepository
                .findAllByRequestorIdNot(user.getId(), PageRequest.of(0, 20,
                        Sort.by(Sort.Direction.DESC, "created")));
        ItemRequest testItemRequest = page.toList().get(0);
        assertThat(testItemRequest).isEqualTo(otherItemRequest);
    }
}
