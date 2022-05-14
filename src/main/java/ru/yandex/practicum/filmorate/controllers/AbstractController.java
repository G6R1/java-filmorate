package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractController<K, V> {

    Map<K, V> resourceStorage;

    @GetMapping()
    public List<V> findAllUsers() {
        return new ArrayList<>(resourceStorage.values());
    }

    @PostMapping()
    public abstract V createUser(@Valid @RequestBody V v);

    @PutMapping()
    public abstract V updateUser(@Valid @RequestBody V v);
    }

}
