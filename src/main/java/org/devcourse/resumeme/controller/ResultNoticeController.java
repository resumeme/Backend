package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.CreateResultRequest;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.service.ResultService;
import org.devcourse.resumeme.service.ResumeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/results")
public class ResultNoticeController {

    private final ResultService resultService;

    private final ResumeService resumeService;

    @PostMapping
    public IdResponse create(@RequestBody CreateResultRequest request) {
        Resume resume = resumeService.getOne(request.resumeId());

        return new IdResponse(resultService.create(request.toEntity(resume)));
    }

}
