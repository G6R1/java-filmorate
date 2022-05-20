package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage storage;
    Map<Long, Set<Long>> likesMap;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public boolean addLike(Long filmId, Long userId) {
        likesMap.put(filmId, likesMap.getOrDefault(filmId, Collections.emptySet()));
        likesMap.get(filmId).add(userId);
        return true;
    }

    public boolean removeLike(Long filmId, Long userId) {
        likesMap.get(filmId).remove(userId);
        return true;

    }

    public List<Long> showTop10(Long userId) {
        return likesMap.keySet().stream()
                .sorted((x, y) -> likesMap.get(y).size() - likesMap.get(x).size())
                .limit(10)
                .collect(Collectors.toList());
    }

    /*
    добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
    Пусть пока каждый пользователь может поставить лайк фильму только один раз. //лайки = сет из пользователей. сайз
     */

    /*
    перенос валидации
     */
}
