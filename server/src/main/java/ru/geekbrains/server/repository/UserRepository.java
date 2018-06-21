package ru.geekbrains.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.server.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByLogin(String login);

    User findUserByEmail(String email);
}
