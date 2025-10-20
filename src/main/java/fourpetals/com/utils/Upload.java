package fourpetals.com.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import fourpetals.com.entity.ProductImage;

@Component
public class Upload {
	@Value("${file.upload.dir:D:/upload/}")
	private String uploadDir;

	public String saveFile(MultipartFile file, String relativeFolder) throws IOException {
		if (file == null || file.isEmpty()) {
			return null;
		}
		Path dirPath = Paths.get(uploadDir + relativeFolder);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		String originalFilename = file.getOriginalFilename();
		String fileName = System.currentTimeMillis() + "_" + originalFilename;
		Path filePath = dirPath.resolve(fileName);
		Files.write(filePath, file.getBytes());
		return relativeFolder + "/" + fileName;
	}

	public File getFile(String filePath) {
		return new File(uploadDir + filePath);
	}

	public boolean deleteFile(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			return false;
		}
		File file = getFile(filePath);
		return file.exists() && file.delete();
	}

	public void deleteFile(Set<ProductImage> images) {
		if (images == null || images.isEmpty()) {
			return;
		}
		for (ProductImage img : images) {
			try {
				deleteFile(img.getDuongDan());
			} catch (Exception e) {
				// Log or handle error silently
			}
		}
	}

	public String getUploadDir() {
		return uploadDir;
	}
}