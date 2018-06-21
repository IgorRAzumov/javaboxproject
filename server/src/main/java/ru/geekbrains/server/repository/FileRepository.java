package ru.geekbrains.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.geekbrains.server.entity.FileData;

import java.util.List;

public interface FileRepository extends CrudRepository<FileData, Long> {
    List<FileData> findAllFilesByUserID(Long userId);

}
