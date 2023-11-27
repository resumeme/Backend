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
    public IdResponse createComponent(@PathVariable Long resumeId, @RequestBody ComponentCreateRequest request, @PathVariable Property type) {
        return new IdResponse(blockService.create(request.toEntity().toComponent(resumeId), type));
    }

    @GetMapping({"/{resumeId}", "/{resumeId}/{type}"})
    public AllComponentResponse getComponent(@PathVariable Long resumeId, @PathVariable(required = false) Property type) {
        List<Converter> all = blockService.getAll(resumeId, type);

        for (List<ComponentResponse> responses : result.values()) {
            responses.sort(comparing(ComponentResponse::getCreatedDate));
        }

        return result;
    }

    @PatchMapping("/{resumeId}/{type}/components/{componentId}")
    public IdResponse updateComponent(@PathVariable Long resumeId, @PathVariable Property type, @RequestBody ComponentCreateRequest request, @PathVariable Long componentId) {
        return new IdResponse(blockService.update(componentId, request.toEntity().toComponent(resumeId), type));
    }

    @DeleteMapping("/{resumeId}/components/{componentId}")
    public void deleteComponent(@PathVariable Long componentId) {
        blockService.delete(componentId);
    }

}
