package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("validObjectFactory")
    void filmValidationOkTest(Film film) throws Exception {
        this.mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidObjectFactory")
    void filmValidationNotOkTest(Film film) throws Exception {
        this.mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    static Stream<Film> validObjectFactory() {
        return Stream.of(
            new Film(12L, "Oreshek", "Big boy", LocalDate.of(1999,1,1), Duration.ofSeconds(123)),
            new Film(12L, "Oreshek", "Big boy", LocalDate.of(1895,12,28), Duration.ofSeconds(123)),
            new Film(12L, "Oreshek", "Big boy", LocalDate.of(1999,1,1), Duration.ofSeconds(1))
        );
    }

    static Stream<Film> invalidObjectFactory() {
        return Stream.of(
                new Film(1L, "", "Empty name", LocalDate.of(1999,1,1), Duration.ofSeconds(123)),
                new Film(2L, "Empty description", "", LocalDate.of(1999,1,1), Duration.ofSeconds(123)),
                new Film(3L, "Description 200+", "Sddnvlkdshfvskdjfksikhfunsdcwjhikjemkfjdsjvfisjnvujshosiufhiuerhfpoerjfverbvefddfshfslnfsnvofedhvuofshjmochdlhdjmcfjdbmgkdjdgfjcmdgfjhdvkfjgfhgfgfdgfvvtvrwvcgsvcgvcsfdchcgfcxhwfuxngbgbfdvgsvfjhgvhfdbdsss", LocalDate.of(1999,1,1), Duration.ofSeconds(123)),
                new Film(4L, "Date", "Date is before 28.12.1895", LocalDate.of(1895,12,27), Duration.ofSeconds(123)),
                new Film(4L, "Duration", "Duration is zero", LocalDate.of(1895,12,27), Duration.ofSeconds(0))
        );
    }
}