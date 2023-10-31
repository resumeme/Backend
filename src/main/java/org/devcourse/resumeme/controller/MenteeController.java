package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.controller.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.controller.dto.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.JwtUser;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.repository.OAuth2InfoRedisRepository;
import org.devcourse.resumeme.service.MenteeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentees")
public class MenteeController {

    private final JwtService jwtService;

    private final MenteeService menteeService;

    private final OAuth2InfoRedisRepository oAuth2InfoRedisRepository;

    @PostMapping
    public Map<String, String> register(@RequestBody MenteeRegisterInfoRequest registerInfoRequest) {
        log.debug("registerInfoRequest.cacheKey = {}", registerInfoRequest.cacheKey());
        OAuth2TempInfo oAuth2TempInfo = oAuth2InfoRedisRepository.findById(registerInfoRequest.cacheKey())
                .orElseThrow(() -> new CustomException("REGISTER_FAIL", "회원가입에 실패했습니다."));

        String refreshToken = jwtService.createRefreshToken();
        Mentee mentee = registerInfoRequest.toEntity(oAuth2TempInfo, refreshToken);
        Mentee savedMentee = menteeService.create(mentee);
        String accessToken = jwtService.createAccessToken(Claims.of(savedMentee));
        oAuth2InfoRedisRepository.delete(oAuth2TempInfo);

        return Map.of("access", accessToken, "refresh", refreshToken);
    }

    @PatchMapping("/{menteeId}")
    public void update(@PathVariable Long menteeId, @RequestBody MenteeInfoUpdateRequest updateRequest, @AuthenticationPrincipal JwtUser user) {
        menteeService.update(user.id(), updateRequest);
    }

}
