package fourpetals.com.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fourpetals.com.utils.Upload;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UploadController {

	@Autowired
	private Upload upload;

	@GetMapping("/images/**")
	public ResponseEntity<FileSystemResource> getImage(HttpServletRequest request) {
		String fullPath = request.getRequestURI();
		String relativePath = fullPath.substring(fullPath.indexOf("/images/") + 8);

		File file = upload.getFile(relativePath);

		if (!file.exists() || !file.isFile()) {
			return ResponseEntity.notFound().build();
		}

		String fileName = file.getName().toLowerCase();
		MediaType mediaType = MediaType.IMAGE_JPEG;

		if (fileName.endsWith(".png")) {
			mediaType = MediaType.IMAGE_PNG;
		} else if (fileName.endsWith(".gif")) {
			mediaType = MediaType.IMAGE_GIF;
		} else if (fileName.endsWith(".webp")) {
			mediaType = MediaType.parseMediaType("image/webp");
		} else if (fileName.endsWith(".svg")) {
			mediaType = MediaType.parseMediaType("image/svg+xml");
		}

		return ResponseEntity.ok().contentType(mediaType).body(new FileSystemResource(file));
	}
}
