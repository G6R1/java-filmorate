package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Data//builder?
public class Film {
    final private Long id;
    @NotBlank
    final private String name;
    @NotBlank
    @Size(min = 1, max = 200)
    final private String description;
    final private LocalDate releaseDate;
    final private Duration duration;
}
