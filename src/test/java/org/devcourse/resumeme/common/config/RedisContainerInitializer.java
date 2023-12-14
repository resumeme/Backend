package org.devcourse.resumeme.common.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class RedisContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("redis:6.0.1"))
                .withEnv("TZ", "Asia/Seoul")
                .withExposedPorts(6379);

        container.start();

        Map<String, String> properties = Map.of(
                "spring.data.redis.host", container.getHost(),
                "spring.data.redis.port", String.valueOf(container.getFirstMappedPort())
        );

        TestPropertyValues.of(properties).applyTo(context.getEnvironment());
    }

}
