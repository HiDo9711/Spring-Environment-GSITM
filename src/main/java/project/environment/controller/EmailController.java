package project.environment.controller;

import jakarta.mail.MessagingException;
import project.environment.email.EmailRequest;
import project.environment.email.VerificationRequest;
import project.environment.service.EmailSendingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/user")
public class EmailController {

    @Autowired
    private EmailSendingService emailService;

    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @PostMapping("/send-email")
    @ResponseBody
    public String sendEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        String email = emailRequest.getEmail();
        String verificationCode = generateVerificationCode();
        verificationCodes.put(email, verificationCode);
        emailService.sendAuthenticationEmail(email, verificationCode);
        return "이메일이 발송되었습니다.";
    }

    @PostMapping("/verify-code")
    @ResponseBody
    public Map<String, Object> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        String code = verificationRequest.getCode();
        Map<String, Object> response = new HashMap<>();

        String savedCode = verificationCodes.get(email);
        if (savedCode != null && savedCode.equals(code)) {
            response.put("success", true);
            response.put("message", "인증 코드가 확인되었습니다.");
            verificationCodes.remove(email);
        } else {
            response.put("success", false);
            response.put("message", "잘못된 인증 코드입니다.");
        }
        return response;
    }
    
    // 8자리 랜덤 코드
    private String generateVerificationCode() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890123456789";
        final int LENGTH = 8;
        final Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}