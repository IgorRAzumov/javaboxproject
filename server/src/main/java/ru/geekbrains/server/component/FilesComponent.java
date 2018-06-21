package ru.geekbrains.server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.geekbrains.server.entity.FileData;
import ru.geekbrains.server.repository.FileRepository;

import java.util.List;

@Component
public class FilesComponent {
    @Autowired
    private FileRepository fileRepository;

    public List<FileData> findAllFilesByUserId(Long userId) {
        return fileRepository.findAllFilesByUserID(userId);
    }

    public void save(FileData fileData) {
        fileRepository.save(fileData);
    }


    public FileData findFileByID(long id) {
        return fileRepository.findOne(id);
    }
}
