package org.devcourse.resumeme.business.user.controller.mentee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeInfoResponse;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.devcourse.resumeme.global.auth.service.login.OAuth2InfoRedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.devcourse.resumeme.global.auth.service.jwt.Token.*;

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

        Mentee mentee = registerInfoRequest.toEntity(oAuth2TempInfo);
        Mentee savedMentee = menteeService.create(mentee);
        Token token = jwtService.createTokens(Claims.of(savedMentee));
        menteeService.updateRefreshToken(savedMentee.getId(), token.refreshToken());
        oAuth2InfoRedisService.delete(oAuth2TempInfo.getId());

        return ResponseEntity.status(200)
                .header(ACCESS_TOKEN_NAME, token.accessToken())
                .header(REFRESH_TOKEN_NAME, token.refreshToken())
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
