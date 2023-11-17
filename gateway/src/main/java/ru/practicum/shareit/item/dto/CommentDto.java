package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class CommentDto {
    private int id;
    @NotBlank(message = "Текст комментария не может отсутствовать или быть пустым.", groups = {Create.class})
    private String text;
}