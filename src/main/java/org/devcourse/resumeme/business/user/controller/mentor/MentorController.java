package org.devcourse.resumeme.business.user.controller.mentor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoResponse;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.AccountService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.business.user.service.vo.RegisterAccountVo;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.devcourse.resumeme.global.auth.service.jwt.Token.ACCESS_TOKEN_NAME;
import static org.devcourse.resumeme.global.auth.service.jwt.Token.REFRESH_TOKEN_NAME;

@Slf4j
@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody MentorRegisterInfoRequest registerInfoRequest) {
        String cacheKey = registerInfoRequest.cacheKey();
        log.info("registerInfoRequest.cacheKey = {}", cacheKey);

        OAuth2TempInfo oAuth2TempInfo = accountService.getTempInfo(cacheKey);
        Mentor mentor = registerInfoRequest.toEntity(oAuth2TempInfo);
        Mentor savedMentor = mentorService.create(mentor);

        Token token = getToken(cacheKey, savedMentor);
        mentorService.updateRefreshToken(savedMentor.getId(), token.refreshToken());

        return ResponseEntity.status(200)
                .header(ACCESS_TOKEN_NAME, token.accessToken())
                .header(REFRESH_TOKEN_NAME, token.refreshToken())
                .build();
    }

    private Token getToken(String cacheKey, Mentor savedMentor) {
        RegisterAccountVo accountVo = new RegisterAccountVo(cacheKey, Claims.of(savedMentor));

        return accountService.registerAccount(accountVo);
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
