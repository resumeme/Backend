package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.resume.Training;
import org.devcourse.resumeme.service.ResumeService;
import org.devcourse.resumeme.service.TrainingService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class TrainingController {

    private final TrainingService trainingService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/trainings")
    public IdResponse createTraining(@PathVariable Long resumeId, @RequestBody TrainingCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Training training = request.toEntity(resume);

        return new IdResponse(trainingService.create(training));
    }

}
