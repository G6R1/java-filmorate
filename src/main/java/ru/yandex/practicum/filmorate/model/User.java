package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {
    final private Long id;
    final private String email;
    final private String login;
    private String name;
    final private Instant birthday;
}
