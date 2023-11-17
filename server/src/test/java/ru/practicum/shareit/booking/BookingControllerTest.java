package ru.practicum.shareit.booking;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.assistant.Status;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    private final MockMvc mockMvc;
    private static final String MEDIA_TYPE = "application/json";
    private static final String USER_ID = "X-Sharer-User-Id";
    private final LocalDateTime start = LocalDateTime.now().plusHours(1).withNano(0);
    private final LocalDateTime end = LocalDateTime.now().plusHours(2).withNano(0);
    @MockBean
    private final BookingService bookingService;
    private final User user1 = User.builder()
            .id(1)
            .name("user1")
            .email("email1@yandex.ru")
            .build();
    private final Item item = Item.builder()
            .id(1)
            .name("name")
            .description("description")
            .available(true)
            .owner(user1)
            .build();
    private final User user2 = User.builder()
            .id(2)
            .name("user2")
            .email("email2@yandex.ru")
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1)
            .start(start)
            .end(end)
            .status(Status.WAITING)
            .bookerId(2)
            .booker(user2)
            .itemId(1)
            .item(item)
            .build();

    @Test
    public void addValidationTest() throws Exception {
        BookingDto invalidBookingDto = BookingDto.builder()
                .end(end)
                .itemId(1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start)
                .itemId(1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start.minusDays(1))
                .end(end)
                .itemId(1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start)
                .end(end.minusDays(1))
                .itemId(1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(-1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header("imposter", 1)
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        invalidBookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1)
                .build();
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, "imposter")
                        .content(new Gson().toJson(invalidBookingDto))
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTest() throws Exception {
        String testJson = "{\"itemId\":\"1\",\"start\":\"2077-11-11T21:11:11\",\"end\":\"2077-11-11T22:11:11\"}";
        when(bookingService.add(any(), anyInt())).thenReturn(bookingDto);
        this.mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1)
                        .content(testJson)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker").value(user2))
                .andExpect(jsonPath("$.item").value(item));
    }

    @Test
    public void statusAppropriatorValidationTest() throws Exception {
        this.mockMvc.perform(patch("/bookings/1?imposter=true")
                        .header(USER_ID, 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/bookings/1?approved=imposter")
                        .header(USER_ID, 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("imposter", 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(USER_ID, "imposter")
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void statusAppropriatorTest() throws Exception {
        when(bookingService.statusAppropriator(anyInt(), anyBoolean(), anyInt())).thenReturn(bookingDto);
        this.mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(USER_ID, 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker").value(user2))
                .andExpect(jsonPath("$.item").value(item));
    }

    @Test
    public void getValidationTest() throws Exception {
        this.mockMvc.perform(get("/bookings/1")
                        .header("imposter", 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings/1")
                        .header(USER_ID, "imposter")
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTest() throws Exception {
        when(bookingService.get(anyInt(), anyInt())).thenReturn(bookingDto);
        this.mockMvc.perform(get("/bookings/1")
                        .header(USER_ID, 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker").value(user2))
                .andExpect(jsonPath("$.item").value(item));

    }

    @Test
    public void getAllValidationTest() throws Exception {
        this.mockMvc.perform(get("/bookings")
                        .header("imposter", 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings")
                        .header(USER_ID, "imposter")
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllTest() throws Exception {
        List<BookingDto> testList = new ArrayList<>();
        testList.add(bookingDto);
        when(bookingService.getAll(anyString(), anyInt(), anyInt(), anyInt())).thenReturn(testList);
        this.mockMvc.perform(get("/bookings")
                        .header(USER_ID, 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].start").isNotEmpty())
                .andExpect(jsonPath("$.[0].end").isNotEmpty())
                .andExpect(jsonPath("$.[0].status").value("WAITING"))
                .andExpect(jsonPath("$.[0].booker").value(user2))
                .andExpect(jsonPath("$.[0].item").value(item));
    }

    @Test
    public void getByUserValidationTest() throws Exception {
        this.mockMvc.perform(get("/bookings/owner")
                        .header("imposter", 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, "imposter")
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByUserTest() throws Exception {
        List<BookingDto> testList = new ArrayList<>();
        testList.add(bookingDto);
        when(bookingService.getByUser(anyString(), anyInt(), anyInt(), anyInt())).thenReturn(testList);
        this.mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, 1)
                        .contentType(MEDIA_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].start").isNotEmpty())
                .andExpect(jsonPath("$.[0].end").isNotEmpty())
                .andExpect(jsonPath("$.[0].status").value("WAITING"))
                .andExpect(jsonPath("$.[0].booker").value(user2))
                .andExpect(jsonPath("$.[0].item").value(item));
    }
}
