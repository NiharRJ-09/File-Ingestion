package com.example.file_ingestion_system.service.notification;

import com.example.file_ingestion_system.model.entity.FileJob;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    public EmailService(
            JavaMailSender mailSender,
            TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendJobCompletedNotification(FileJob job) {
        try {
            Context context = new Context();
            context.setVariable("job", job);
            context.setVariable("user", job.getUser());

            String content = templateEngine.process("job-completed", context);

            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(job.getUser().getEmail());
            helper.setSubject("File Processing Completed: " + job.getOriginalFilename());
            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendJobFailedNotification(FileJob job) {
        try {
            Context context = new Context();
            context.setVariable("job", job);
            context.setVariable("user", job.getUser());

            String content = templateEngine.process("job-failed", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(job.getUser().getEmail());
            helper.setSubject("File Processing Failed: " + job.getOriginalFilename());
            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
