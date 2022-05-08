package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Data//builder?
public class Film {
    final private Long id;
    final private String name;
    final private String description;
    final private LocalDate releaseDate;
    final private Duration duration;
}
