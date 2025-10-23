package fourpetals.com.controller.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

    @GetMapping("/api/session")
    public String getSessionId(HttpSession session) {
        return session.getId();
    }
}