package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException  extends  Exception{

    public FilmValidationException() {
    }

    public FilmValidationException(final String message) {
        super(message);
    }
}
