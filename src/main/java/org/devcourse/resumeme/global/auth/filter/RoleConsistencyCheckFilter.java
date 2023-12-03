package org.devcourse.resumeme.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.devcourse.resumeme.global.exception.ChangeMentorRoleException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTOR;
import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTOR_ALREADY_APPROVED;

@RequiredArgsConstructor
public class RoleConsistencyCheckFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && isRoleInconsistent(auth)) {
            throw new ChangeMentorRoleException(MENTOR_ALREADY_APPROVED);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRoleInconsistent(Authentication auth) {
        if (!(auth.getPrincipal() instanceof JwtUser)) {
            return false;
        }

        JwtUser user = (JwtUser) auth.getPrincipal();

        User findUser = userService.getOne(user.id());
        return isPending(auth) && Mentor.of(findUser).getRole().equals(ROLE_MENTOR);
    }

    private boolean isPending(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_PENDING"));
    }


}
