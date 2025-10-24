package fourpetals.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/401")
    public String error401() {
        return "error/401";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }
}
