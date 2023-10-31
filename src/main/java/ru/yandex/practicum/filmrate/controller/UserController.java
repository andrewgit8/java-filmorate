package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.ValidateException;
import ru.yandex.practicum.filmrate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
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
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Был/а зарегистрирован новый юзер " + user.getLogin() + " идентификатор: " + user.getId());
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidateException {
        validate(user);

        if (users.get(user.getId()) == null) {
            log.error("Было невозможно обновить данные, пользователь " + user.getId() + " не существовал");
            throw new ValidateException("User с " + user.getId() + " идентификатором не существует. Вызовите метод POST");
        } else {
            users.put(user.getId(), user);
            log.info("Данные под идентификатором " + user.getId() + " были обновлены");
        }
        return user;
    }

    private void validate(User user) throws ValidateException {
       if (user.getEmail().indexOf('@') == -1 || user.getEmail().isBlank()) {
            log.error("Некорректный ввод почтового ящика " + user.getEmail());
            throw new ValidateException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.error("Некорректный ввод логина " + user.getEmail());
            throw new ValidateException("Логин не может быть пустым или содержать пробелы");
        } /*else if (user.getBirthday().isAfter(LocalDate.now())) {
           log.error("Дата рождения юзера  " + user.getEmail() + " была введена ошибочно");
           throw new ValidateException("Дата рождения не может быть выше сегодняшнего дня");
       }*/ else if (user.getName() == null || user.getName().isBlank()) {
           user.setName(user.getLogin());
           log.warn("Пустое имя, было присвоено значение логина " + user.getLogin());
       }
    }
}
