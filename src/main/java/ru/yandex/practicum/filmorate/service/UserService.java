package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    UserStorage storage;
    Map<Long, List<Long>> friendsMap;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public boolean addFriend(Long userId, Long friendId) {
        friendsMap.put(userId, friendsMap.getOrDefault(userId, Collections.emptyList()));
        friendsMap.put(friendId, friendsMap.getOrDefault(friendId, Collections.emptyList()));
        friendsMap.get(userId).add(friendId);
        friendsMap.get(friendId).add(userId);
        return true;
    }

    public boolean removeFriend(Long userId, Long friendId) {
        friendsMap.get(userId).remove(friendId);
        friendsMap.get(friendId).remove(userId);
        return true;
    }

    public List<Long> showAllFriends(Long userId) {
        return friendsMap.get(userId);
    }

    /*
    валидация
     */
}
