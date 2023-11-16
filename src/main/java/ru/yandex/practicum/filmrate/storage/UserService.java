package ru.yandex.practicum.filmrate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;
    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.getUser(id).addFriend(inMemoryUserStorage.getUser(friendId));
    }

    public void deleteFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.getUser(id).removeFriend(inMemoryUserStorage.getUser(friendId));
    }


}
