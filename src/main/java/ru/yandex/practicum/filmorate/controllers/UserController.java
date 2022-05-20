package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends AbstractController<User>{

    UserStorage storage;

    @Autowired
    public UserController(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    @GetMapping()
    public List<User> findAll() {
        return storage.findAll();
    }

    @Override
    @PostMapping()
    public User create(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() != null) {
            log.info("Ошибка создания: некорректные данные о пользователе.");
            throw new UserValidationException();
        }

        User userForSave = storage.create(user);
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + storage.findAll().size());
        return userForSave;
    }

    @Override
    @PutMapping()
    public User update(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() == null) {
            log.info("Ошибка обновления: некорректные данные о пользователе.");
            throw new UserValidationException();
        }

        User userForSave = storage.update(user);
        log.info("Выполнен запрос updateUser.");
        return userForSave;
    }
}