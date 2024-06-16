package org.devcourse.resumeme.global.auth.filter.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            JwtUser user = (JwtUser) authentication.getPrincipal();
            userService.deleteRefreshToken(user.id());
        }
    }

}
