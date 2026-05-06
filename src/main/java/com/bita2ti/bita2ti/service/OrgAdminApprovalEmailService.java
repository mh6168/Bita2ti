package com.bita2ti.bita2ti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class OrgAdminApprovalEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrgAdminApprovedEmail(String toEmail, String name, String validationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("✅ Your Organization Admin request was approved");

            String htmlContent =
                    "<div style='font-family:Arial;padding:20px;background:#f4f4f4'>" +
                    "<div style='max-width:600px;margin:auto;background:white;padding:20px;border-radius:10px'>" +
                    "<h1 style='color:#27ae60;margin-top:0;'>Approval confirmed 🎉</h1>" +
                    "<p>Hi <b>" + name + "</b>,</p>" +
                    "<p>Your request to become an <b>Organization Admin</b> has been approved.</p>" +
                    "<h3 style='margin-bottom:6px;'>Verify digital IDs for your organization</h3>" +
                    "<p>Use this link to access the validation form:</p>" +
                    "<p><a href='" + validationLink + "'>" + validationLink + "</a></p>" +
                    "<hr>" +
                    "<p style='font-size:12px;color:gray'>" +
                    "This is an automated message from Bita2ti System" +
                    "</p>" +
                    "</div></div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Approved email failed: " + e.getMessage());
        }
    }
}

