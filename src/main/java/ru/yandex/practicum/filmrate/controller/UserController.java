package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.User;
import ru.yandex.practicum.filmrate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmrate.storage.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    InMemoryUserStorage inMemoryUserStorage;
    UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }
    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id,
                             @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/users")
    public List<User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidateException {
        nameChecker(user);
        return inMemoryUserStorage.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidateException {
        nameChecker(user);
        return inMemoryUserStorage.update(user);
    }

    private void nameChecker(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Пустое имя, было присвоено значение логина {}", user.getLogin());
        }
    }
}