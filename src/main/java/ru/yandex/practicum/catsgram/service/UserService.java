package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

	private final HashMap<Long, User> users = new HashMap<>();

	public Collection<User> findAll() {
		return users.values();
	}

	public User findById(long userId) {
		return Optional.ofNullable(users.get(userId))
				.orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userId)));
	}

	public User create(User user) {
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			throw new ConditionsNotMetException("Имейл должен быть указан");
		}
		if (users.values().stream()
				.map(User::getEmail)
				.anyMatch(email -> Objects.equals(email, user.getEmail()))) {
			throw new DuplicatedDataException("Этот имейл уже используется");
		}
		user.setId(getNextId());
		user.setRegistrationDate(Instant.now());
		users.put(user.getId(), user);
		return user;
	}

	public User update(User newUser) {
		if (newUser.getId() == null) {
			throw new ConditionsNotMetException("Id должен быть указан");
		}
		if (users.containsKey(newUser.getId())) {
			User oldUser = users.get(newUser.getId());
			if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
				if (!Objects.equals(oldUser.getEmail(), newUser.getEmail()) &&
						users.values().stream()
								.map(User::getEmail)
								.anyMatch(email -> Objects.equals(email, newUser.getEmail()))) {
					throw new DuplicatedDataException("Этот имейл уже используется");
				}
				oldUser.setEmail(newUser.getEmail());
			}
			if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
				oldUser.setPassword(newUser.getPassword());
			}
			return oldUser;
		}
		throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
	}

	public Optional<User> findUserById(long id) {
		return Optional.ofNullable(users.get(id));
	}

	private long getNextId() {
		long currentMaxId = users.values()
				.stream()
				.mapToLong(User::getId)
				.max()
				.orElse(0);
		return ++currentMaxId;
	}
}
