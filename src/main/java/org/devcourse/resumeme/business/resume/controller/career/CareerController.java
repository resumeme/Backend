package org.devcourse.resumeme.business.resume.controller.career;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.CareerResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.service.ComponentService;
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

    private final ComponentService blockService;

    @PostMapping("/{resumeId}/{type}")
    public IdResponse createCareer(@PathVariable Long resumeId, @RequestBody ComponentCreateRequest request, @PathVariable String type) {
        return new IdResponse(blockService.create(request.toEntity().of(resumeId), BlockType.of(type)));
    }

    @GetMapping("/{resumeId}/careers")
    public List<CareerResponse> getCareer(@PathVariable Long resumeId) {
        return blockService.getAll(resumeId).stream()
                .filter(component -> component.isType("CAREER"))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> new CareerResponse(Career.from(component)))
                .toList();
    }

}
