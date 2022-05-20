package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private Long idCounter = 1L;
    private final Map<Long, Film> filmMap;

    public InMemoryFilmStorage() {
        filmMap = new HashMap<>();
    }


    public List<Film> findAll() {
        return new ArrayList<>(filmMap.values());
    }

    public Film create(Film film) {
        Film filmForSave = setId(film);
        filmMap.put(filmForSave.getId(), filmForSave);
        return filmForSave;
    }

    public Film update(Film film) throws FilmValidationException {
        filmMap.put(film.getId(), film);
        return film;
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
