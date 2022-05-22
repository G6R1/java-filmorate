package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    FilmStorage storage;
    UserStorage userStorage;
    FilmService service;

    @Autowired
    public FilmController(FilmStorage storage, FilmService service, UserStorage userStorage) {
        this.storage = storage;
        this.service = service;
        this.userStorage = userStorage;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Выполнен запрос getAllFilms.");
        return storage.findAll();
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable Long id) {
        Film film = storage.getFilm(id);
        if (film == null)
            throw new FilmNotFoundException();
        log.info("Выполнен запрос getUser.");
        return film;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        service.filmValidation(film);

        if (film.getId() != null)
            throw new FilmValidationException("id");

        Film filmForSave = storage.create(film);
        log.info("Выполнен запрос createFilm. Текущее количество фильмов: " + storage.findAll().size());
        return filmForSave;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        service.filmValidation(film);

        if (film.getId() == null)
            throw new FilmValidationException("id");

        if (storage.getFilm(film.getId()) == null)
            throw new FilmNotFoundException();

        Film filmForSave = storage.update(film);
        log.info("Выполнен запрос updateFilm.");
        return filmForSave;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        if (storage.getFilm(id) == null)
            throw new FriendNotFoundException();

        if (userStorage.getUser(userId) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос addLike.");
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        if (storage.getFilm(id) == null)
            throw new FriendNotFoundException();

        if (userStorage.getUser(userId) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос removeLike.");
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsWithMostLikes(@RequestParam Optional<Integer> count) {
        log.info("Выполнен запрос getFilmsWithMostLikes.");
        return service.getFilmsWithMostLikes(count.orElse(10));
    }
}