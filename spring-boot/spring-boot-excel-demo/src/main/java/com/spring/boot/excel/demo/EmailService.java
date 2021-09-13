package com.spring.boot.excel.demo;

import com.spring.boot.excel.demo.dto.EmailDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final Configuration configuration;

    public String sendMail(EmailDto emailDto, Map<String, String> model, InputStreamSource source) {
        String response;
        MimeMessage message = mailSender.createMimeMessage();
        try {

            Template template = configuration.getTemplate("email.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            ClassPathResource image = new ClassPathResource("images/logo.png");

            MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
            helper.setTo(emailDto.getTo());
            helper.setFrom(emailDto.getFrom());
            helper.setSubject(emailDto.getSubject());
            helper.setText(html, true);
            helper.addInline("quotation", image);
            helper.addAttachment("quotation.xlsx", source);

            mailSender.send(message);
            response = "Email has been sent to :" + emailDto.getTo();

        } catch (MessagingException | TemplateException | IOException e) {
            response = "Email send failure to :" + emailDto.getTo();
        }
        return response;
    }
}
