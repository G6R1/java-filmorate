package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public interface FilmStorage {

    List<Film> getAllFilms();

    Film getFilm(Long id);

    Film create(Film film);

    Film update(Film film);
}

