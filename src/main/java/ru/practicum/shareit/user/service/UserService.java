package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс сервиса контроллера обработки запросов связанных с {@link User} и {@link UserDto}.
 */
public interface UserService {
	UserDto add(UserDto userDto);

	UserDto update(UserDto userDto, int id);

	void delete(int id);

	UserDto get(int id);

	List<UserDto> getAll();
}
