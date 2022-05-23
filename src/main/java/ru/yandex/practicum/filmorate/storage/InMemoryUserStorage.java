package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Long idCounter = 1L;
    private final Map<Long, User> userMap;

    public InMemoryUserStorage() {
        userMap = new HashMap<>();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUser(Long id) {
        return userMap.get(id);
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        User userForSave = setNameIfNameIsBlank(setId(user));
        userMap.put(userForSave.getId(), userForSave);
        return userForSave;
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        User userForSave = setNameIfNameIsBlank(user);
        userMap.put(user.getId(), userForSave);
        return userForSave;
    }

    /**
     * Если имя имя для отображения User пустое, будет использован логин.
     *
     * @param user объект для проверки.
     * @return Объект User с не пустым именем.
     */
    private User setNameIfNameIsBlank(User user) {
        if (user.getName().isBlank()) {
            return new User(user.getId(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getLogin(),
                    user.getBirthday());
        }
        return user;
    }

    /**
     * Задает id новому пользователю.
     *
     * @param user User с id == null
     * @return User с заданным id.
     */
    private User setId(User user) {
        return new User(idCounter++, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }
}
