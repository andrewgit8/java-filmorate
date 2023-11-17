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

    /*Comparator<Film> comparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o1.getLikeList().size() - o2.getLikeList().size();
            }
        };*/

    public List<Film> getCountFilms(Integer count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikeList().size());
        List<Film> films = inMemoryFilmStorage.getAll();
        Collections.sort(films, comparator);

        return films.stream().limit(count).collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public void deleteLike(Integer id, Integer userId) {
        inMemoryFilmStorage.getFilm(id).getLikeList().remove(inMemoryUserStorage.getUser(userId));
    }

    public void putLike(Integer id, Integer userId) {
        inMemoryFilmStorage.getFilm(id).getLikeList().add(inMemoryUserStorage.getUser(userId));
    }
}
