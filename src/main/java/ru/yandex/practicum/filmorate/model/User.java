package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class User {
    final private Long id;
    final private String email;
    final private String login;
    final private String name;
    final private LocalDate birthday;
}
