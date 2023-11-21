package ru.yandex.practicum.filmrate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmrate.exception.FilmIllegalArgumentException;
import ru.yandex.practicum.filmrate.model.Film;

@Service
public class FilmService {
    private InMemoryFilmStorage inMemoryFilmStorage;
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<Film> getCountFilms(Integer count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikeList().size());

        return inMemoryFilmStorage.getAll().stream().sorted(comparator.reversed()).limit(count).collect(Collectors.toList());
    }

    public void deleteLike(Integer id, Integer userId) {
        if (id < 0 || userId < 0) {
            throw new FilmIllegalArgumentException("Айди не может быть отрицательным");
        }
        inMemoryFilmStorage.getFilm(id).deleteLike(userId);
    }

    public void putLike(Integer id, Integer userId) {
        if (id < 0 || userId < 0) {
            throw new FilmIllegalArgumentException("Айди не может быть отрицательным");
        }

        inMemoryFilmStorage.getFilm(id).addLike(userId);
    }
}
