package fourpetals.com.controller.payment;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fourpetals.com.service.MomoService;

@RestController
@RequestMapping("/api/payment/momo")
public class MomoCallbackController {

    private final MomoService momoService;

    public MomoCallbackController(MomoService momoService) {
        this.momoService = momoService;
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody Map<String, Object> payload) {
        System.out.println("📩 Callback từ MoMo: " + payload);
        String orderId = (String) payload.get("orderId");

        Object rcObj = payload.get("resultCode");
        int resultCode = rcObj instanceof Number ? ((Number) rcObj).intValue() : Integer.parseInt(rcObj.toString());

        momoService.handleCallback(orderId, resultCode);
        return ResponseEntity.ok("OK");
    }
}
