package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    Map<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws UserValidationException {
        if (!userValidation(user)) {
            log.info("Ошибка создания: некорректные данные о пользователе.");
            throw new UserValidationException("Ошибка: проверьте корректность данных о пользователе.");
        }
        users.put(user.getId(), setNameIfNameIsBlank(user));
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + users.size());
        return setNameIfNameIsBlank(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws UserValidationException {
        if (!userValidation(user)) {
            log.info("Ошибка обновления: некорректные данные о пользователе.");
            throw new UserValidationException("Ошибка: проверьте корректность данных о пользователе.");
        }

        users.put(user.getId(), setNameIfNameIsBlank(user));
        log.info("Выполнен запрос updateUser.");
        return setNameIfNameIsBlank(user);
    }


    /**
     * Проверяет объект User на соответствие критериям:
     *  электронная почта не может быть пустой и должна содержать символ @;
     *  логин не может быть пустым и содержать пробелы;
     *  имя для отображения может быть пустым — в таком случае будет использован логин;
     *  дата рождения не может быть в будущем.
     * @param user проверяемый объект.
     * @return результат валидации.
     */
    private boolean userValidation(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")
            || user.getLogin().isBlank() || user.getLogin().contains(" ")
            || user.getName() == null
            || user.getBirthday().isAfter(LocalDate.now())) {
            return false;
        }
        return true;
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
}
/*
электронная почта не может быть пустой и должна содержать символ @;
логин не может быть пустым и содержать пробелы;
имя для отображения может быть пустым — в таком случае будет использован логин;
дата рождения не может быть в будущем.
 */