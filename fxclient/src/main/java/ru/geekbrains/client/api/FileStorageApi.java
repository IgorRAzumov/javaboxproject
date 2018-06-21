package ru.geekbrains.client.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.common.model.FileInfo;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.model.UserData;

import java.util.List;

public interface FileStorageApi {
    @FormUrlEncoded
    @POST("login/signin")
    Call<ServerResponse<UserData>> signIn(@Field("login") String login, @Field("password") String pass);

    @FormUrlEncoded
    @POST("login/signup")
    Call<ServerResponse<UserData>> signUp(@Field("login") String login, @Field("password") String pass,
                                          @Field("email") String email, @Field("name") String name);

    @FormUrlEncoded
    @POST("files/getAllFiles")
    Call<ServerResponse<List<FileInfo>>> getAllFiles(@Field("token") String token);

    @Multipart
    @POST("files/uploadFile")
    Call<ServerResponse<List<FileInfo>>> uploadFile(@Part("token") RequestBody token, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("files/downloadFile")
    @Streaming
    Call<ResponseBody> downloadFile( @Field("token") String token, @Field("fileId") Long fileId);


}
