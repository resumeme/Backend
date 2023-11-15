package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class ComponentController {

    private final ComponentService blockService;

    private final ResumeService resumeService;

    @PostMapping("/{resumeId}/{type}")
    public IdResponse createCareer(@PathVariable Long resumeId, @RequestBody ComponentCreateRequest request, @PathVariable String type) {
        return new IdResponse(blockService.create(request.toEntity().of(resumeId), BlockType.of(type)));
    }

    @GetMapping("/{resumeId}/{type}")
    public List<ComponentResponse> getCareer(@PathVariable Long resumeId, @PathVariable(required = false) String type) {
        resumeService.getOne(resumeId);

        return blockService.getAll(resumeId).stream()
                .filter(component -> component.isType(type))
                .flatMap(component -> component.getComponents().stream())
                .toList().stream()
                .map(component -> BlockType.from(type, component))
                .toList();
    }

    @PatchMapping("/{resumeId}/{type}/components/{componentId}")
    public IdResponse updateComponent(@PathVariable Long resumeId, @RequestBody ComponentCreateRequest request, @PathVariable Long componentId) {
        String originType = blockService.delete(componentId);

        return createCareer(resumeId, request, originType);
    }

    @DeleteMapping("/{resumeId}/components/{componentId}")
    public void deleteComponent(@PathVariable Long componentId) {
        blockService.delete(componentId);
    }

}
