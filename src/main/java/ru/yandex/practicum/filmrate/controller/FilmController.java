package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.FilmIllegalArgumentException;
import ru.yandex.practicum.filmrate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.storage.FilmService;
import ru.yandex.practicum.filmrate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class FilmController {

    private InMemoryFilmStorage inMemoryFilmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFilmNotFound(final FilmNotFoundException e) {
        return Map.of("Ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleIllegalArgument(final FilmIllegalArgumentException e) {
        return Map.of("Ошибка", e.getMessage());
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getCountFilms(@RequestParam(required = false, defaultValue = "10") String count) {
        return filmService.getCountFilms(Integer.parseInt(count));
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id,
                        @PathVariable Integer userId) {
        filmService.putLike(id, userId);
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