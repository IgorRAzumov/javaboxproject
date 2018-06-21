package ru.geekbrains.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.geekbrains.common.model.FileInfo;
import ru.geekbrains.common.model.RequestResult;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.common.utils.FileStorageUtils;
import ru.geekbrains.server.component.FilesComponent;
import ru.geekbrains.server.component.UsersComponent;
import ru.geekbrains.server.entity.FileData;
import ru.geekbrains.server.entity.UserInfo;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileStorageService implements IFileStorageService {

    private static final String TOKEN_ERROR = "ошибка авторизации. попробуйте переавторизоваться";
    private static final String SERVER_APP_DIRECTORY = "/archive";
    private static final String ERROR_STORAGE_DATE = "у вас нет файлов";
    private static final String ERROR_SAVE = "шибка сохранения";
    private static final String ERROR_NO_DISK_SPACE = "размер файла превышает оставшегося места ";
    private static final String UNKNOWN_TOKEN = "unknown token";

    @Autowired
    private FilesComponent filesComponent;

    @Autowired
    private UsersComponent usersComponent;

    private FileStorageUtils fileUtils;

    @PostConstruct
    public void init() {
        fileUtils = new FileStorageUtils();
        //создание директории хранилища
        if (!Files.exists(Paths.get(SERVER_APP_DIRECTORY))) {
            if (!fileUtils.createDirectory(SERVER_APP_DIRECTORY)) {
                //бработка - не создалась корневая директория
            }
        }
    }


    //получение списка файлов пользователя
    @Override
    public ServerResponse<List<FileInfo>> getAllFiles(String token) {
        ServerResponse<List<FileInfo>> response = new ServerResponse<>();
        response.setRequestResult(RequestResult.ERROR);

        //запрашиваем информацию о пользователе по токену пользователя в БД)
        UserInfo userInfo = usersComponent.getUserInfoByToken(token);
        if (userInfo == null) {
            response.setMessage(TOKEN_ERROR);
        } else {
            long id = userInfo.getUser().getId();
            //запрашиваем в БД список файлов по id
            List<FileData> fileData = filesComponent.findAllFilesByUserId(id);
            if (fileData == null) {
                response.setMessage(ERROR_STORAGE_DATE);
            } else {
                response.setRequestResult(RequestResult.OK);
                List<FileInfo> list = mapFileDataToFileInfo(fileData);
                response.setBody(list);
            }
        }
        return response;
    }

    //сохраниние на диск и в БД
    @Override
    public ServerResponse<FileInfo> uploadFile(MultipartFile file, String token) {
        ServerResponse<FileInfo> serverResponse = new ServerResponse<>();
        serverResponse.setRequestResult(RequestResult.ERROR);

        UserInfo userInfo = usersComponent.getUserInfoByToken(token);
        if (userInfo != null) {
            long freeDiscSpace = userInfo.getFreeDiscSpace();//проверка свободного места
            long fileSize = file.getSize();
            if (fileSize < freeDiscSpace) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                try {
                    if (!filename.contains("..")) {
                        Files.copy(file.getInputStream(), Paths.get(userInfo.getHomePath(), filename),//
                                StandardCopyOption.REPLACE_EXISTING);//сохраняем файл перезаписываем - потом исправлю

                        userInfo.setFreeDiscSpace(freeDiscSpace - fileSize);//устанавливаем новое размер места
                        usersComponent.save(userInfo);//сохраняем в БД

                        FileData fileData = new FileData(file.getOriginalFilename(),
                                userInfo.getHomePath() + "/" + filename, new Date(),
                                userInfo.getUser().getId(), fileSize);
                        filesComponent.save(fileData);

                        serverResponse.setBody(mapFileDataToFileInfo(fileData));//маппим обьект entity в ответ
                        serverResponse.setRequestResult(RequestResult.OK);
                    } else {
                        //недопустимое имя файла
                    }
                } catch (IOException e) {
                    serverResponse.setMessage(ERROR_SAVE);
                }
            } else {
                serverResponse.setMessage(ERROR_NO_DISK_SPACE);
            }
        } else {
            serverResponse.setMessage(UNKNOWN_TOKEN);
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<FileInfo> uploadFiles(List<MultipartFile> files, String token) {
        return null;
    }

    //отпрака файла пользователю
    @Override
    public StreamingResponseBody downloadFile(String token, Long fileId) {
        UserInfo userInfo = usersComponent.getUserInfoByToken(token);//запрос в бд
        if (userInfo != null) {
            FileData fileData = filesComponent.findFileByID(fileId);
            if (fileData != null && userInfo.getId() == fileData.getUserID()) {
                try {
                    Path path = Paths.get(fileData.getPath());
                    InputStream inputStream = new FileInputStream(path.toFile());
                    return outputStream -> {
                        int nRead;
                        byte[] data = new byte[1024];
                        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                            outputStream.write(data, 0, nRead);
                        }
                    };
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public StreamingResponseBody downloadFiles(String token, List<Long> fileIds) {
        return null;
    }

    @Override
    public ServerResponse<List<FileInfo>> deleteFile(long fileId, String token) {
        return null;
    }

    @Override
    public ServerResponse<List<FileInfo>> deleteFiles(List<Long> fileIds, String token) {
        return null;
    }

    //создание папки вновь зарегестрировавшемуся пользователю
    public String createUserHomeDirectory(String home) {
        String homePath = SERVER_APP_DIRECTORY + File.separatorChar + home;
        return fileUtils.createDirectory(homePath) ? homePath : null;

    }

    //маппинг модели с БД в модель Ответа
    @Override
    public List<FileInfo> mapFileDataToFileInfo(List<FileData> fileDataList) {
        List<FileInfo> filesInfo = new ArrayList<>();
        for (FileData aFileDataList : fileDataList) {
            filesInfo.add(mapFileDataToFileInfo(aFileDataList));
        }
        return filesInfo;
    }

    private FileInfo mapFileDataToFileInfo(FileData fileData) {
        return new FileInfo(fileData.getLastModify(), fileData.getFileName(), fileData.getSize());
    }
}
