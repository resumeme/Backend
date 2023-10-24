package org.devcourse.resumeme.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            OAuth2CustomUser oAuth2CustomUser = (OAuth2CustomUser) authentication.getPrincipal();

            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            if (oAuth2CustomUser.getUser().getRole() == Role.ROLE_GUEST) {
                response.setHeader("id", oAuth2CustomUser.getUser().getId().toString());
                response.sendRedirect("/primary-info-form");
            }

        } catch (Exception e) {
            throw new CustomException("FAIL_OAUTH_LOGIN", "소셜 로그인에 실패했습니다.");
        }
    }

}
