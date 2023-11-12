package ru.practicum.shareit.user;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final MockMvc mockMvc;
    private static final String MEDIA_TYPE = "application/json";
    @MockBean
    private final UserService userService;

    @Test
    public void addValidationTest() throws Exception {
        UserDto invalidUser = UserDto.builder().email("email@yandex.ru").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidUser = UserDto.builder().name("").email("email@yandex.ru").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidUser = UserDto.builder().name(" ").email("email@yandex.ru").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        StringBuilder bigName = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            bigName.append(i);
        }
        invalidUser = UserDto.builder().name(bigName.toString()).email("email@yandex.ru").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidUser = UserDto.builder().name("name").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidUser = UserDto.builder().name("name").email("").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidUser = UserDto.builder().name("name").email(" ").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidUser = UserDto.builder().name("name").email("emailYandex@.ru").build();
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(invalidUser))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTest() throws Exception {
        when(userService.add(any()))
                .thenReturn(UserDto.builder().id(1).name("name").email("email@yandex.ru").build());
        this.mockMvc.perform(post("/users")
                        .content(new Gson().toJson(UserDto.builder().name("name").email("email@yandex.ru").build()))
                        .contentType(MEDIA_TYPE)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("email@yandex.ru"));
    }

    @Test
    public void updateValidationTest() throws Exception {
        this.mockMvc.perform(patch("/users/{userId}", 1)
                        .content(new Gson().toJson(UserDto.builder().name("name").email("emailYandex@.ru").build()))
                        .contentType(MEDIA_TYPE)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/users/{userId}", "imposter")
                        .content(new Gson().toJson(UserDto.builder().name("name").email("email@Yandex.ru").build()))
                        .contentType(MEDIA_TYPE)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTest() throws Exception {
        when(userService.update(any(), anyInt()))
                .thenReturn(UserDto.builder().id(1).name("newName").email("newEmail@yandex.ru").build());
        this.mockMvc.perform(patch("/users/{userId}", 1)
                        .content(new Gson().toJson(UserDto.builder().name("newName").email("newEmail@yandex.ru")
                                .build()))
                        .contentType(MEDIA_TYPE)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("newName"))
                .andExpect(jsonPath("$.email").value("newEmail@yandex.ru"));
    }

    @Test
    public void deleteValidationTest() throws Exception {
        this.mockMvc.perform(delete("/users/{userId}", "imposter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTest() throws Exception {
        this.mockMvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getValidationTest() throws Exception {
        this.mockMvc.perform(get("/users/{userId}", "imposter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTest() throws Exception {
        when(userService.get(anyInt()))
                .thenReturn(UserDto.builder().id(1).name("name").email("email@yandex.ru").build());
        this.mockMvc.perform(get("/users/{userId}", 1)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("email@yandex.ru"));
    }
}
