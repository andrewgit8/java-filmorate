package ru.yandex.practicum.filmrate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.exception.UserNotFoundException;
import ru.yandex.practicum.filmrate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User getUserById(Integer id) {
        if (inMemoryUserStorage.getUser(id) == null) {
            throw new UserNotFoundException("Такого друга не существует");
        }
        return inMemoryUserStorage.getUser(id);
    }

    public Set<User> getCommonFriends(Integer id, Integer otherId) {
        Set<User> commonList = new HashSet<>();

        for (Integer integer : inMemoryUserStorage.getUser(id).getFriendlist()) {
            if (inMemoryUserStorage.getUser(otherId).getFriendlist().contains(integer)) {
                commonList.add(getUserById(integer));
            }
        }
        return commonList;
    }

    public Set<User> getFriends(Integer id) {
        Set<User> userList = new TreeSet<>(Comparator.comparing(x -> x.getId()));
        for (Integer number : getUserById(id).getFriendlist()) {
            userList.add(getUserById(number));
        }

        return userList;
    }

    public void addFriend(Integer id, Integer friendId) {
        if (inMemoryUserStorage.getUser(friendId) == null) {
            throw new UserNotFoundException("Такого друга не существует");
        }


        inMemoryUserStorage.getUser(id).addFriend(inMemoryUserStorage.getUser(friendId).getId());
        inMemoryUserStorage.getUser(friendId).addFriend(inMemoryUserStorage.getUser(id).getId());


    }

    public void deleteFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.getUser(id).deleteFriend(inMemoryUserStorage.getUser(friendId).getId());
        inMemoryUserStorage.getUser(friendId).deleteFriend(inMemoryUserStorage.getUser(id).getId());
    }


}
