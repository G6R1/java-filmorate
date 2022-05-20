package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends AbstractController<Film>{
    FilmStorage storage;

    @Autowired
    public FilmController (FilmStorage storage) {
        this.storage = storage;
    }

    @Override
    @GetMapping()
    public List<Film> findAll() {
        return storage.findAll();
    }

    @Override
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws FilmValidationException {
        if (!filmValidation(film) || film.getId() != null) {
            log.info("Ошибка создания: некорректные данные о фильме.");
            throw new FilmValidationException();
        }
        Film filmForSave = storage.create(film);
        log.info("Выполнен запрос createFilm. Текущее количество фильмов: " + storage.findAll().size());
        return filmForSave;
    }

    @Override
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws FilmValidationException {
        if (!filmValidation(film) || film.getId() == null) {
            log.info("Ошибка обновления: некорректные данные о фильме.");
            throw new FilmValidationException();
        }
        Film filmForSave = storage.update(film);
        log.info("Выполнен запрос updateFilm.");
        return filmForSave;
    }

    /**
     * Проверяет объект Film на соответствие критериям:
     * название не может быть пустым;
     * максимальная длина описания — 200 символов;
     * дата релиза — не раньше 28 декабря 1895 года;
     * продолжительность фильма должна быть положительной.
     *
     * @param film проверяемый объект.
     * @return результат валидации.
     */
    private boolean filmValidation(Film film) {
        return !film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                && film.getDuration().toMillis() > 0;
    }
}