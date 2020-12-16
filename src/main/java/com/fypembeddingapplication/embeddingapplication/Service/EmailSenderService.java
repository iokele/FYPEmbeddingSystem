package com.fypembeddingapplication.embeddingapplication.Service;

import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private JavaMailSender javaMailSender;
    @Async
    public void sendEmail(String userMail, String token){
        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(userMail);
            messageHelper.setSubject("Mail Confirmation Link!");
            messageHelper.setText("Thank you for registering. Please enter below token in the application to activate your account:" + token );
            javaMailSender.send(mailMessage);
        }catch (MessagingException e){
            e.printStackTrace();
        }catch (MailException e){
            e.printStackTrace();
        }

    }
}
