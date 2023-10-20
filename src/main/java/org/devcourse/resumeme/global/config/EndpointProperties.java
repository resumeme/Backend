package org.devcourse.resumeme.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("endpoint")
public class EndpointProperties {

    @Getter
    private List<String> permitAll;

    public EndpointProperties(List<String> permitAll) {
        this.permitAll = permitAll;
    }

}
