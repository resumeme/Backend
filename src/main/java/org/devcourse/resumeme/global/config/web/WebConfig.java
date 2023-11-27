package org.devcourse.resumeme.global.config.web;

import org.devcourse.resumeme.business.resume.domain.Property;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = "org.devcourse.resumeme.global")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PropertyConverter());
    }

    static class PropertyConverter implements Converter<String, Property> {

        @Override
        public Property convert(String source) {
            return Property.valueOf(source.toUpperCase());
        }

    }

}
