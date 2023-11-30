package ru.yandex.practicum.filmrate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.exception.ValidateException;
import ru.yandex.practicum.filmrate.model.User;
import ru.yandex.practicum.filmrate.service.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Integer id,
                                      @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public Set<User> getFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
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
        return userService.getAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidateException {
        nameChecker(user);
        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidateException {
        nameChecker(user);
        return userService.update(user);
    }

    private void nameChecker(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Пустое имя, было присвоено значение логина {}", user.getLogin());
        }
    }
}