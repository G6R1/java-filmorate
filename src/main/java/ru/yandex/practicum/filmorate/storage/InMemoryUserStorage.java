package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

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
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUser(Long id) {
        return userMap.get(id);
    }

    @Override
    public User create(User user) {
        User userForSave = setId(user);
        userMap.put(userForSave.getId(), userForSave);
        return userForSave;
    }

    @Override
    public User update(User user) {
        userMap.put(user.getId(), user);
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
