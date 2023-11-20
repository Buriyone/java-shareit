package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.user.model.User;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.shareit.request.model.ItemRequest;

/**
 * DTO-объект для {@link ItemRequest}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {
    private int id;
    @NotBlank(message = "текст запроса не может отсутствовать.")
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;
}
