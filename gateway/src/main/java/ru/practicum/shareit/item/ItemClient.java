package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

import static ru.practicum.shareit.validation.Validator.idValidator;
import static ru.practicum.shareit.validation.Validator.pageValidator;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> add(ItemDto itemDto, int userId) {
        idValidator(userId);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(ItemDto itemDto, int itemId, int userId) {
        idValidator(itemId);
        idValidator(userId);
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getById(int itemId, int userId) {
        idValidator(itemId);
        idValidator(userId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> search(String text, long userId, int from, int size) {
        idValidator(userId);
        pageValidator(from, size);
        Map<String, Object> parameters = Map.of("text", text, "from", from, "size", size);
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAll(long userId, int from, int size) {
        idValidator(userId);
        pageValidator(from, size);
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        return get("/?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(int itemId, int userId, CommentDto commentDto) {
        idValidator(itemId);
        idValidator(userId);
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
