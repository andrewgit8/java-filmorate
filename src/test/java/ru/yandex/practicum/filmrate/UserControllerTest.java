package ru.yandex.practicum.filmrate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmrate.controller.UserController;
import ru.yandex.practicum.filmrate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Month;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void createTest() throws ValidateException {
        User user = new User("nosferatusod@gmail.com", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        userController.create(user);
        System.out.println(user.toString());
    }

    @Test
    public void getAll() throws ValidateException {
        User user = new User("nosferatusod@gmail.com", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        User user1 = new User("zorrro@yahoo.com", "mande", "Arseny",
                LocalDate.of(1995, Month.JANUARY, 23));

        userController.create(user);
        userController.create(user1);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);

        assertArrayEquals(users.toArray(), userController.getAll().toArray());
    }

    @Test
    public void updateExistedTest() throws ValidateException {
        User user = new User("nosferatusod@gmail.com", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        User user1 = new User(1, "deslanddd@gmail.com", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        userController.create(user);
        userController.update(user1);
    }

    @Test
    public void updateNonExistedTest() {
        User user = new User(5, "nosferatusod@gmail.com", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.update(user);
                    }
                }
        );
        assertEquals("User с 5 идентификатором не существует. Вызовите метод POST", exception.getMessage());
    }

    @Test
    public void createWithEmalWithoutAt() {
        User user = new User("nosferatusodgmail.com", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.create(user);
                    }
                }
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void createWithEmailWithoutSymbols() {
        User user = new User(" ", "nullpointer", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.create(user);
                    }
                }
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void createWithLoginWithGaps() {
        User user = new User("nosferatusod@gmail.com", " ", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.create(user);
                    }
                }
        );
        assertEquals("Логин не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    public void createWithBlankLogin() {
        User user = new User("nosferatusod@gmail.com", "", "Andrey",
                LocalDate.of(1998, Month.JANUARY, 17));

        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.create(user);
                    }
                }
        );
        assertEquals("Логин не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    public void createWithBlankName() throws ValidateException {
        User user = new User("nosferatusod@gmail.com", "nullpointer", "",
                LocalDate.of(1998, Month.JANUARY, 17));
        userController.create(user);
        assertEquals("nullpointer", user.getName());
    }

    @Test
    public void createWithFutureBirthday() {
        User user = new User("nosferatusod@gmail.com", "nullpointer", "",
                LocalDate.of(2446, 8, 20));
        final ValidateException exception = assertThrows(
                ValidateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.create(user);
                    }
                }
        );
        assertEquals("Дата рождения не может быть выше сегодняшнего дня", exception.getMessage());
    }
}
