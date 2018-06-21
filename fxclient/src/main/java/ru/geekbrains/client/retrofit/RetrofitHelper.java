package ru.geekbrains.client.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.client.api.FileStorageApi;

import java.util.Date;

public class RetrofitHelper {
    private static final String FILE_STORAGE_API_URL = "http://localhost:8080/";

    private static RetrofitHelper retrofitHelper = new RetrofitHelper();
    private static FileStorageApi fileStorageApi;

    private RetrofitHelper() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class,
                (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));

        Gson gson = builder.create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FILE_STORAGE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        fileStorageApi = retrofit.create(FileStorageApi.class);
    }

    public static RetrofitHelper getInstance() {
        return retrofitHelper;
    }

    public static FileStorageApi getFileStorageApi() {
        return fileStorageApi;
    }
}
