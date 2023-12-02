package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.service.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(value = "/films/{id}")
    public Optional<Film> getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getTopFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
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
        return filmService.getAll();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        validate(film);
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws ValidateException {
        validate(film);
        return filmService.update(film);
    }

    private void validate(Film film) throws ValidateException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза фильма {} была введена ошибочно. Фильм не может быть выпущен раньше 28.12.1895", film.getName());
            throw new ValidateException("Некорректный ввод даты релиза");
        }
    }
}