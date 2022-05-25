package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@ToString
public class Film {
    final private Long id;
    @NotBlank
    final private String name;
    @NotBlank
    @Size(min = 1, max = 200)
    final private String description;
    @Past
    final private LocalDate releaseDate;
    @Positive
    final private Integer duration;
}
