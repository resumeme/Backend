package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageResponse;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.devcourse.resumeme.business.resume.domain.BlockType.FOREIGN_LANGUAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ForeignLanguageController {

    private final ComponentService componentService;

    @PostMapping("/{resumeId}/foreign-languages")
    public IdResponse createForeignLanguage(@PathVariable Long resumeId, @RequestBody ForeignLanguageCreateRequest request) {
        ForeignLanguage foreignLanguage = request.toEntity();

        return new IdResponse(componentService.create(foreignLanguage.of(resumeId), FOREIGN_LANGUAGE));
    }

    @GetMapping("/{resumeId}/foreign-languages")
    public List<ForeignLanguageResponse> getForeignLanguages(@PathVariable Long resumeId) {
        return componentService.getAll(resumeId).stream()
                .filter(component -> component.isType("FOREIGN_LANGUAGE"))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> new ForeignLanguageResponse(ForeignLanguage.from(component)))
                .toList();
    }

}
