package ru.yandex.practicum.filmorate.exceptions;

public class UserValidationException extends Exception{

    public UserValidationException() {
    }

    public UserValidationException(final String message) {
        super(message);
    }
}
