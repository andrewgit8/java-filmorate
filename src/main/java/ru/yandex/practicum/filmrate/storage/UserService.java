package ru.yandex.practicum.filmrate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.model.User;
import lombok.extern.slf4j.Slf4j;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;
    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User getUserById(Integer id) {
        return inMemoryUserStorage.getUser(id);
    }

    public Set<User> getCommonFriends(Integer id, Integer otherId) {
        Set<User> commonList = new HashSet<>();

        for (User user : inMemoryUserStorage.getUser(id).getFriendlist()) {
            if (inMemoryUserStorage.getUser(otherId).getFriendlist().contains(user)) {
                commonList.add(user);
            }
        }
        return commonList;
    }

    public Set<User> getFriends(Integer id) {
        log.info("Был передан список друзей: {}", inMemoryUserStorage.getUser(id).getFriendlist().toArray());
        return inMemoryUserStorage.getUser(id).getFriendlist();
    }

    public void addFriend(Integer id, Integer friendId) {
        if (inMemoryUserStorage.getUser(friendId) == null) {
            throw new RuntimeException("Такого друга не существует");
        }
        log.info("Был добавлен друг пользователю {} под именем {}", inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(friendId));
        inMemoryUserStorage.getUser(id).getFriendlist().add(inMemoryUserStorage.getUser(friendId));
        inMemoryUserStorage.getUser(friendId).getFriendlist().add(inMemoryUserStorage.getUser(id));
    }

    public void deleteFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.getUser(id).getFriendlist().remove(inMemoryUserStorage.getUser(friendId));
    }


}
