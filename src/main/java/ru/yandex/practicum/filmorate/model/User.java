package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    final private Long id;
    @Email
    @NotBlank
    final private String email;
    @NotBlank
    @Pattern(regexp = "\\S*$")
    final private String login;
    @NotNull
    final private String name;
    @PastOrPresent
    final private LocalDate birthday;
}
