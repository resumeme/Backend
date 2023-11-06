package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.CareerResponse;
import org.devcourse.resumeme.business.resume.domain.Career;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.CareerService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
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
public class CareerController {

    private final CareerService careerService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/careers")
    public IdResponse createCareer(@PathVariable Long resumeId, @RequestBody CareerCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Career career = request.toEntity(resume);

        return new IdResponse(careerService.create(career));
    }

    @GetMapping("/{resumeId}/careers")
    public List<CareerResponse> getCareer(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        List<Career> careers = resume.getCareer();

        return careers.stream()
                .map(CareerResponse::new)
                .toList();
    }

}