package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
import org.devcourse.resumeme.business.resume.domain.Training;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.devcourse.resumeme.business.resume.domain.BlockType.TRAINING;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class TrainingController {

    private final ComponentService componentService;

    @PostMapping("/{resumeId}/trainings")
    public IdResponse createTraining(@PathVariable Long resumeId, @RequestBody TrainingCreateRequest request) {
        Training training = request.toEntity();

        return new IdResponse(componentService.create(training.of(resumeId), TRAINING));
    }

    @GetMapping("/{resumeId}/trainings")
    public List<TrainingResponse> getTraining(@PathVariable Long resumeId) {
        return componentService.getAll(resumeId).stream()
                .filter(component -> component.isType("TRAINING"))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> new TrainingResponse(Training.from(component)))
                .toList();
    }

}
