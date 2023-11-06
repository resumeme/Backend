package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.controller.dto.MentorInfoResponse;
import org.devcourse.resumeme.controller.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.controller.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.service.MentorService;
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
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final JwtService jwtService;

    private final MentorService mentorService;

    private final OAuth2InfoRedisService oAuth2InfoRedisService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody MentorRegisterInfoRequest registerInfoRequest) {
        log.debug("registerInfoRequest.cacheKey = {}", registerInfoRequest.cacheKey());
        OAuth2TempInfo oAuth2TempInfo = oAuth2InfoRedisService.getOne(registerInfoRequest.cacheKey());

        String refreshToken = jwtService.createRefreshToken();
        Mentor mentor = registerInfoRequest.toEntity(oAuth2TempInfo, refreshToken);
        Mentor savedMentor = mentorService.create(mentor);
        String accessToken = jwtService.createAccessToken(Claims.of(savedMentor));
        oAuth2InfoRedisService.delete(oAuth2TempInfo.getId());

        return ResponseEntity.status(200)
                .header("access", accessToken)
                .header("refresh", refreshToken)
                .build();
    }

    @GetMapping("/{mentorId}")
    public MentorInfoResponse getOne(@PathVariable Long mentorId) {
        Mentor findMentor = mentorService.getOne(mentorId);

        return new MentorInfoResponse(findMentor);
    }

    @PatchMapping("/{mentorId}")
    public IdResponse update(@PathVariable Long mentorId, @RequestBody MentorInfoUpdateRequest mentorInfoUpdateRequest) {
        Long updatedMentorId = mentorService.update(mentorId, mentorInfoUpdateRequest);

        return new IdResponse(updatedMentorId);
    }

}
