package ru.yandex.practicum.filmrate.storage;

import ru.yandex.practicum.filmrate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User create(User user);

    User update(User user);
}
