package org.devcourse.resumeme.business.resume.controller.certification;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationResponse;
import org.devcourse.resumeme.business.resume.domain.Certification;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class CertificationController {

    private final ComponentService componentService;

    @GetMapping("/{resumeId}/certifications")
    public List<CertificationResponse> createCertification(@PathVariable Long resumeId) {
        return componentService.getAll(resumeId).stream()
                .filter(component -> component.isType("CERTIFICATION"))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> new CertificationResponse(Certification.from(component)))
                .toList();
    }

}
