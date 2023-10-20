package org.devcourse.resumeme.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("endpoint")
public class EndpointProperties {

    @Getter
    private List<String> permitAll;

    @Getter
    private Map<String, List<String>> roles;

    public EndpointProperties(List<String> permitAll, Map<String, List<String>> roles) {
        this.permitAll = permitAll;
        this.roles = roles;
    }

}
