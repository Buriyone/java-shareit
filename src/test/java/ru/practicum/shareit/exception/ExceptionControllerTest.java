package ru.practicum.shareit.exception;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;
import ru.practicum.shareit.user.dto.UserDto;
import javax.transaction.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ExceptionControllerTest {
    private final MockMvc mockMvc;
    private static final String MEDIA_TYPE = "application/json";
    private static final String USER_ID = "X-Sharer-User-Id";
    private final Gson gson = new Gson();

    @Test
    public void notFoundExceptionTest() throws Exception {
        this.mockMvc.perform(get("/users/999")
                        .accept(MEDIA_TYPE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.actionError")
                        .value("Ошибка поиска."))
                .andExpect(jsonPath("$.description")
                        .value("Пользователь с id: 999 не найден."));
    }

    @Test
    public void validationExceptionTest() throws Exception {
        this.mockMvc.perform(get("/items?from=-1")
                        .header(USER_ID, 1)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.actionError")
                        .value("Ошибка валидации."))
                .andExpect(jsonPath("$.description")
                        .value("Значение from не может быть отрицательным."));
    }

    @Test
    public void conflictExceptionTest() throws Exception {
        UserDto user = UserDto.builder()
                .name("user")
                .email("email@yandex.ru")
                .build();
        this.mockMvc.perform(post("/users")
                        .contentType(MEDIA_TYPE)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        this.mockMvc.perform(post("/users")
                        .contentType(MEDIA_TYPE)
                        .content(gson.toJson(user)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.actionError")
                        .value("Конфликт запроса."))
                .andExpect(jsonPath("$.description")
                        .value("Электронная почта уже занята."));
    }

    @Test
    public void forbiddenExceptionTest() throws Exception {
        UserDto user = gson.fromJson(this.mockMvc.perform(post("/users")
                        .contentType(MEDIA_TYPE)
                        .content(gson.toJson(UserDto.builder()
                                .name("user")
                                .email("email@yandex.ru")
                                .build())))
                .andReturn()
                .getResponse()
                .getContentAsString(), UserDto.class);
        ItemDtoIncreasedConfidential item = gson.fromJson(this.mockMvc.perform(post("/items")
                        .header(USER_ID, user.getId())
                        .contentType(MEDIA_TYPE)
                        .content(gson.toJson(ItemDto.builder()
                                .name("name")
                                .description("description")
                                .available(true)
                                .build())))
                .andReturn()
                .getResponse()
                .getContentAsString(), ItemDtoIncreasedConfidential.class);
        UserDto imposter = gson.fromJson(this.mockMvc.perform(post("/users")
                        .contentType(MEDIA_TYPE)
                        .content(gson.toJson(UserDto.builder()
                                .name("imposter")
                                .email("imposter@yandex.ru")
                                .build())))
                .andReturn()
                .getResponse()
                .getContentAsString(), UserDto.class);
        String message = String.format("Вещь c id: %d не принадлежит пользователю с id: %d.",
                item.getId(), imposter.getId());
        this.mockMvc.perform(patch("/items/" + item.getId())
                        .header(USER_ID, imposter.getId())
                        .contentType(MEDIA_TYPE)
                        .content(new Gson().toJson(ItemDto.builder()
                                .name("newName")
                                .description("newDescription")
                                .available(true)
                                .build())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.actionError")
                        .value("В доступе отказано."))
                .andExpect(jsonPath("$.description")
                        .value(message));
    }

    @Test
    public void stateExceptionTest() throws Exception {
        UserDto user = gson.fromJson(this.mockMvc.perform(post("/users")
                        .contentType(MEDIA_TYPE)
                        .content(gson.toJson(UserDto.builder()
                                .name("user")
                                .email("email@yandex.ru")
                                .build())))
                .andReturn()
                .getResponse()
                .getContentAsString(), UserDto.class);
        this.mockMvc.perform(get("/bookings?state=imposter")
                        .header(USER_ID, user.getId())
                        .accept(MEDIA_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Unknown state: UNSUPPORTED_STATUS"));
    }
}
