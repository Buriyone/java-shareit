package ru.practicum.shareit.request;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final MockMvc mockMvc;
    private final String MEDIA_TYPE = "application/json";
    private final String USER_ID = "X-Sharer-User-Id";
    private final User user = User.builder()
            .id(1)
            .name("user")
            .email("email@yandex.ru")
            .build();
    private final LocalDateTime time = LocalDateTime.now();
    @MockBean
    private final ItemRequestService itemRequestService;

    @Test
    public void addValidationTest() throws Exception {
        ItemRequestDto invalidItemRequestDto = ItemRequestDto.builder().build();
        this.mockMvc.perform(post("/requests")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemRequestDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemRequestDto = ItemRequestDto.builder().description("").build();
        this.mockMvc.perform(post("/requests")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemRequestDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemRequestDto = ItemRequestDto.builder().description(" ").build();
        this.mockMvc.perform(post("/requests")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemRequestDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/requests")
                        .header(USER_ID, "imposter")
                        .content(new Gson().toJson(invalidItemRequestDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/requests")
                        .header("imposter", 1)
                        .content(new Gson().toJson(invalidItemRequestDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTest() throws Exception {
        when(itemRequestService.add(any(), anyInt()))
                .thenReturn(ItemRequestDto.builder().id(1).description("description")
                        .created(time).requestor(user).build());
        this.mockMvc.perform(post("/requests")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(ItemRequestDto.builder().description("description").build()))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.requestor").value(user));
    }

    @Test
    public void getValidationTest() throws Exception {
        this.mockMvc.perform(get("/requests")
                        .header("imposter", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests")
                        .header(USER_ID, "imposter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTest() throws Exception {
        List<ItemRequestDto> testList = new ArrayList<>();
        testList.add(ItemRequestDto.builder().id(1).description("description")
                .created(time).requestor(user).build());
        when(itemRequestService.get(anyInt()))
                .thenReturn(testList);
        this.mockMvc.perform(get("/requests")
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].description").value("description"))
                .andExpect(jsonPath("$.[0].created").isNotEmpty())
                .andExpect(jsonPath("$.[0].requestor").value(user));
    }

    @Test
    public void getAllValidationTest() throws Exception {
        this.mockMvc.perform(get("/requests/all")
                        .header("imposter", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/all")
                        .header(USER_ID, "imposter"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/all")
                        .header(USER_ID, 1)
                        .param("from", "imposter"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/all")
                        .header(USER_ID, 1)
                        .param("size", "imposter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllTest() throws Exception {
        List<ItemRequestDto> testList = new ArrayList<>();
        testList.add(ItemRequestDto.builder().id(1).description("description")
                .created(time).requestor(user).build());
        when(itemRequestService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(testList);
        this.mockMvc.perform(get("/requests/all")
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].description").value("description"))
                .andExpect(jsonPath("$.[0].created").isNotEmpty())
                .andExpect(jsonPath("$.[0].requestor").value(user));
    }

    @Test
    public void getByIdValidationTest() throws Exception {
        this.mockMvc.perform(get("/requests/1")
                        .header("imposter", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/1")
                        .header(USER_ID, "imposter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByIdTest() throws Exception {
        when(itemRequestService.getById(anyInt(), anyInt()))
                .thenReturn(ItemRequestDto.builder().id(1).description("description")
                        .created(time).requestor(user).build());
        this.mockMvc.perform(get("/requests/1")
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.requestor").value(user));
    }
}
