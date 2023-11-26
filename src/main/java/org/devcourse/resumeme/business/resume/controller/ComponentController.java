package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumes")
public class ComponentController {

    private final ComponentService blockService;

    @PostMapping("/{resumeId}/{type}")
    public IdResponse createComponent(@PathVariable Long resumeId, @RequestBody ComponentCreateRequest request, @PathVariable String type) {
        return new IdResponse(blockService.create(request.toEntity().of(resumeId), type));
    }

    @GetMapping({"/{resumeId}", "/{resumeId}/{type}"})
    public Map<String, List<ComponentResponse>> getComponent(@PathVariable Long resumeId, @PathVariable(required = false) String type) {
        Map<String, List<ComponentResponse>> result = new HashMap<>();

        List<Component> components = blockService.getAll(resumeId);
        for (Component component : components) {
            if (component.isType(type)) {
                for (Component subComponent : component.getComponents()) {
                    List<ComponentResponse> response = result.getOrDefault(component.getProperty(), new ArrayList<>());
                    response.add(BlockType.convert(component.getProperty(), subComponent));
                    result.put(component.getProperty(), response);
                }
            }
        }

        for (List<ComponentResponse> responses : result.values()) {
            responses.sort(comparing(ComponentResponse::getCreatedDate));
        }

        return result;
    }

    @PatchMapping("/{resumeId}/{type}/components/{componentId}")
    public IdResponse updateComponent(@PathVariable Long resumeId, @PathVariable String type, @RequestBody ComponentCreateRequest request, @PathVariable Long componentId) {
        return new IdResponse(blockService.update(componentId, request.toEntity().of(resumeId), type));
    }

    @DeleteMapping("/{resumeId}/components/{componentId}")
    public void deleteComponent(@PathVariable Long componentId) {
        blockService.delete(componentId);
    }

}
