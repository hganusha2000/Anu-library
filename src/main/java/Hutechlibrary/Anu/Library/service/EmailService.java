package Hutechlibrary.Anu.Library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public void sendActivationEmail(String to, String username, String activationToken) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String activationLink = baseUrl + "/api/auth/activate?token=" + activationToken;
        String subject = "Account Activation";
        String content = "<h3>Welcome, " + username + "!</h3>" +
                "<p>Please click the link below to activate your account:</p>" +
                "<a href=\"" + activationLink + "\">Activate Account</a>" +
                "<p>This link will expire in 24 hours.</p>";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }
}
