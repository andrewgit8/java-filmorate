package ru.yandex.practicum.filmrate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<Film> films = inMemoryFilmStorage.getAll();
        Collections.sort(films, comparator);
        List<Film> test = films.stream().limit(count).collect(Collectors.toList());
        System.out.println("TEST " + test.toString());
        return films.stream().limit(count).collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public void deleteLike(Integer id, Integer userId) {
        inMemoryFilmStorage.getFilm(id).deleteLike(userId);
    }

    public void putLike(Integer id, Integer userId) {
        System.out.println("Ставлю лайк");
        inMemoryFilmStorage.getFilm(id).addLike(userId);
    }
}
