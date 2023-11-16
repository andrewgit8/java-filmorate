package ru.yandex.practicum.filmrate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private int id = 1;
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Был создан новый фильм {} идентификатор {}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.get(film.getId()) == null) {
            log.error("Был передан несуществующий в базе данных фильм {}", film.getName());
            throw new ValidateException("Фильма  " + film.getName() + " не существует. Вызовите метод POST");
        } else {
            films.put(film.getId(), film);
            log.info("Задача под номером " + film.getId() + " успешно обновлена");
        }
        return film;
    }
}
