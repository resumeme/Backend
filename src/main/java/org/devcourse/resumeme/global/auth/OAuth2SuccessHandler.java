package org.devcourse.resumeme.global.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.devcourse.resumeme.domain.user.Role;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            if (principalDetails.getUser().getRole() == Role.ROLE_GUEST) {
                response.setHeader("id", principalDetails.getUser().getId().toString());
                response.sendRedirect("/primary-info-form");
            }

        } catch (Exception e) {
            throw e;
        }
    }

}
