package ru.geekbrains.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.model.UserData;
import ru.geekbrains.server.services.IAuthService;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    IAuthService authService;

    //авторизация
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ServerResponse<UserData> signIn(@RequestParam String login, @RequestParam String password) {
        return authService.signIn(login, password);
    }

    //регистрация
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ServerResponse<UserData> signUp(@RequestParam String login, @RequestParam String password,
                           @RequestParam String email, @RequestParam String name) {
        return authService.signUp(login, password, email, name);
    }
}
