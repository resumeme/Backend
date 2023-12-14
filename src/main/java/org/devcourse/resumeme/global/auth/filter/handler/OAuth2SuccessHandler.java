package org.devcourse.resumeme.global.auth.filter.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.global.auth.model.UserCommonInfo;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.login.OAuth2CustomUser;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            OAuth2CustomUser oAuth2CustomUser = (OAuth2CustomUser) authentication.getPrincipal();
            UserCommonInfo commonInfo = oAuth2CustomUser.getUserCommonInfo();
            String accessToken = jwtService.createAccessToken(Claims.of(commonInfo));
            String refreshToken = jwtService.createRefreshToken();

            userService.updateRefreshToken(commonInfo.id(), refreshToken);

            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        } catch (Exception e) {
            throw new CustomException("FAIL_OAUTH_LOGIN", "로그인에 실패했습니다.");
        }
    }

}
