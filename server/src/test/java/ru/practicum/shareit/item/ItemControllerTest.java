package ru.practicum.shareit.item;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoIncreasedConfidential;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIncreasedConfidential;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final MockMvc mockMvc;
    private static final String MEDIA_TYPE = "application/json";
    private static final String USER_ID = "X-Sharer-User-Id";
    @MockBean
    private final ItemService itemService;

    private final LocalDateTime time = LocalDateTime.now();
    private final User user1 = User.builder()
            .id(1)
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final User user2 = User.builder()
            .id(2)
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1)
            .description("requestDescription")
            .requestor(user2)
            .created(time)
            .build();
    private final ItemDtoIncreasedConfidential itemDtoIncreasedConfidential = ItemDtoIncreasedConfidential.builder()
            .id(1)
            .name("name")
            .description("description")
            .available(true)
            .owner(user1)
            .requestId(itemRequest.getId())
            .build();

    @Test
    public void addValidationTest() throws Exception {
        ItemDto invalidItemDto = ItemDto.builder()
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("")
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name(" ")
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("name")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("name")
                .description("")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("name")
                .description(" ")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header("imposter", 1)
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidItemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, "imposter")
                        .content(new Gson().toJson(invalidItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTest() throws Exception {
        when(itemService.add(any(), anyInt()))
                .thenReturn(itemDtoIncreasedConfidential);
        this.mockMvc.perform(post("/items")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(ItemDto.builder()
                                .name("name")
                                .description("description")
                                .available(true)
                                .build()))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner").value(user1))
                .andExpect(jsonPath("$.requestId").value(1));
    }

    @Test
    public void updateValidationTest() throws Exception {
        ItemDto testItemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(patch("/items/imposter")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(testItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/items/1")
                        .header("imposter", 1)
                        .content(new Gson().toJson(testItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/items/1")
                        .header(USER_ID, "imposter")
                        .content(new Gson().toJson(testItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTest() throws Exception {
        when(itemService.update(any(), anyInt(), anyInt()))
                .thenReturn(itemDtoIncreasedConfidential);
        this.mockMvc.perform(patch("/items/1")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(ItemDto.builder()
                                .name("name")
                                .description("description")
                                .available(true)
                                .build()))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner").value(user1))
                .andExpect(jsonPath("$.requestId").value(1));
    }

    @Test
    public void getValidationTest() throws Exception {
        ItemDto testItemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        this.mockMvc.perform(get("/items/imposter")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(testItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/1")
                        .header("imposter", 1)
                        .content(new Gson().toJson(testItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/1")
                        .header(USER_ID, "imposter")
                        .content(new Gson().toJson(testItemDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTest() throws Exception {
        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(itemDtoIncreasedConfidential);
        this.mockMvc.perform(get("/items/1")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(ItemDto.builder()
                                .name("name")
                                .description("description")
                                .available(true)
                                .build()))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.owner").value(user1))
                .andExpect(jsonPath("$.requestId").value(1));
    }

    @Test
    public void searchValidationTest() throws Exception {
        this.mockMvc.perform(get("/items/search?text=name")
                        .header("imposter", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/search?text=name")
                        .header(USER_ID, "imposter"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/search?imposter=name")
                        .header(USER_ID, 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void searchTest() throws Exception {
        List<ItemDtoIncreasedConfidential> testList = new ArrayList<>();
        testList.add(itemDtoIncreasedConfidential);
        when(itemService.search(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(testList);
        this.mockMvc.perform(get("/items/search?text=name")
                        .header(USER_ID, 1)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[0].description").value("description"))
                .andExpect(jsonPath("$.[0].available").value(true))
                .andExpect(jsonPath("$.[0].owner").value(user1))
                .andExpect(jsonPath("$.[0].requestId").value(1));
    }

    @Test
    public void getAllValidationTest() throws Exception {
        this.mockMvc.perform(get("/items")
                        .header("imposter", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items")
                        .header(USER_ID, "imposter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllTest() throws Exception {
        List<ItemDtoIncreasedConfidential> testList = new ArrayList<>();
        testList.add(itemDtoIncreasedConfidential);
        when(itemService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(testList);
        this.mockMvc.perform(get("/items")
                        .header(USER_ID, 1)
                        .accept(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[0].description").value("description"))
                .andExpect(jsonPath("$.[0].available").value(true))
                .andExpect(jsonPath("$.[0].owner").value(user1))
                .andExpect(jsonPath("$.[0].requestId").value(1));
    }

    @Test
    public void addCommentValidationTest() throws Exception {
        CommentDto invalidCommentDto = CommentDto.builder().build();
        this.mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidCommentDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidCommentDto = CommentDto.builder()
                .text("")
                .build();
        this.mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidCommentDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidCommentDto = CommentDto.builder()
                .text(" ")
                .build();
        this.mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidCommentDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        CommentDto validCommentDto = CommentDto.builder()
                .text("text")
                .build();
        this.mockMvc.perform(post("/items/imposter/comment")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(validCommentDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/items/1/comment")
                        .header("imposter", 1)
                        .content(new Gson().toJson(validCommentDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID, "imposter")
                        .content(new Gson().toJson(validCommentDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCommentTest() throws Exception {
        when(itemService.addComment(anyInt(), anyInt(), any()))
                .thenReturn(CommentDtoIncreasedConfidential.builder()
                        .id(1)
                        .authorName(user2.getName())
                        .text("comment")
                        .created(time)
                        .build());
        this.mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(CommentDto.builder()
                                .text("comment")
                                .build()))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.authorName").value(user2.getName()))
                .andExpect(jsonPath("$.text").value("comment"))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }
}
