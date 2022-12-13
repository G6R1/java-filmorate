package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select  film_id, f.name as fname, description, releaseDate, duration, " +
                "f.rating_id as frating_id, rm.name as rmname " +
                "from films as f join rating_mpa as rm on f.rating_id = rm.rating_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    /**
     * Маппер
     */
    private Film makeFilm(ResultSet rs) throws SQLException {
        Long id = rs.getLong("film_id");
        String name = rs.getString("fname");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        Integer duration = rs.getInt("duration");
        RatingMPA mpa = new RatingMPA(rs.getLong("frating_id"), rs.getString("rmname"));

        return new Film(id, name, description, releaseDate, duration, mpa, null);
    }

    @Override
    public Film getFilm(Long id) {
        String sql = "select  film_id, f.name as fname, description, releaseDate, duration, " +
                "f.rating_id as frating_id, rm.name as rmname " +
                "from films as f " +
                "join rating_mpa as rm on f.rating_id = rm.rating_id " +
                "where film_id = ?";

        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (filmRowSet.next()) {
            return new Film(
                    filmRowSet.getLong("film_id"),
                    filmRowSet.getString("FNAME"),
                    filmRowSet.getString("description"),
                    filmRowSet.getDate("releaseDate").toLocalDate(),
                    filmRowSet.getInt("duration"),
                    new RatingMPA(filmRowSet.getLong("FRATING_ID"), filmRowSet.getString("RMNAME")),
                    Collections.emptySet()
            );
        } else {
            return null;
        }
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Long id = simpleJdbcInsert.executeAndReturnKey(toMap(film)).longValue();

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)",
                        id,
                        genre.getId());
            }
        }

        return getFilm(id);
    }

    @Override
    public Film update(Film film) {
        String sql = "update films set name = ?, description = ?, releasedate = ?, duration = ?, rating_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        Set<Long> newGenres = new HashSet<>();
        if (film.getGenres() != null)
            newGenres = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());

        Set<Long> oldGenres = new HashSet<>();
        String sqlGenres = "select * from film_genres where film_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGenres, film.getId());
        while (genreRows.next()) {
            oldGenres.add(genreRows.getLong("genre_id"));
        }

        Set<Long> clone = new HashSet<>(oldGenres);
        oldGenres.removeAll(newGenres);
        newGenres.removeAll(clone);

        for (Long genreId : oldGenres) {
            jdbcTemplate.update("delete from film_genres where film_id = ? and genre_id = ?", film.getId(), genreId);
        }

        for (Long genreId : newGenres) {
            jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)", film.getId(), genreId);
        }

        return getFilm(film.getId());
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("releaseDate", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        values.put("likes_counter", 0);
        return values;
    }

    @Override
    public RatingMPA getMpa(Long ratingId) {
        String sql = "select * from rating_MPA where rating_id = ?";

        SqlRowSet mpaRowSet = jdbcTemplate.queryForRowSet(sql, ratingId);

        if (mpaRowSet.next()) {
            RatingMPA ratingMPA = new RatingMPA(mpaRowSet.getLong("rating_id"),
                    mpaRowSet.getString("name"));
            return ratingMPA;
        } else {
            return null;
        }
    }

    @Override
    public List<RatingMPA> getAllMpa() {
        String sql = "select * from rating_MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRatingMPA(rs));
    }

    private RatingMPA makeRatingMPA(ResultSet rs) throws SQLException {
        Long id = rs.getLong("rating_id");
        String name = rs.getString("name");
        return new RatingMPA(id, name);
    }
}
