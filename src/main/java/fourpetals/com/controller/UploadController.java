package fourpetals.com.controller;

import java.io.File;
import java.io.IOException;

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

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder,   
            Model model) {
        try {
            String relativePath = Upload.saveFile(file, folder);
            model.addAttribute("message", "Upload thành công: " + relativePath);
            model.addAttribute("filePath", relativePath);
        } catch (IOException e) {
            model.addAttribute("message", "Upload thất bại: " + e.getMessage());
        }
        return "upload";
    }

    @GetMapping("/images/**")
    public ResponseEntity<FileSystemResource> getImage(HttpServletRequest request) {
        String relativePath = request.getRequestURI().replace("/images/", "");
        File file = Upload.getFile(relativePath);
        if (!file.exists()) return ResponseEntity.notFound().build();

        String lower = file.getName().toLowerCase();
        MediaType mediaType = MediaType.IMAGE_JPEG;
        if (lower.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
        else if (lower.endsWith(".gif")) mediaType = MediaType.IMAGE_GIF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(new FileSystemResource(file));
    }
}