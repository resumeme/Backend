package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.MenteeInfoResponse;
import org.devcourse.resumeme.controller.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.controller.dto.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.OAuth2InfoRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentees")
public class MenteeController {

    private final JwtService jwtService;

    private final MenteeService menteeService;

    private final OAuth2InfoRedisService oAuth2InfoRedisService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody MenteeRegisterInfoRequest registerInfoRequest) {
        log.debug("registerInfoRequest.cacheKey = {}", registerInfoRequest.cacheKey());
        OAuth2TempInfo oAuth2TempInfo = oAuth2InfoRedisService.getOne(registerInfoRequest.cacheKey());

        String refreshToken = jwtService.createRefreshToken();
        Mentee mentee = registerInfoRequest.toEntity(oAuth2TempInfo, refreshToken);
        Mentee savedMentee = menteeService.create(mentee);
        String accessToken = jwtService.createAccessToken(Claims.of(savedMentee));
        oAuth2InfoRedisService.delete(oAuth2TempInfo.getId());

        return ResponseEntity.status(200)
                .header("access", accessToken)
                .header("refresh", refreshToken)
                .build();
    }

    @PatchMapping("/{menteeId}")
    public IdResponse update(@PathVariable Long menteeId, @RequestBody MenteeInfoUpdateRequest updateRequest) {
        Long updatedMenteeId = menteeService.update(menteeId, updateRequest);

        return new IdResponse(updatedMenteeId);
    }

    @GetMapping("/{menteeId}")
    public MenteeInfoResponse getOne(@PathVariable Long menteeId) {
        Mentee findMentee = menteeService.getOne(menteeId);

        return new MenteeInfoResponse(findMentee);
    }

}
