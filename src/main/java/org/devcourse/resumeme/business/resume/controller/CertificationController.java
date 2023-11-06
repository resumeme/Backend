package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Certification;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.CertificationService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class CertificationController {

    private final CertificationService certificationService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/certifications")
    public IdResponse createCertification(@PathVariable Long resumeId, @RequestBody CertificationCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Certification certification = request.toEntity(resume);

        return new IdResponse(certificationService.create(certification));
    }

    @GetMapping("/{resumeId}/certifications")
    public List<CertificationResponse> createCertification(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        List<Certification> certifications = resume.getCertification();

        return certifications.stream()
                .map(CertificationResponse::new)
                .toList();
    }

}