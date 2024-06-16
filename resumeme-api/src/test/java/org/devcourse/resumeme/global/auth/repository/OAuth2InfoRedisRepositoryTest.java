package org.devcourse.resumeme.global.auth.repository;

import org.devcourse.resumeme.common.config.RedisRepositoryConfig;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2InfoRedisRepositoryTest extends RedisRepositoryConfig {

    @Autowired
    private OAuth2InfoRedisRepository repository;

    @Test
    void id를_null값으로_redis_저장후_id값_랜덤하게_생성된다() {
        OAuth2TempInfo savedInfo = repository.save(new OAuth2TempInfo(null, "KAKAO", "nickname", "validformat12@gmail.com", "url"));

        assertThat(savedInfo.getId()).isNotNull();
    }

}
