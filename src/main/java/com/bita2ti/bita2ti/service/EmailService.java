package com.bita2ti.bita2ti.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name, String digitalId) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("🎉 Welcome to Bita2ti - Your Digital Identity is Ready");

            String htmlContent =
                    "<div style='font-family:Arial;padding:20px;background:#f4f4f4'>" +
                    "<div style='max-width:600px;margin:auto;background:white;padding:20px;border-radius:10px'>" +

                    "<h1 style='color:#2c3e50'>Welcome to Bita2ti 🚀</h1>" +

                    "<p>Hi <b>" + name + "</b>,</p>" +

                    "<p>Your account has been successfully created.</p>" +

                    "<h2 style='color:#27ae60'>Your Digital ID</h2>" +
                    "<div style='padding:10px;background:#ecf0f1;border-radius:5px;font-size:18px'>" +
                    digitalId +
                    "</div>" +

                    "<p style='margin-top:20px'>" +
                    "With Bita2ti, you now have a unified digital identity for all organizations." +
                    "</p>" +

                    "<hr>" +

                    "<p style='font-size:12px;color:gray'>" +
                    "This is an automated message from Bita2ti System" +
                    "</p>" +

                    "</div></div>";

            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }
}