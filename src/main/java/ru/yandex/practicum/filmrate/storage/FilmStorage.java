package ru.yandex.practicum.filmrate.storage;

import ru.yandex.practicum.filmrate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);
}
