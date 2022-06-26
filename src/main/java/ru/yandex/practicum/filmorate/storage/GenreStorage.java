package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public interface GenreStorage {

    Genre getGenre(Long genreId) ;

    List<Genre> getAllGenres() ;

    Map<Long, Set<Genre>> getMapFilmIdSetGenre() ;

    Map<Long, Set<Genre>> getMapFilmIdSetGenreWithIdFilter(Long filmId) ;

}
