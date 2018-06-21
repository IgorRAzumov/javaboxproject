package ru.geekbrains.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.geekbrains.common.model.FileInfo;
import ru.geekbrains.common.model.ServerResponse;
import ru.geekbrains.server.services.FileStorageService;

import java.util.List;


@RestController
@RequestMapping("/files")
public class FileStorageController {
    @Autowired
    FileStorageService fileStorageService;


    @RequestMapping(value = "/getAllFiles", method = RequestMethod.POST)
    public ServerResponse<List<FileInfo>> getFilesByToken(@RequestParam String token) {
        return fileStorageService.getAllFiles(token);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ServerResponse<FileInfo> uploadFile(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "file") MultipartFile file) {
        return fileStorageService.uploadFile(file, token);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
    public StreamingResponseBody getSteamingFile(@RequestParam(value = "token") String token,
                                                 @RequestParam(value = "fileId") Long fileId) {
       return fileStorageService.downloadFile(token, fileId);
    }
}
