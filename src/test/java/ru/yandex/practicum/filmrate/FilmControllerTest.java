package ru.yandex.practicum.filmrate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmrate.controller.FilmController;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.storage.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.Month;

public class FilmControllerTest {
    FilmController filmController;
    String underLimit = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ullamcorper, mauris eu fermentum " +
            "pretium, turpis metus suscipit mi, id aliquet nunc neque non lorem. Proin auctor, nisi in gravida iaculis,";
    String limit = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ullamcorper, mauris eu fermentumr" +
            "pretium, turpis metus suscipit mi, id aliquet nunc neque non lorem. Proin auctor, nisi in gravida iaculisrr";


    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new InMemoryFilmStorage());
    }


    @Test
    public void createTest() throws ValidateException {
        Film film = new Film("Пролетая над гнездом кукушки", underLimit, LocalDate.of(1975, Month.MAY, 16), 210);
        filmController.create(film);

    }


    @Test
    public void updateNonExistedFilm() throws ValidateException {
        Film film = new Film(2, "Пролетая над гнездом кукушки", underLimit, LocalDate.of(1975, Month.MAY, 16), 210);

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.update(film);
                    }
                }
        );
        assertEquals("Фильма  Пролетая над гнездом кукушки не существует. Вызовите метод POST", exception.getMessage());
    }


    @Test
    public void updateExistedFilm() throws ValidateException {
        Film film = new Film("Пролетая над гнездом кукушки", underLimit, LocalDate.of(1975, Month.MAY, 16), 210);
        Film film1 = new Film(1, "Пролетая над гнездом кукушки", underLimit, LocalDate.of(1975, Month.MAY, 16), 207);

        filmController.create(film);
        filmController.update(film1);

    }

    @Test
    public void getAllFilms() throws ValidateException {
        Film film = new Film(1, "Пролетая над гнездом кукушки", underLimit, LocalDate.of(1975, Month.MAY, 16), 210);
        Film film1 = new Film(2, "Парфюмер", underLimit, LocalDate.of(2013, Month.MAY, 16), 185);

        filmController.create(film);
        filmController.create(film1);

        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film1);

        assertArrayEquals(films.toArray(), filmController.getAll().toArray());
    }

    @Test
    public void createFilmWithIncorrectName() {
        Film film = new Film(" ", underLimit, LocalDate.of(1995, Month.JANUARY, 16), 210);

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(film);
                    }
                }
        );
        assertEquals("Имя не может быть пустым", exception.getMessage());
    }

    @Test
    public void createFilmWithSymbolsLimit() {
        Film film = new Film("Пианист", limit, LocalDate.of(1995, Month.JANUARY, 16), 210);

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(film);
                    }
                }
        );
        assertEquals("Описание превышает допустим лимит символов", exception.getMessage());
    }

    @Test
    public void createFilmWithTimeLimit() {
        Film film = new Film("Пианист", underLimit, LocalDate.of(1895, Month.DECEMBER, 27), 210);

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(film);
                    }
                }
        );
        assertEquals("Некорректный ввод даты релиза", exception.getMessage());
    }

    @Test
    public void createFilmWithNegativeDuration() {
        Film film = new Film("Пианист", underLimit, LocalDate.of(1995, Month.DECEMBER, 27), -1);

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(film);
                    }
                }
        );
        assertEquals("Продолжительность должна быть положительной.", exception.getMessage());
    }

}
