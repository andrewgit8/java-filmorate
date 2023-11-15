package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private int id = 1;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping(value = "/users")
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidateException {
        nameChecker(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Был/а зарегистрирован новый юзер {} идентификатор: {}", user.getLogin(), user.getId());
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidateException {
        nameChecker(user);

        if (users.get(user.getId()) == null) {
            log.error("Было невозможно обновить данные, пользователь {} не существовал", user.getId());
            throw new ValidateException("User с " + user.getId() + " идентификатором не существует. Вызовите метод POST");
        } else {
            users.put(user.getId(), user);
            log.info("Данные под идентификатором {} были обновлены", user.getId());
        }
        return user;
    }

    private void nameChecker(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Пустое имя, было присвоено значение логина {}", user.getLogin());
        }
    }
}