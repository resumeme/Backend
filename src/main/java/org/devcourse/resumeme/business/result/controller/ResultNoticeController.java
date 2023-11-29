package org.devcourse.resumeme.business.result.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.result.controller.dto.CreateResultRequest;
import org.devcourse.resumeme.business.result.controller.dto.ResultsResponse;
import org.devcourse.resumeme.business.result.service.ResultService;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResultsResponse getAll(Pageable pageable) {
        return new ResultsResponse(resultService.getAll(pageable));
    }

}
