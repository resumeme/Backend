package org.devcourse.resumeme.business.resume.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;
import org.devcourse.resumeme.business.resume.controller.dto.v2.ComponentCreateRequestV2;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTemplate;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resumes")
public class ComponentController {

    private final ComponentService componentService;

    @PostMapping("/{resumeId}/{type}")
    public IdResponse createComponent(@PathVariable Long resumeId, @RequestBody ComponentCreateRequestV2 request, @PathVariable Property type) {
        return new IdResponse(componentService.create(request.toVo(type).toComponent(resumeId), type));
    }

    @GetMapping({"/{resumeId}", "/{resumeId}/{type}"})
    public AllComponentResponse getComponent(@PathVariable Long resumeId, @PathVariable(required = false) Property type) {
        ResumeTemplate template = componentService.getAll(resumeId, type);

        return AllComponentResponse.from(template);
    }

    @PatchMapping("/{resumeId}/{type}/components/{componentId}")
    public IdResponse updateComponent(@PathVariable Long resumeId, @PathVariable Property type, @RequestBody ComponentCreateRequestV2 request, @PathVariable Long componentId) {
        return new IdResponse(componentService.update(componentId, request.toVo(type).toComponent(resumeId), type));
    }

    @DeleteMapping("/{resumeId}/components/{componentId}")
    public void deleteComponent(@PathVariable Long componentId) {
        componentService.delete(componentId);
    }

}
