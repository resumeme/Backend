package org.devcourse.resumeme.global.config.web;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = "org.devcourse.resumeme.global")
public class WebConfig {

}
