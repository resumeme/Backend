package org.devcourse.resumeme.global.auth.filter.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final MentorService mentorService;

    private final MenteeService menteeService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            JwtUser user = (JwtUser) authentication.getPrincipal();
            if (isMentee(authentication)) {
                menteeService.deleteRefreshToken(user.id());
            }
            mentorService.deleteRefreshToken(user.id());
        }
    }

    private boolean isMentee(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_MENTEE"));
    }

}
