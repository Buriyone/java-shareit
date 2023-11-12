package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {
    private final UserRepository userRepository;
    private final TestEntityManager entityManager;
    private final User user = User.builder().name("John").email("malkovich@yandex.ru").build();

    @Test
    public void findByEmailContainingTest() {
        this.entityManager.persist(user);
        User testUser;
        if (this.userRepository.findByEmailContainingIgnoreCase(user.getEmail()).isPresent()) {
            testUser = this.userRepository.findByEmailContainingIgnoreCase(user.getEmail()).get();
            assertThat(testUser).isEqualTo(user);
            assertThat(testUser.getName()).isEqualTo("John");
            assertThat(testUser.getEmail()).isEqualTo("malkovich@yandex.ru");
        }
    }
}
