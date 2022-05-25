package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;
    private final Map<Long, List<Long>> friendsMap;

    @Autowired
    public UserService(UserStorage storage) {
        friendsMap = new HashMap<>();
        this.storage = storage;
    }

    //работа с сохранинем/обновление пользователей

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUser(Long id) {
        return storage.getUser(id);
    }

    public User create(User user) {
        return storage.create(setNameIfNameIsBlank(user));
    }

    public User update(User user) {
        return storage.update(setNameIfNameIsBlank(user));
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

    //работа c дружескими связями между пользователями

    public boolean addFriend(Long userId, Long friendId) {
        initiateCheck(userId);
        initiateCheck(friendId);
        friendsMap.get(userId).add(friendId);
        friendsMap.get(friendId).add(userId);
        return true;
    }

    public List<User> getFriends(Long userId) {
        initiateCheck(userId);
        return friendsMap.get(userId).stream().map(storage::getUser).collect(Collectors.toList());
    }

    public boolean removeFriend(Long userId, Long friendId) {
        initiateCheck(userId);
        initiateCheck(friendId);
        friendsMap.get(userId).remove(friendId);
        friendsMap.get(friendId).remove(userId);
        return true;
    }

    public List<Long> getAllFriends(Long userId) {
        initiateCheck(userId);
        return friendsMap.get(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        initiateCheck(userId);
        initiateCheck(otherId);
        return friendsMap.get(userId).stream()
                .filter((x) -> friendsMap.get(otherId).contains(x))
                .map(storage::getUser)
                .collect(Collectors.toList());
    }

    private void initiateCheck(Long id) {
        if (!friendsMap.containsKey(id))
            friendsMap.put(id, new ArrayList<>());
    }
}
