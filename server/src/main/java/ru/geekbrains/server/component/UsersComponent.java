package ru.geekbrains.server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.geekbrains.server.entity.UserInfo;
import ru.geekbrains.server.repository.UserInfoRepository;

@Component
public class UsersComponent {
    @Autowired
    UserInfoRepository userInfoRepository;

    public boolean save(UserInfo userInfo) {
        return userInfoRepository.save(userInfo) != null;
    }

    public UserInfo getUserInfoByToken(String token) {
        return userInfoRepository.findUserInfoByToken(token);
    }
}
