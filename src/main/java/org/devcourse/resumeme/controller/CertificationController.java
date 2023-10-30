package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.CertificationCreateRequest;
import org.devcourse.resumeme.domain.resume.Certification;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.CertificationService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
