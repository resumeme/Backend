package org.devcourse.resumeme.global.config.web;

import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.snapshot.entity.SnapshotType;
import org.devcourse.resumeme.business.user.domain.Role;
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
        registry.addConverter(new SnapshotTypeConverter());
        registry.addConverter(new RoleConverter());
    }

    static class PropertyConverter implements Converter<String, Property> {

        @Override
        public Property convert(String source) {
            return Property.valueOf(source.toUpperCase());
        }

    }

    static class SnapshotTypeConverter implements Converter<String, SnapshotType> {

        @Override
        public SnapshotType convert(String source) {
            return SnapshotType.valueOf(source.toUpperCase());
        }

    }

    static class RoleConverter implements Converter<String, Role> {

        @Override
        public Role convert(String source) {
            if (source.equals("mentors")) {
                return Role.ROLE_MENTOR;
            }

            return Role.ROLE_MENTEE;
        }

    }

}
