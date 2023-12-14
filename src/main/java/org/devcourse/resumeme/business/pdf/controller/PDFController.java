package org.devcourse.resumeme.business.pdf.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.pdf.service.PDFGenerator;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PDFController {

    private final ResumeService resumeService;

    private final JwtService jwtService;

    private final PDFGenerator generator;

    @PostMapping("/pdf/resumes/{resumeId}")
    public ResponseEntity<Resource> createPDF(@CurrentSecurityContext(expression = "authentication") Authentication auth, @PathVariable Long resumeId) throws IOException, InterruptedException {
        JwtUser user = (JwtUser) auth.getPrincipal();
        Optional<String> role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).findAny();

        if (!resumeService.getOne(resumeId).getMenteeId().equals(user.id())) {
            throw new CustomException(ExceptionCode.BAD_REQUEST);
        }

        String accessToken = jwtService.createAccessToken(new Claims( user.id(), role.get(), new Date()));

        Path pdfPath = generator.createPdf(resumeId, accessToken);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String contentType = Files.probeContentType(pdfPath);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, contentType);
        httpHeaders.add("Content-Disposition", "attachment; filename=pdf_" + currentDateTime + ".pdf");

        Resource resource = new InputStreamResource(Files.newInputStream(pdfPath));

        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

}
