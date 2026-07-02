package tests.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateTimestampFolder {

    static String createFolder() {

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String folderName = current.format(formatter);

        Path path = Paths.get(folderName);

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folderName;

    }
}
