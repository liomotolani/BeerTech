package com.beerkhaton.mealtrackerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String sender;

    @Value("${epic.frontend.url}")
    private String url;


    @Override
    public void sendNewUserEmail(String email, String username, String password) throws MessagingException, MessagingException, UnsupportedEncodingException {
        String content = "    <div>\n" +
                "        <div style=\" text-align: center;\">\n" +
                "            <img style=\"margin-top: 2rem; width: 60%; margin-bottom: 1rem;\" class=\"coinstickLogo\" src=\"https://res.cloudinary.com/danfo/image/upload/v1668175622/logo_gzgxwj.png\" alt=\"\"/>\n" +
                "            </div>\n" +
                "        <hr style=\"background: #22AACC; margin-left: -3%; margin-right: -3% ;\"/>\n" +
                "        <p>Dear "+username+",</p>\n" +
                "\n" +
                "        <p>We are excited to announce the launch of our new meal management platform for our company. As an" +
                "       employee of the company, you are entitled to one meal per day, and our new platform will make it easier for you to manage" +
                "       your meal allowances.</p>\n" +
                "        <p> To access the platform, please use the following login credentials:</p>\n" +
                "        <p>Username : <span style=\"color: #22AACC; font-weight: 650; font-size: medium;\">"+email+"</span></p>\n" +
                "\n" +
                "        <p>Password : <span style=\"color: #22AACC; font-weight: 650; font-size: medium;\">"+password+"</span></p>\n" +
                "\n" +
                "<p> Please note that you will be prompted to change your password on your first login. </p>\n"
                +
                "        <p>You can access the platform using the link : <span style=\"color: #22AACC; font-weight: 650; font-size: medium;\">"+url+"</span></p>\n" +
                "\n" +
                "        <p>If you have any questions or concerns, please don't hesitate to reach out to us. </p>\n" +
                "        <p>Best regards,</p>\n" +
                "<p>Admin</p>\n"+
                "\n" +
                "    </div>";
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(email);
        helper.setFrom(new InternetAddress(sender,"Epic-BeerTech"));
        helper.setReplyTo("noreply@epicbeertech.co");
        helper.setSubject("Meal Ticket Profile");
        helper.setText(content, true);
        helper.setReplyTo("replyto@epicbeertech.co");
        javaMailSender.send(mail);
    }
}
