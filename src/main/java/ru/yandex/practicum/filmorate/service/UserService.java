package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage storage;
    Map<Long, List<Long>> friendsMap;

    @Autowired
    public UserService(UserStorage storage) {
        friendsMap = new HashMap<>();
        this.storage = storage;
    }

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
