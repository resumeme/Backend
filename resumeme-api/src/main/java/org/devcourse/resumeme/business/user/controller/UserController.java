package org.devcourse.resumeme.business.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.dto.UserInfoResponse;
import org.devcourse.resumeme.business.user.controller.dto.UserInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.dto.UserRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.business.user.service.AccountService;
import org.devcourse.resumeme.business.user.service.vo.CreatedUserVo;
import org.devcourse.resumeme.business.user.service.vo.RegisterAccountVo;
import org.devcourse.resumeme.business.user.service.vo.UserDomainVo;
import org.devcourse.resumeme.business.user.service.vo.UserInfoVo;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.devcourse.resumeme.global.auth.service.jwt.Token.ACCESS_TOKEN_NAME;
import static org.devcourse.resumeme.global.auth.service.jwt.Token.REFRESH_TOKEN_NAME;
import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private final AccountService accountService;

    @PostMapping("/{role}")
    public ResponseEntity<Void> register(@PathVariable Role role, @RequestBody UserRegisterInfoRequest registerInfoRequest) {
        String cacheKey = registerInfoRequest.cacheKey();
        log.info("registerInfoRequest.cacheKey = {}", cacheKey);

        OAuth2TempInfo oAuth2TempInfo = accountService.getTempInfo(cacheKey);
        UserDomainVo userVo = registerInfoRequest.toVo(role, oAuth2TempInfo);
        CreatedUserVo createdUserVo = userService.create(userVo.toUser());

        Claims claim = createdUserVo.toClaim();
        Token token = getToken(cacheKey, claim);
        userService.updateRefreshToken(claim.id(), token.refreshToken());

        return ResponseEntity.status(200)
                .header(ACCESS_TOKEN_NAME, token.accessToken())
                .header(REFRESH_TOKEN_NAME, token.refreshToken())
                .build();
    }

    private Token getToken(String cacheKey, Claims claims) {
        RegisterAccountVo accountVo = new RegisterAccountVo(cacheKey, claims);

        return accountService.registerAccount(accountVo);
    }

    @GetMapping("/user")
    public UserInfoResponse getMyInfo(@CurrentSecurityContext(expression = "authentication") Authentication auth) {
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        Role role = getRole(auth);

        return getOne(role, jwtUser.id());
    }

    private Role getRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
    }

    @PatchMapping("/{role}/{userId}")
    public IdResponse update(@PathVariable Long userId, @RequestBody UserInfoUpdateRequest request) {
        Long updatedUserId = userService.update(userId, request.toVo());

        return new IdResponse(updatedUserId);
    }

    @GetMapping("/{role}/{userId}")
    public UserInfoResponse getOne(@PathVariable Role role, @PathVariable Long userId) {
        UserInfoVo userInfoVo = userService.getOne(role, userId);

        return UserInfoResponse.of(userInfoVo);
    }

}
