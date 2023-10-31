package ru.yandex.practicum.filmrate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    /*Используя эти аннотации нужно ли нам реализовывать собственный валидатор или лучше его удалить?
    * Да, такая запись намного короче, но в моем валидаторе логи и более подробное описание ошибки
    * Так что же лучше, удалить мой валидатор и оставить эти аннотации, либо удалить аннотации и оставить валидатор?
    * И я правильно понимаю, что сначала выполняется валидатор, а потом аннотации?*/


    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
