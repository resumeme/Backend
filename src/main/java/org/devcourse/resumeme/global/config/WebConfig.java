package org.devcourse.resumeme.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${appConfig.cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Value("${appConfig.cors.corsMapping}")
    private String corsMapping;

    @Value("${appConfig.cors.maxAge}")
    private int maxAge;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping(corsMapping)
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(maxAge);
    }

}
