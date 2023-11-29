package org.devcourse.resumeme.global.config.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("endpoint")
public class EndpointProperties {

    private final List<Matcher> matchers;

    private final List<String> ignores;

    public EndpointProperties(List<PropertyMatcher> matchers, List<String> ignores) {
        this.matchers = getMatchers(matchers);
        this.ignores = ignores;
    }

    private static List<Matcher> getMatchers(List<PropertyMatcher> matchers) {
        return matchers.stream()
                .flatMap(matcher -> matcher.toMatcher().stream())
                .toList();
    }

    public List<Matcher> matchers() {
        return matchers;
    }

    public List<String> ignores() {
        return ignores;
    }

    private static class PropertyMatcher {

        private List<String> get;

        private List<String> post;

        private List<String> put;

        private List<String> patch;

        private List<String> delete;

        private String role;

        public PropertyMatcher(List<String> get, List<String> post, List<String> put, List<String> patch, List<String> delete, String role) {
            this.get = get;
            this.post = post;
            this.put = put;
            this.patch = patch;
            this.delete = delete;
            this.role = role;
        }

        public List<Matcher> toMatcher() {
            List<Matcher> result = new ArrayList<>();
            result.addAll(getMatchers(get, "GET"));
            result.addAll(getMatchers(post, "POST"));
            result.addAll(getMatchers(put, "PUT"));
            result.addAll(getMatchers(patch, "PATCH"));
            result.addAll(getMatchers(delete, "DELETE"));

            return result;
        }

        private List<Matcher> getMatchers(List<String> endpoints, String method) {
            Map<Request, List<String>> matchers = new LinkedHashMap<>();
            if (endpoints != null) {
                for (String endpoint : endpoints) {
                    Request request = new Request(method, endpoint);
                    List<String> roles = matchers.getOrDefault(request, new ArrayList<>());
                    roles.add(role.toUpperCase());

                    matchers.put(request, roles);
                }
            }

            return matchers.entrySet().stream()
                    .map(entry -> new Matcher(entry.getKey(), entry.getValue()))
                    .toList();
        }

    }

}
