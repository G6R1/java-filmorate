package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends AbstractController<Long, User>{

    private Long idCounter = 1L;

    public UserController() {
        super(new HashMap<>());
    }

    @Override
    @PostMapping()
    public User create(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() != null) {
            log.info("Ошибка создания: некорректные данные о пользователе.");
            throw new UserValidationException();
        }

        User userForSave = setNameIfNameIsBlank(setId(user));
        resourceStorage.put(userForSave.getId(), userForSave);
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + resourceStorage.size());
        return userForSave;
    }

    @Override
    @PutMapping()
    public User update(@Valid @RequestBody User user) throws UserValidationException {
        if (user.getId() == null) {
            log.info("Ошибка обновления: некорректные данные о пользователе.");
            throw new UserValidationException();
        }

        resourceStorage.put(user.getId(), setNameIfNameIsBlank(user));
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