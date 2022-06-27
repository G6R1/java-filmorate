package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
public class GenreBdStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreBdStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(Long genreId) {
        String sql = "select * from genres where genre_id = ?";

        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(sql, genreId);

        if (genreRowSet.next()) {
            return new Genre(genreRowSet.getLong("genre_id"),
                    genreRowSet.getString("name"));
        } else {
            return null;
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    /**
     * Маппер
     */
    private Genre makeGenre(ResultSet rs) throws SQLException {
        Long id = rs.getLong("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    @Override
    public Map<Long, Set<Genre>> getMapFilmIdSetGenre() {
        String sqlGenres = "select film_id, fg.GENRE_ID as fggenre_id, g.name as gname\n" +
                "from film_genres as fg join genres as g on fg. genre_id = g. genre_id\n";

        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(sqlGenres);

        return makeGenreMap(genresRowSet);
    }

    @Override
    public Map<Long, Set<Genre>> getMapFilmIdSetGenreWithIdFilter(Long filmId) {
        String sqlGenres = "select film_id, fg.GENRE_ID as fggenre_id, g.name as gname " +
                "from film_genres as fg join genres as g on fg. genre_id = g. genre_id " +
                "where film_id = ?";
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(sqlGenres, filmId);

        return makeGenreMap(genresRowSet);
    }

    /**
     * Преобразует данные таблицы film_genres в HashMap<Long, Set<Genre>>, где Long - id фильма,
     * Set<Genre> - сет из жанров фильма с ключем-id для помещения в объект film.
     *
     * @param genresRowSet
     * @return
     */
    private Map<Long, Set<Genre>> makeGenreMap(SqlRowSet genresRowSet) {
        Map<Long, Set<Genre>> genreMap = new HashMap<>();
        while (genresRowSet.next()) {
            Long film_id = genresRowSet.getLong("film_id");
            Long genre_id = genresRowSet.getLong("FGGENRE_ID");
            String genre_name = genresRowSet.getString("GNAME");

            if (genreMap.containsKey(film_id)) {
                genreMap.get(film_id).add(new Genre(genre_id, genre_name));
            } else {
                genreMap.put(film_id, new HashSet<>());
                genreMap.get(film_id).add(new Genre(genre_id, genre_name));
            }
        }
        return genreMap;
    }
}
