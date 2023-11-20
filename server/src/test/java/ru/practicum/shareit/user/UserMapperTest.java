package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserMapperTest {
    private final User user = User.builder().name("Bruce").email("willis@yandex.ru").build();
    private final UserDto userDto = UserDto.builder().name("sylvester").email("stallone@yandex.ru").build();
    private final UserMapper userMapper;

    @Test
    public void toUserTest() {
        User testUser = userMapper.toUser(userDto);
        assertEquals(testUser.getName(), userDto.getName(), "Имя отличается.");
        assertEquals(testUser.getEmail(), userDto.getEmail(), "Электронная почта отличается.");
    }

    @Test
    public void toUserDtoTest() {
        UserDto testUserDto = userMapper.toUserDto(user);
        assertEquals(testUserDto.getName(), user.getName(), "Имя отличается.");
        assertEquals(testUserDto.getEmail(), user.getEmail(), "Электронная почта отличается.");
    }
}
