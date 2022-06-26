package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Выполнен запрос getAllUsers.");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        log.info("Выполнен запрос getUser.");
        return user;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        User userForSave = userService.create(user);
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + userService.getAllUsers().size());
        return userForSave;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        User userForSave = userService.update(user);
        log.info("Выполнен запрос updateUser.");
        return userForSave;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Выполнен запрос addFriend.");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        log.info("Выполнен запрос removeFriend.");
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        List<User> list = userService.getFriends(id);
        log.info("Выполнен запрос getFriends.");
        return list;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> list = userService.getCommonFriends(id, otherId);
        log.info("Выполнен запрос getCommonFriends.");
        return list;
    }
}