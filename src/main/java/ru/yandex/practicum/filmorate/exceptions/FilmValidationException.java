package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException extends ValidationException {

    public FilmValidationException() {
        super("Incorrect film data.");
    }
}
