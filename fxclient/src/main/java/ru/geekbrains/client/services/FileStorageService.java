package ru.geekbrains.client.services;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.geekbrains.client.MyDate;
import ru.geekbrains.client.data.FileInfoTableViewEntity;
import ru.geekbrains.client.data.ResultCallback;
import ru.geekbrains.client.retrofit.RetrofitHelper;
import ru.geekbrains.common.model.FileInfo;
import ru.geekbrains.common.model.RequestResult;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.utils.FileStorageUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import retrofit2.ResultCallback;

public class FileStorageService implements IFileStorageService {
    private FileStorageUtils fileStorageUtils;

    @Override
    public boolean createDirectory(String path) {
        return false;
    }

    @Override
    public void getAllFiles(String token, ResultCallback<List<FileInfo>> controllerResultCallback) {
        RetrofitHelper
                .getFileStorageApi()
                .getAllFiles(token)
                .enqueue(new Callback<ServerResponse<List<FileInfo>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<FileInfo>>> call, Response<ServerResponse<List<FileInfo>>> response) {
                        ServerResponse<List<FileInfo>> serverResponse;
                        if (response.isSuccessful() && (serverResponse = response.body()) != null) {
                            if (serverResponse.getRequestResult() == RequestResult.OK) {
                                List<FileInfo> fileInfoList = serverResponse.getBody();
                                if (fileInfoList != null) {
                                    controllerResultCallback.onSuccessful(fileInfoList);
                                } else {
                                    controllerResultCallback.onFailure("ошибка запроса. повторите еще раз");
                                }
                            } else {
                                controllerResultCallback.onFailure(serverResponse.getMessage());

                            }
                        } else {
                            controllerResultCallback.onFailure("ошибка запроса. повторите еще раз");
                        }
                    }


                    @Override
                    public void onFailure(Call<ServerResponse<List<FileInfo>>> call, Throwable throwable) {
                        controllerResultCallback.onFailure("ошибка запроса. повторите еще раз");
                    }
                });
    }


    public void saveFile(MultipartFile multipartFile, String token) {
    }


    @Override
    public void uploadFiles(List<File> files, String token, ResultCallback<List<FileInfo>> resultCallback) {
        File file;
        file = files.get(0);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RetrofitHelper
                .getFileStorageApi()
                .uploadFile(requestToken, body)
                .enqueue(new Callback<ServerResponse<List<FileInfo>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<List<FileInfo>>> call, Response<ServerResponse<List<FileInfo>>> response) {
                        ServerResponse<List<FileInfo>> serverResponse;
                        if (response.isSuccessful() && (serverResponse = response.body()) != null) {
                            if (serverResponse.getRequestResult() == RequestResult.OK) {
                                List<FileInfo> fileInfoList = serverResponse.getBody();
                                if (fileInfoList != null) {
                                    resultCallback.onSuccessful(fileInfoList);
                                } else {
                                    resultCallback.onFailure("ошибка запроса. повторите еще раз");
                                }
                            } else {
                                resultCallback.onFailure(serverResponse.getMessage());

                            }
                        } else {
                            resultCallback.onFailure("ошибка запроса. повторите еще раз");
                        }
                    }


                    @Override
                    public void onFailure(Call<ServerResponse<List<FileInfo>>> call, Throwable throwable) {
                        resultCallback.onFailure("ошибка запроса. повторите еще раз");
                    }
                });
    }

    @Override
    public void downloadFile(long fileId, String token, ResultCallback<File> resultCallback) {
        RetrofitHelper
                .getFileStorageApi()
                .downloadFile( token, fileId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody =response.body();
                            //Files.write(Paths.get("/athive",resp),responseBody.bytes())
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                    }
                });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream((Paths.get("/")).toFile());//futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public List<FileInfoTableViewEntity> mapDateToTableViewFormat(List<FileInfo> fileInfoList) {
        List<FileInfoTableViewEntity> listEntity = new ArrayList<>();
        for (FileInfo fileInfo : fileInfoList) {
            listEntity.add(new FileInfoTableViewEntity(fileInfo.getSize(), fileInfo.getName(),
                    new MyDate(fileInfo.getLastModify().getTime())));
        }
        return listEntity;
    }

}
