// fourpetals/com/config/WebMvcConfig.java
package fourpetals.com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileSystemRoot = "file:" + uploadRoot; 
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileSystemRoot)
                .setCachePeriod(3600);
    }
}
