package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    UserStorage storage;
    UserService service;

    @Autowired
    public UserController(UserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Выполнен запрос getAllUsers.");
        return storage.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = storage.getUser(id);
        if (user == null)
            throw new UserNotFoundException();
        log.info("Выполнен запрос getUser.");
        return user;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (user.getId() != null)
            throw new UserValidationException("id");

        User userForSave = storage.create(user);
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + storage.findAll().size());
        return userForSave;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == null)
            throw new UserValidationException("id");

        if (storage.getUser(user.getId()) == null)
            throw new UserNotFoundException();

        User userForSave = storage.update(user);
        log.info("Выполнен запрос updateUser.");
        return userForSave;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (storage.getUser(id) == null || storage.getUser(friendId) == null)
            throw new UserNotFoundException();

        if (service.getAllFriends(id).contains(friendId))
            throw new AlreadyFriendsException();

        service.addFriend(id, friendId);
        log.info("Выполнен запрос addFriend.");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (storage.getUser(id) == null || storage.getUser(friendId) == null)
            throw new UserNotFoundException();

        if (!service.getAllFriends(id).contains(friendId))
            throw new FriendNotFoundException();

        service.removeFriend(id, friendId);
        log.info("Выполнен запрос removeFriend.");
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        if (storage.getUser(id) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос getFriends.");
        return service.getAllFriends(id).stream()
                .map(storage::getUser)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (storage.getUser(id) == null || storage.getUser(otherId) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос getCommonFriends.");
        return service.getCommonFriends(id, otherId);
    }
}