package org.devcourse.resumeme.common.config;

import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;


@DataRedisTest
@ContextConfiguration(initializers = RedisContainerInitializer.class)
public abstract class RedisRepositoryConfig {

}
