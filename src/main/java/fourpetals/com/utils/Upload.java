package fourpetals.com.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class Upload {

	private static final String UPLOAD_DIR = "D:/upload/"; 

    public static String saveFile(MultipartFile file, String relativeFolder) throws IOException {
        if (file.isEmpty()) return null;

        Path dirPath = Paths.get(UPLOAD_DIR + relativeFolder);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = dirPath.resolve(fileName);

        Files.write(filePath, file.getBytes());

        return relativeFolder + "/" + fileName;
    }

    public static File getFile(String relativePath) {
        return new File(UPLOAD_DIR + relativePath);
    }
}
