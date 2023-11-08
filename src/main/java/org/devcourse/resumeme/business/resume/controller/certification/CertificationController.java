package org.devcourse.resumeme.business.resume.controller.certification;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.certification.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationResponse;
import org.devcourse.resumeme.business.resume.domain.Certification;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.devcourse.resumeme.business.resume.domain.BlockType.CERTIFICATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class CertificationController {

    private final ComponentService componentService;

    @PostMapping("/{resumeId}/certifications")
    public IdResponse createCertification(@PathVariable Long resumeId, @RequestBody CertificationCreateRequest request) {
        Certification certification = request.toEntity();

        return new IdResponse(componentService.create(certification.of(resumeId), CERTIFICATION));
    }

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
