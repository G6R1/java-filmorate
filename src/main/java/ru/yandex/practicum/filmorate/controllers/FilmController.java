package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    Map<Long, Film> films = new HashMap<>();
    private Long idCounter = 1L;

    @GetMapping()
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) throws FilmValidationException {
        if (!filmValidation(film) || film.getId() != null) {
            log.info("Ошибка создания: некорректные данные о фильме.");
            throw new FilmValidationException();
        }
        Film filmForSave = setId(film);
        films.put(filmForSave.getId(), filmForSave);
        log.info("Выполнен запрос createFilm. Текущее количество фильмов: " + films.size());
        return filmForSave;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmValidationException {
        if (!filmValidation(film) || film.getId() == null) {
            log.info("Ошибка обновления: некорректные данные о фильме.");
            throw new FilmValidationException();
        }
        films.put(film.getId(), film);
        log.info("Выполнен запрос updateFilm.");
        return film;
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || film.getDuration().toMillis() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Задает id новому фильму.
     *
     * @param film Film с id == null
     * @return Film с заданным id.
     */
    private Film setId(Film film) {
        return new Film(idCounter++, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
    }
}