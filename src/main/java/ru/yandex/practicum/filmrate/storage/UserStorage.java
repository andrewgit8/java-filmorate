package ru.yandex.practicum.filmrate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmrate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    List<User> getAll();
    User create(User user);
    User update(User user);
}
