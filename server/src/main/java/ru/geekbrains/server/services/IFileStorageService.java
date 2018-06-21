package ru.geekbrains.server.services;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.geekbrains.common.model.FileInfo;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.server.entity.FileData;

import java.util.List;

public interface IFileStorageService {
    //получение списка файлов пользователя
    ServerResponse<List<FileInfo>> getAllFiles(String token);

    //сохраниние на диск и в БД
    ServerResponse<FileInfo> uploadFile(MultipartFile file, String token);

    ServerResponse<FileInfo> uploadFiles(List<MultipartFile> files, String token);

    StreamingResponseBody downloadFile(String token, Long fileId);

    StreamingResponseBody downloadFiles(String token, List<Long> fileIds);

    ServerResponse<List<FileInfo>> deleteFile(long fileId, String token);

    ServerResponse<List<FileInfo>> deleteFiles(List<Long> fileIds, String token);

    List<FileInfo> mapFileDataToFileInfo(List<FileData> fileDataList);


}
