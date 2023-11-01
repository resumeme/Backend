package org.devcourse.resumeme.common.controller;

import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.event.EventStatus;
import org.devcourse.resumeme.domain.event.Progress;
import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;
import org.devcourse.resumeme.domain.user.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EnumController {

    @GetMapping("/enums")
    public EnumDocsResponse enums() {
        return new EnumDocsResponse(
                to(Position.values()),
                to(EventStatus.values()),
                to(Progress.values()),
                to(BlockType.values()),
                to(Provider.values()),
                to(Role.values()),
                to(ExceptionCode.values())
        );
    }

    private Map<String, String> to(DocsEnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .collect(Collectors.toMap(DocsEnumType::getType, DocsEnumType::getDescription));
    }

    public record EnumDocsResponse(Map<String, String> position, Map<String, String> eventStatus,
                                   Map<String, String> progress, Map<String, String> blockType,
                                   Map<String, String> provider, Map<String, String> role,
                                   Map<String, String> exceptionCode) {

    }

}
