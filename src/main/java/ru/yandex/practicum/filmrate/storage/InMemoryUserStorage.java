package ru.yandex.practicum.filmrate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Был/а зарегистрирован новый юзер {} идентификатор: {}", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()) == null) {
            log.error("Было невозможно обновить данные, пользователь {} не существовал", user.getId());
            throw new ValidateException("User с " + user.getId() + " идентификатором не существует. Вызовите метод POST");
        } else {
            users.put(user.getId(), user);
            log.info("Данные под идентификатором {} были обновлены", user.getId());
        }
        return user;
    }

    public User getUser(Integer id) {
        return users.get(id);
    }
}
