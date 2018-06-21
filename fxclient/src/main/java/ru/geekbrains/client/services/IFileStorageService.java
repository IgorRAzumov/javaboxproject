package ru.geekbrains.client.services;

import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.client.data.ResultCallback;
import ru.geekbrains.common.model.FileInfo;

import java.io.File;
import java.util.List;

public interface IFileStorageService {

    boolean createDirectory(String path);

    void getAllFiles(String token, ResultCallback<List<FileInfo>> listResultCallback);

    void saveFile(MultipartFile multipartFile, String token);

    void uploadFiles(List<File> files, String token, ResultCallback<List<FileInfo>> resultCallback);

    void downloadFile(long fileId, String token, ResultCallback<File> resultCallback);
}