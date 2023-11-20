package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

import static ru.practicum.shareit.validation.Validator.*;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> add(BookingDto bookingDto, int userId) {
        idValidator(userId);
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> statusAppropriator(int bookingId, boolean approved, long userId) {
        idValidator(bookingId);
        idValidator(userId);
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved=" + approved, userId, parameters);
    }

    public ResponseEntity<Object> get(int bookingId, int userId) {
        idValidator(bookingId);
        idValidator(userId);
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAll(String state, long userId, int from, int size) {
        idValidator(userId);
        pageValidator(from, size);
        stateValidator(state);
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get("?state=" + state + "&from=" + from + "&size=" + size, userId, parameters);
    }

    public ResponseEntity<Object> getByUser(String state, long userId, int from, int size) {
        idValidator(userId);
        pageValidator(from, size);
        stateValidator(state);
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get("/owner?state=" + state + "&from=" + from + "&size=" + size, userId, parameters);
    }
}
