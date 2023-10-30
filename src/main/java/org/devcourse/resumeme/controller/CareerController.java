package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.CareerCreateRequest;
import org.devcourse.resumeme.domain.resume.Career;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.CareerService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/careers")
public class CareerController {

    private final CareerService careerService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}")
    public IdResponse createCareer(@PathVariable Long resumeId, @RequestBody CareerCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Career career = request.toEntity(resume);

        return new IdResponse(careerService.create(career));
    }

}
