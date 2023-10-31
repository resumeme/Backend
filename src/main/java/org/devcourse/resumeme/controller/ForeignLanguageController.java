package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.ForeignLanguageRequestDto;
import org.devcourse.resumeme.domain.resume.ForeignLanguage;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.ForeignLanguageService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ForeignLanguageController {

    private final ForeignLanguageService foreignLanguageService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/foreign-languages")
    public IdResponse createForeignLanguage(@PathVariable Long resumeId, @RequestBody ForeignLanguageRequestDto request) {
        Resume resume = resumeService.getOne(resumeId);
        ForeignLanguage foreignLanguage = request.toEntity(resume);

        return new IdResponse(foreignLanguageService.create(foreignLanguage));
    }
}

