package com.spring.boot.excel.demo;

import com.spring.boot.excel.demo.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.spring.boot.excel.demo.QuotationDataSupplier.loadQuoteData;

@Slf4j
@RequiredArgsConstructor
@Controller
public class QuotationReportController {

    private final QuotationReportService quotationReportService;
    private final EmailService emailService;

    @GetMapping("/quotation/export")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=quotation.xlsx");
        ByteArrayOutputStream outputStream = quotationReportService.generateQuotation(loadQuoteData(), false);
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
    }

    @GetMapping("/quotation/mail")
    public String sendMail() {
        ByteArrayOutputStream outputStream = quotationReportService.generateQuotation(loadQuoteData(), false);
        return send(outputStream);
    }

    private String send(ByteArrayOutputStream outputStream) {
        InputStreamSource source = new ByteArrayResource(outputStream.toByteArray());
        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("jayaprakashc17@yahoo.com");
        emailDto.setTo("jayaprakashc17@yahoo.com");
        emailDto.setSubject("Sending mail using sendinblue mail server");
        Map<String, String> model = new HashMap<>();
        model.put("name", "X");
        model.put("value", "Welcome...");
        return emailService.sendMail(emailDto, model, source);
    }
}
