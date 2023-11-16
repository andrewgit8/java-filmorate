package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.storage.InMemoryFilmStorage;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@RestController
public class FilmController {

    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping(value = "/films")
    public List<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        validate(film);
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws ValidateException {
        validate(film);
        return inMemoryFilmStorage.update(film);
    }

    private void validate(Film film) throws ValidateException {
         if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза фильма {} была введена ошибочно. Фильм не может быть выпущен раньше 28.12.1895", film.getName());
            throw new ValidateException("Некорректный ввод даты релиза");
        }
    }
}