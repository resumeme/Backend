package org.devcourse.resumeme.global.config.security.properties;

import java.util.Objects;

public record Request(String method, String endpoint) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request endpoint1 = (Request) o;
        return Objects.equals(method, endpoint1.method) && Objects.equals(endpoint, endpoint1.endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, endpoint);
    }

}
