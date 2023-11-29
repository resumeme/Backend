package org.devcourse.resumeme.business.user.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.service.vo.RegisterAccountVo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.devcourse.resumeme.global.auth.service.login.OAuth2InfoRedisService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtService jwtService;

    private final OAuth2InfoRedisService oAuth2InfoRedisService;

    public Token registerAccount(RegisterAccountVo accountVo) {
        OAuth2TempInfo oAuth2TempInfo = oAuth2InfoRedisService.getOne(accountVo.cacheKey());
        oAuth2InfoRedisService.delete(oAuth2TempInfo.getId());

        return jwtService.createTokens(accountVo.claims());
    }

    public OAuth2TempInfo getTempInfo(String cacheKey) {
        return oAuth2InfoRedisService.getOne(cacheKey);
    }

}
