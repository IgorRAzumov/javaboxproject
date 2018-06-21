package ru.geekbrains.client.data;

public interface ResultCallback<T> {
    void onSuccessful(T result);

    void onFailure(String message);
}
