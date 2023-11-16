package ru.yandex.practicum.filmrate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmrate.model.Film;

import javax.validation.Valid;
import java.util.List;

public interface FilmStorage {
    List<Film> getAll();
    Film create(Film film);
    Film update(Film film);
}
