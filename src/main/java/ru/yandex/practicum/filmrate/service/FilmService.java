package ru.yandex.practicum.filmrate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmrate.exception.BadRequestException;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmrate.storage.InMemoryUserStorage;

@Slf4j
@Service
public class FilmService {
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getTopFilms(Integer count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikeList().size());

        return inMemoryFilmStorage.getAll()
                .stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<Film> getFilmById(Integer id) {
        Optional<Film> result = Optional.ofNullable(inMemoryFilmStorage.getFilm(id));

        if (result.isEmpty()) {
            throw new NotFoundException("Фильма с таким идентификатором нет в базе");
        }
        return result;
    }

    public List<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    public Film create(Film film) {
        inMemoryFilmStorage.create(film);
        return film;
    }

    public Film update(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            log.error("Был передан несуществующий в базе данных фильм {}", film.getName());
            throw new ValidateException("Фильма  " + film.getName() + " не существует. Вызовите метод POST");
        } else {
            return inMemoryFilmStorage.update(film);
        }
    }

    public void deleteLike(Integer id, Integer userId) {
        if (id < 0 || userId < 0) {
            throw new BadRequestException("Айди не может быть отрицательным");
        }
        inMemoryFilmStorage.getFilm(id).deleteLike(userId);
    }

    public void putLike(Integer id, Integer userId) {
        if (id < 0 || userId < 0) {
            throw new BadRequestException("Айди не может быть отрицательным");
        }

        inMemoryFilmStorage.getFilm(id).addLike(userId);
    }
}
