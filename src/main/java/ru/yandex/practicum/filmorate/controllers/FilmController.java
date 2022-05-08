package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@RestController
public class FilmController {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    Map<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws FilmValidationException {
        if (!filmValidation(film)) {
            log.info("Ошибка создания: некорректные данные о фильме.");
            throw new FilmValidationException("Ошибка: проверьте корректность данных о фильме.");
        }
        films.put(film.getId(), film);
        log.info("Выполнен запрос createFilm. Текущее количество фильмов: " + films.size());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws FilmValidationException {
        if (!filmValidation(film)) {
            log.info("Ошибка обновления: некорректные данные о фильме.");
            throw new FilmValidationException("Ошибка: проверьте корректность данных о фильме.");
        }
        films.put(film.getId(), film);
        log.info("Выполнен запрос updateFilm.");
        return film;
    }

    /**
     * Проверяет объект Film на соответствие критериям:
     *  название не может быть пустым;
     *  максимальная длина описания — 200 символов;
     *  дата релиза — не раньше 28 декабря 1895 года;
     *  продолжительность фильма должна быть положительной.
     * @param film проверяемый объект.
     * @return результат валидации.
     */
    private boolean filmValidation(Film film) {
        if (film.getName().isBlank()
            || film.getDescription().length() > 200 || film.getDescription().isBlank()
            || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
            || film.getDuration().toMillis() <= 0) {
            return false;
        }
        return true;
    }
}