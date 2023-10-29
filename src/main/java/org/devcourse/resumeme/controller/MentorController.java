package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.controller.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.repository.OAuth2InfoRedisRepository;
import org.devcourse.resumeme.service.MentorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final JwtService jwtService;

    private final MentorService mentorService;

    private final OAuth2InfoRedisRepository oAuth2InfoRedisRepository;

    @PostMapping
    public Map<String, String> register(@RequestBody MentorRegisterInfoRequest registerInfoRequest) {
        log.debug("registerInfoRequest.cacheKey = {}", registerInfoRequest.cacheKey());
        OAuth2TempInfo oAuth2TempInfo = oAuth2InfoRedisRepository.findById(registerInfoRequest.cacheKey())
                .orElseThrow(() -> new CustomException("REGISTER_FAIL", "회원가입에 실패했습니다."));

        String refreshToken = jwtService.createRefreshToken();
        Mentor mentor = registerInfoRequest.toEntity(oAuth2TempInfo, refreshToken);
        Mentor savedMentor = mentorService.create(mentor);
        String accessToken = jwtService.createAccessToken(Claims.of(savedMentor));

        return Map.of("access", accessToken, "refresh", refreshToken);
    }

}
