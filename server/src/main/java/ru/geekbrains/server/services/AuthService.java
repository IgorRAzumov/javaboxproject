package ru.geekbrains.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.common.model.RequestResult;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.model.UserData;
import ru.geekbrains.common.utils.AuthUtils;
import ru.geekbrains.server.component.AuthComponent;
import ru.geekbrains.server.component.UsersComponent;
import ru.geekbrains.server.entity.User;
import ru.geekbrains.server.entity.UserInfo;

import javax.annotation.PostConstruct;
import java.util.Random;


@Service
public class AuthService implements IAuthService {


    private final static String LOGIN_ERROR = "invalid login or password";
    private final static String LOGIN_BUSY = "login busy";
    private final static String EMAIL_BUSY = "email busy";
    private static final String EMAIL_MISTAKE = "недопустимые символы в емайл";
    private static final String PASSWORD_MISTAKE = "недопустимые символы в пароле";
    private static final String LOGIN_MISTAKE = "недопустимые символы в логине";
    private static final String SHA_ENCRYPTED_MESSAGE = "application error";
    private static final String USER_NAME_MISTAKE = "user name mistake";
    private static final String ERROR_CREATE_HOME_DIRECTORY = "error create directory";
    private static String ERROR_SAVE_USER = "error create user";
    private static String ERROR_AUTH_USER = "error authorization";
    private static long START_STORAGE_SIZE = 200;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    private AuthComponent authComponent;
    @Autowired
    private UsersComponent usersComponent;
    private AuthUtils authUtils;
    private Random random;

    @PostConstruct
    public void init() {
        authUtils = new AuthUtils();// что если пометить аннотацией @Bean класс AuthUtils, но он же используется и в client
    }

    @Override
    public ServerResponse<UserData> signIn(String login, String password) {//авторизация
        ServerResponse<UserData> response = new ServerResponse<>();//создаем модель ответа
        response.setRequestResult(RequestResult.ERROR);
        //в БД две сущности User(login,password,email) и UserInfo(сстальные данные о пользователе)

        if (authUtils.checkLoginMistake(login)) {
            if (authUtils.checkPasswordMistake(password)) {
                User user = authComponent.getUserByLogin(login);//получение по логину из БД usera
                if (user != null) {
                    if (user.getPassword().equals(password)) {
                        UserInfo userInfo = user.getUserInfo();//получение User info (One to one с user)

                        String token = userInfo.getToken();//токен сессии хранится в БД может стоит хэшировать на какое-то время?
                        if (token == null || token.isEmpty()) {
                            token = createUserToken(user.getLogin(), user.getEmail());
                            userInfo.setToken(token);
                            usersComponent.save(userInfo);//Сохранения токена в БД
                        }

                        response.setRequestResult(RequestResult.OK);//результат пакуем в serverResponse
                        response.setBody(mapUserInfoToUserData(userInfo));//преобразуем ответ к модели ответа
                    } else {
                        response.setMessage(LOGIN_ERROR);
                    }
                } else {
                    response.setMessage(LOGIN_ERROR);
                }
            } else {
                response.setMessage(PASSWORD_MISTAKE);
            }
        } else {
            response.setMessage(LOGIN_MISTAKE);
        }
        return response;
    }

    @Override
    public ServerResponse<UserData> signUp(String login, String password, String email, String userName) {
        ServerResponse<UserData> response = new ServerResponse<>();
        response.setRequestResult(RequestResult.ERROR);

        if (!userName.isEmpty()) {
            if (authUtils.checkLoginMistake(login)) {
                if (authUtils.checkPasswordMistake(password)) {
                    if (authUtils.checkEmailMistake(email)) {
                        if (authComponent.getUserByLogin(login) == null) {
                            if (authComponent.getUserByEmail(email) == null) {
                                try {
                                    UserInfo userInfo = new UserInfo(userName, START_STORAGE_SIZE);
                                    userInfo.setToken(createUserToken(login, email));

                                    String homeDirectoryPath = fileStorageService.createUserHomeDirectory
                                            (authUtils.encryptSHA256(email//создаем домашнюю папку по email(он уникален)
                                                    .replace("@", "")
                                                    .replace(".", "")));
                                    if (homeDirectoryPath != null) {
                                        userInfo.setHomePath(homeDirectoryPath);
                                        User user = new User(login, password, email, userInfo);

                                        if (authComponent.saveUser(user)) {
                                            response.setRequestResult(RequestResult.OK);
                                            response.setBody(mapUserInfoToUserData(userInfo));
                                        } else {
                                            response.setMessage(ERROR_SAVE_USER);
                                        }
                                    } else {
                                        response.setMessage(ERROR_CREATE_HOME_DIRECTORY);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    response.setMessage(SHA_ENCRYPTED_MESSAGE);
                                }
                            } else {
                                response.setMessage(EMAIL_BUSY);
                            }
                        } else {
                            response.setMessage(LOGIN_BUSY);
                        }
                    } else {
                        response.setMessage(EMAIL_MISTAKE);
                    }
                } else {
                    response.setMessage(PASSWORD_MISTAKE);
                }
            } else {
                response.setMessage(LOGIN_MISTAKE);
            }
        } else {
            response.setMessage(USER_NAME_MISTAKE);
        }
        return response;
    }

    // маппинг модели с БД в модель Ответа
    @Override
    public UserData mapUserInfoToUserData(UserInfo userInfo) {
        return new UserData(userInfo.getName(), userInfo.getFreeDiscSpace(),
                userInfo.getToken());
    }

    //метод создания токена, не лучшая реализация
    @Override
    public String createUserToken(String login, String userEmail) {//очень примитивный генератор токенов
        if (random == null) {
            random = new Random();
        }
        String token = "";
        String digitBase = String.valueOf(Math.abs(random.nextLong()));
        String alphabetBase = (login + userEmail
                .replace("@", "")
                .replace(".", ""));
        StringBuilder tmp = new StringBuilder();
        int minLengthBase = (alphabetBase.length() <= digitBase.length()) ? alphabetBase.length() : digitBase.length();
        for (int i = 0; i < minLengthBase; i++) {
            tmp
                    .append(alphabetBase.charAt(i))
                    .append(digitBase.charAt(i));
        }

        try {
            token = authUtils.encryptSHA256(tmp.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //обработка ошибки шифрования
        }
        return token;
    }
}


