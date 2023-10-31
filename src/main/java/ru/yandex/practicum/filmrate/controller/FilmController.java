package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public class FilmController {
    private int id = 1;
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping(value = "/films")
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidateException {
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Был создан новый фильм " + film.getName() + " идентификатор " + film.getId());
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) throws ValidateException {
        validate(film);

        if (films.get(film.getId()) == null) {
            log.error("Была передан несуществующий в базе данных фильм " + film.getName());
            throw new ValidateException("Фильма  " + film.getName() + " не существует. Вызовите метод POST");
        } else {
            films.put(film.getId(), film);
            log.info("Задача под номером " + film.getId() + " успешно обновлена");
        }
        return film;
    }

    private void validate(Film film) throws ValidateException {
        if (film.getName().isBlank()) {
            log.error("Было переданно некорректное имя " + film.getName());
            throw new ValidateException("Имя не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Описание фильма " + film.getName() + " не соответствовало лимиту в 200 символов");
            throw new ValidateException("Описание превышает допустим лимит символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза фильма " + film.getName() + " была введена ошибочно. Фильм не может быть выпущен раньше 28.12.1895");
            throw new ValidateException("Некорректный ввод даты релиза");
        } else if (film.getDuration() < 0 ) {
            log.error("Была введена отрицательная продолжительность у фильма " + film.getName());
            throw new ValidateException("Продолжительность должна быть положительной.");
        }
    }
}
