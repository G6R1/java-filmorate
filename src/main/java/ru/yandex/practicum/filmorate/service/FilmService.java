package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, LikeStorage likeStorage, UserService userService, GenreStorage genreStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreStorage = genreStorage;
    }

    //работа с сохранением/изменением фильмов

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Long id) {
        Film film = filmStorage.getFilm(id);
        if (film == null)
            throw new FilmNotFoundException();
        return film;
    }

    public Film create(Film film) {
        if (film.getId() != null)
            throw new FilmValidationException("id");

        filmValidation(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() == null)
            throw new FilmValidationException("id");

        if (this.getFilm(film.getId()) == null)
            throw new FilmNotFoundException();

        filmValidation(film);
        return filmStorage.update(film);
    }

    /**
     * Проверяет объект Film на соответствие критериям:
     * дата релиза — не раньше 28 декабря 1895 года;
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


    //работа с лайками

    public boolean addLike(Long filmId, Long userId) {
        if (this.getFilm(filmId) == null)
            throw new FriendNotFoundException();

        if (userService.getUser(userId) == null)
            throw new UserNotFoundException();

        return likeStorage.addLike(filmId, userId);
    }

    public boolean removeLike(Long filmId, Long userId) {
        if (this.getFilm(filmId) == null)
            throw new FriendNotFoundException();

        if (userService.getUser(userId) == null)
            throw new UserNotFoundException();

        return likeStorage.removeLike(filmId, userId);
    }

    public List<Film> getFilmsWithMostLikes(Integer num) {
        return likeStorage.getFilmsWithMostLikes(num);
    }

    //рейтинг/жанры

    public RatingMPA getMpa(Long ratingId) {
        if (ratingId < 1 || ratingId > 5)
            throw new NotFoundException("ratingMpa");
        return filmStorage.getMpa(ratingId);
    }

    public List<RatingMPA> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Genre getGenre(Long genreId) {
        if (genreId < 1 || genreId > 6)
            throw new NotFoundException("Genre");
        return genreStorage.getGenre(genreId);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
