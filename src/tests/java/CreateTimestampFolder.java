package tests.java;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateTimestampFolder {

    static String folderNameGenerator() {

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String folderName = current.format(formatter);

        return folderName;

    }
}
