package ru.geekbrains.client.services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.geekbrains.client.data.ResultCallback;
import ru.geekbrains.client.retrofit.RetrofitHelper;
import ru.geekbrains.common.model.RequestResult;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.model.UserData;
import ru.geekbrains.common.utils.AuthUtils;

public class AuthService implements IAuthService {
    private static final String ERROR_NETWORK = "network error";
    private static final String ERROR_REQUEST = "response error ";
    private AuthUtils authUtils;

    public AuthService() {
        authUtils = new AuthUtils();
    }

    @Override
    public void signIn(String login, String password, ResultCallback resultCallback) throws Exception {
        password = authUtils.encryptSHA256(password);

        RetrofitHelper
                .getFileStorageApi()
                .signIn(login, password)
                .enqueue(new Callback<ServerResponse<UserData>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<UserData>> call, Response<ServerResponse<UserData>> response) {
                        handleResponse(response, resultCallback);
                    }

                    @Override
                    public void onFailure(Call<ServerResponse<UserData>> call, Throwable throwable) {
                        failureResponse(throwable);
                    }
                });
    }

    @Override
    public void signUp(String login, String password, String email, String userName, ResultCallback resultCallback) throws Exception {
        password = authUtils.encryptSHA256(password);
        RetrofitHelper
                .getFileStorageApi()
                .signUp(login, password, email, userName)
                .enqueue(new Callback<ServerResponse<UserData>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<UserData>> call, Response<ServerResponse<UserData>> response) {
                        handleResponse(response, resultCallback);
                    }

                    @Override
                    public void onFailure(Call<ServerResponse<UserData>> call, Throwable throwable) {
                        failureResponse(throwable);
                    }
                });
    }

    private void handleResponse(Response<ServerResponse<UserData>> response, ResultCallback resultCallback) {
        if (response.isSuccessful() && response.body() != null) {
            ServerResponse<UserData> serverResponse = response.body();
            if (serverResponse.getRequestResult() == RequestResult.OK) {
                resultCallback.onSuccessful(serverResponse.getBody());
            } else {
                resultCallback.onFailure(serverResponse.getMessage());
            }
        } else {
            resultCallback.onFailure(ERROR_NETWORK);
        }
    }

    private void failureResponse(Throwable throwable) {

    }
}
