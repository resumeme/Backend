package org.devcourse.resumeme.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("endpoint")
public class EndpointProperties {

    @Getter
    private Map<String, List<String>> permitAll;

    @Getter
    private List<Matcher> roles;

    public EndpointProperties(Map<String, List<String>> permitAll, List<Matcher> roles) {
        this.permitAll = permitAll;
        this.roles = roles;
    }

    public static class Matcher {

        @Getter
        private Map<String, List<String>> matcher;

        @Getter
        private List<String> role;

        public Matcher(Map<String, List<String>> matcher, List<String> role) {
            this.matcher = matcher;
            this.role = role;
        }

    }
}
