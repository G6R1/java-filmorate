package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @GetMapping()
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() != null) {
            log.info("Ошибка создания: некорректные данные о пользователе.");
            throw new UserValidationException();
        }

        User userForSave = setNameIfNameIsBlank(setId(user));
        users.put(userForSave.getId(), userForSave);
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + users.size());
        return userForSave;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() == null) {
            log.info("Ошибка обновления: некорректные данные о пользователе.");
            throw new UserValidationException();
        }

        users.put(user.getId(), setNameIfNameIsBlank(user));
        log.info("Выполнен запрос updateUser.");
        return setNameIfNameIsBlank(user);
    }

    /**
     * Если имя имя для отображения User пустое, будет использован логин.
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
     * @param user User с id == null
     * @return User с заданным id.
     */
    private User setId(User user) {
        return new User(idCounter++, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }
}
/*
электронная почта не может быть пустой и должна содержать символ @;
логин не может быть пустым и содержать пробелы;
имя для отображения может быть пустым — в таком случае будет использован логин;
дата рождения не может быть в будущем.
 */