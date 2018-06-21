package ru.geekbrains.client.services;

import ru.geekbrains.client.data.ResultCallback;


public interface IAuthService<T> {

    void signIn(String login, String password, ResultCallback<T> resultCallback) throws Exception;

    void signUp(String login, String password, String email, String userName, ResultCallback<T>resultCallback) throws Exception;


   }