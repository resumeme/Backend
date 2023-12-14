package org.devcourse.resumeme.global.auth.service.login;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.repository.OAuth2InfoRedisRepository;
import org.springframework.stereotype.Service;

import static org.devcourse.resumeme.global.exception.ExceptionCode.INFO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OAuth2InfoRedisService {

    private final OAuth2InfoRedisRepository redisRepository;

    public String create(OAuth2TempInfo oAuth2TempInfo) {
        OAuth2TempInfo savedTempInfo = redisRepository.save(oAuth2TempInfo);

        return savedTempInfo.getId();
    }

    public OAuth2TempInfo getOne(String id) {
        return redisRepository.findById(id)
                .orElseThrow(() -> new CustomException(INFO_NOT_FOUND));
    }

    public void delete(String id) {
        OAuth2TempInfo tempInfo = getOne(id);
        redisRepository.delete(tempInfo);
    }

}
