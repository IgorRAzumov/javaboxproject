package ru.geekbrains.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageUtils {
    public boolean createDirectory(String path) {
        boolean result = false;
        Path directoryPath = Paths.get(path);
        if (!Files.exists(directoryPath)) {
            try {
                if (Files.createDirectory(Paths.get(path)) != null) {
                    result = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
