package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.ResumeCreateRequest;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private final ResumeService resumeService;


    @PostMapping()
    public Long createResume(
            // jwt 유저 파라미터 추가 예정
            @RequestBody ResumeCreateRequest request) {
        Resume resume = request.toEntity();
        Long savedId = resumeService.create(resume);

        return savedId;
    }

}
