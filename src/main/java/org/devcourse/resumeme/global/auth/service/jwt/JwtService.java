package org.devcourse.resumeme.global.auth.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.jwt.JwtProperties;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private static final String ID = "id";

    private static final String ROLE = "role";

    private static final String BEARER = "Bearer ";

    private final JwtProperties jwtProperties;

    public String createAccessToken(Claims claims) {
        return BEARER + JWT.create()
                .withSubject(jwtProperties.access().headerName())
                .withExpiresAt(new Date(claims.expiration().getTime() + jwtProperties.access().expiration()))
                .withClaim(ID, claims.id())
                .withClaim(ROLE, claims.role())
                .sign(Algorithm.HMAC512(jwtProperties.secretKey()));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return BEARER + JWT.create()
                .withSubject(jwtProperties.refresh().headerName())
                .withExpiresAt(new Date(now.getTime() + jwtProperties.refresh().expiration()))
                .sign(Algorithm.HMAC512(jwtProperties.secretKey()));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, jwtProperties.refresh().headerName());
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractToken(request, jwtProperties.access().headerName());
    }

    private Optional<String> extractToken(HttpServletRequest request, String tokenType) {
        return Optional.ofNullable(request.getHeader(tokenType))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .filter(this::isNotManipulated)
                .map(JwtService::refineToken);
    }


    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(jwtProperties.access().headerName(), accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(jwtProperties.refresh().headerName(), refreshToken);
    }

    public boolean isNotManipulated(String token) {
        try {
            String tokenRefined = refineToken(token);
            JWT.require(Algorithm.HMAC512(jwtProperties.secretKey())).build().verify(tokenRefined);

            return true;
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNotExpired(String token) {
        try {
            String tokenRefined = refineToken(token);
            JWT.require(Algorithm.HMAC512(jwtProperties.secretKey())).build().verify(tokenRefined);

            return true;
        } catch (TokenExpiredException e) {
            return false;
        }
    }

    public Claims extractClaim(String accessToken) {
        String refinedToken = refineToken(accessToken);
        Map<String, Claim> claims = JWT.decode(refinedToken).getClaims();

        Long id = claims.get(ID).asLong();
        String role = claims.get(ROLE).asString();
        Date expiration = claims.get("exp").asDate();

        return new Claims(id, role, expiration);
    }

    public boolean compareTokens(String refreshTokenSaved, String refreshToken) {
        return refineToken(refreshTokenSaved).equals(refreshToken);
    }

    public Token createTokens(Claims claims) {
        Claims claimsForIssueToken = new Claims(claims.id(), claims.role(), new Date());

        return new Token(createAccessToken(claimsForIssueToken), createRefreshToken());
    }

    private static String refineToken(String token) {
        return token.replace(BEARER, "");
    }

}
