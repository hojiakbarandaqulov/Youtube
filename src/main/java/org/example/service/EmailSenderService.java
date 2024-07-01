package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Value("${spring.mail.username}")
    String fromMailAddress;

    public void sendEmail(String token){
        String email =  JwtUtil.decode(token).getUsername();
        String url = "http://localhost:8080/auth/verification/" + token;
        String formatText = "<style>\n" +
                "    a:link, a:visited {\n" +
                "        background-color: #f44336;\n" +
                "        color: white;\n" +
                "        padding: 14px 25px;\n" +
                "        text-align: center;\n" +
                "        text-decoration: none;\n" +
                "        display: inline-block;\n" +
                "    }\n" +
                "\n" +
                "    a:hover, a:active {\n" +
                "        background-color: red;\n" +
                "    }\n" +
                "</style>\n" +
                "<div style=\"text-align: center\">\n" +
                "    <h1>Welcome to YouTube</h1>\n" +
                "    <br>\n" +
                "    <p>Please click button  below to complete registration</p>\n" +
                "    <div style=\"text-align: center\">\n" +
                "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                "    </div>";
        String text = String.format(formatText, url);
        send(email, "Complete registration", text);
        emailHistoryService.save(email, url);
    }

    public void sendEmailForChange(String token){
        String email =  JwtUtil.decode(token).getUsername();
        String url = "http://localhost:8080/profile/verification/" + token;
        String formatText = "<style>\n" +
                "    a:link, a:visited {\n" +
                "        background-color: #f44336;\n" +
                "        color: white;\n" +
                "        padding: 14px 25px;\n" +
                "        text-align: center;\n" +
                "        text-decoration: none;\n" +
                "        display: inline-block;\n" +
                "    }\n" +
                "\n" +
                "    a:hover, a:active {\n" +
                "        background-color: red;\n" +
                "    }\n" +
                "</style>\n" +
                "<div style=\"text-align: center\">\n" +
                "    <h1>Welcome to YouTube</h1>\n" +
                "    <br>\n" +
                "    <p>Please click button  below to complete registration</p>\n" +
                "    <div style=\"text-align: center\">\n" +
                "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                "    </div>";
        String text = String.format(formatText, url);
        send(email, "Complete registration", text);
        emailHistoryService.save(email, url);
    }



    public void send(String toAccount, String subject, String text) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromMailAddress);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
