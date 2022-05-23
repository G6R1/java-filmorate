package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage storage;
    Map<Long, Set<Long>> likesMap;

    @Autowired
    public FilmService(FilmStorage storage) {
        likesMap = new HashMap<>();
        this.storage = storage;
    }

    public boolean addLike(Long filmId, Long userId) {
        initiateCheck(filmId);

        likesMap.get(filmId).add(userId);
        return true;
    }

    public boolean removeLike(Long filmId, Long userId) {
        initiateCheck(filmId);

        likesMap.get(filmId).remove(userId);
        return true;

    }

    public List<Film> getFilmsWithMostLikes(Integer num) {
        return storage.findAll().stream()
                .peek((x) -> initiateCheck(x.getId()))
                .sorted((x, y) -> likesMap.get(y.getId()).size() - likesMap.get(x.getId()).size())
                .limit(num)
                .collect(Collectors.toList());
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
    public boolean filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidationException("releaseDate");
        }
        return true;
    }
    private void initiateCheck(Long id) {
        if (!likesMap.containsKey(id))
            likesMap.put(id, new HashSet<>());
    }
}
