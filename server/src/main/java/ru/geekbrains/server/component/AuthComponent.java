package ru.geekbrains.server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.geekbrains.server.entity.User;
import ru.geekbrains.server.repository.UserRepository;

@Component
public class AuthComponent {
    @Autowired
    UserRepository userRepository;

    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean saveUser(User user) {
        return userRepository.save(user) != null;
    }
}
