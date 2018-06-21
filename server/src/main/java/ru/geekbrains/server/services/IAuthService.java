package ru.geekbrains.server.services;

import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.model.UserData;
import ru.geekbrains.server.entity.UserInfo;

public interface IAuthService {
    ServerResponse<UserData> signIn(String login, String password);

    ServerResponse<UserData> signUp(String login, String password, String email, String userName);

    String createUserToken(String login, String userEmail);

    UserData mapUserInfoToUserData(UserInfo userInfo);

}
