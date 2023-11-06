package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.Training;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.resume.service.TrainingService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
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
public class TrainingController {

    private final TrainingService trainingService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/trainings")
    public IdResponse createTraining(@PathVariable Long resumeId, @RequestBody TrainingCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Training training = request.toEntity(resume);

        return new IdResponse(trainingService.create(training));
    }

    @GetMapping("/{resumeId}/trainings")
    public List<TrainingResponse> getTraining(@PathVariable Long resumeId) {
        Resume resume = resumeService.getOne(resumeId);
        List<Training> trainings = resume.getTraining();

        return trainings.stream()
                .map(TrainingResponse::new)
                .toList();
    }

}
