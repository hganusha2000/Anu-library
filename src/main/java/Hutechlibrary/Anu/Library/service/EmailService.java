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

    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            String resetLink = baseUrl + "/reset-password?token=" + token;  // Your frontend reset page
            String subject = "Password Reset Request";
            String content = "<h3>Password Reset Requested</h3>" +
                    "<p>We received a request to reset your password.</p>" +
                    "<p>Click the link below to reset it:</p>" +
                    "<a href=\"" + resetLink + "\">Reset Password</a>" +
                    "<p>This link will expire in 15 minutes.</p>";

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = send HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
