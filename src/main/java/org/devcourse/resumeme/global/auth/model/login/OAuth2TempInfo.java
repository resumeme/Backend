package org.devcourse.resumeme.global.auth.model.login;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "userInfo", timeToLive = 300)
public class OAuth2TempInfo {

    private final String id;
    private final String provider;
    private final String nickname;
    private final String email;
    private final String imageUrl;

    public OAuth2TempInfo(String id, String provider, String nickname, String email, String imageUrl) {
        this.id = id;
        this.provider = provider;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
    }

}
