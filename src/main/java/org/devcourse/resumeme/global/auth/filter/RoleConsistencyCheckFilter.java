package org.devcourse.resumeme.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTOR;

@RequiredArgsConstructor
public class RoleConsistencyCheckFilter extends OncePerRequestFilter {

    private final MentorService mentorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && isRoleInconsistent(auth)) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRoleInconsistent(Authentication auth) {
        if (!(auth.getPrincipal() instanceof JwtUser)) {
            return false;
        }

        JwtUser user = (JwtUser) auth.getPrincipal();

        return isPending(auth) && mentorService.getOne(user.id()).getRole().equals(ROLE_MENTOR);
    }

    private boolean isPending(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_PENDING"));
    }


}
