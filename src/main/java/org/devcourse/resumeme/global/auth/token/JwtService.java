package org.devcourse.resumeme.global.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.global.auth.model.Claims;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private static final String ACCESS_TOKEN_NAME = "access";

    private static final String REFRESH_TOKEN_NAME = "refresh";

    private static final int ACCESS_TOKEN_EXP = 3600 * 1000;

    private static final int REFRESH_TOKEN_EXP = 3600 * 24 * 7 * 1000;

    private static final String ID = "id";

    private static final String ROLE = "role";

    private static final String BEARER = "Bearer ";

    private static final String SECRET_KEY = "resumeJWT";

    public String createAccessToken(Claims claims) {
        return BEARER + JWT.create()
                .withSubject(ACCESS_TOKEN_NAME)
                .withExpiresAt(new Date(claims.expiration().getTime() + ACCESS_TOKEN_EXP))
                .withClaim(ID, claims.id())
                .withClaim(ROLE, claims.role())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return BEARER + JWT.create()
                .withSubject(REFRESH_TOKEN_NAME)
                .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXP))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_NAME))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_NAME))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_TOKEN_NAME, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_TOKEN_NAME, refreshToken);
    }

    public boolean validate(String token) {
        try {
            String tokenRefined = token.replace(BEARER, "");
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(tokenRefined);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims extractClaim(String accessToken) {
        String refinedToken = accessToken.replace("Bearer ", "");
        Map<String, Claim> claims = JWT.decode(refinedToken).getClaims();

        Long id = claims.get(ID).asLong();
        String role = claims.get(ROLE).asString();
        Date expiration = claims.get("exp").asDate();

        return new Claims(id, role, expiration);
    }

    public boolean compareTokens(String refreshTokenSaved, String refreshToken) {
        return refreshTokenSaved.equals(refreshToken);
    }

    public Map<String, String> createAndSendNewTokens(Claims claims, HttpServletResponse response) {
        String accessToken = createAccessToken(new Claims(claims.id(), claims.role(), new Date()));
        String refreshToken = createRefreshToken();
        sendAccessAndRefreshToken(response, accessToken, refreshToken);
        return Map.of("access", accessToken, "refresh", refreshToken);
    }

}
