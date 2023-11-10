package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
import org.devcourse.resumeme.business.resume.domain.Training;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class TrainingController {

    private final ComponentService componentService;

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
