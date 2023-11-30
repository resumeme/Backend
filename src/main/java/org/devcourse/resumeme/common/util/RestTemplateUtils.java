package org.devcourse.resumeme.common.util;

import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static lombok.AccessLevel.PRIVATE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.SERVER_ERROR;

@NoArgsConstructor(access = PRIVATE)
public class RestTemplateUtils {

    private static final RestTemplate TEMPLATE = new RestTemplate();

    public static <T> T getLocalResponse(String uri, Class<T> clazz) {
        try {
            URI url = new URI("http://localhost:8080" + uri);
            return TEMPLATE.getForObject(url, clazz);
        } catch (URISyntaxException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

}
