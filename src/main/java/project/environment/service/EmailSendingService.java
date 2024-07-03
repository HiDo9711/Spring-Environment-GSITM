package project.environment.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendAuthenticationEmail(String recipientEmail, String authenticationCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "지방상수도 수질 정보 제공 페이지 이메일 인증";

        String htmlContent = "<html><body>" +
                "<div style='background-color: #87CEEB; padding: 20px;'>" +
                "<div style='background-color: white; padding: 20px; border-radius: 10px;'>" +
                "<h2 style='color: #32CD32; text-align: center;'>지방상수도 수질 정보 제공 페이지 코드</h2>" +
                "<p style='text-align: center;'>아래 코드를 입력하여 이메일 인증을 완료해주세요</p><br><br>" +
                "<div style='text-align: center; border: 2px solid #32CD32; padding: 10px; font-size: 18px; font-weight: bold;'>" +
                authenticationCode +
                "</div>" +
                "<br><br><p style='text-align: center;'>이메일 인증을 위해 위 코드를 입력하면 회원가입이 정상 진행됩니다.</p>" +
                "</div></div></body></html>";

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); 

        javaMailSender.send(message);
    }

}