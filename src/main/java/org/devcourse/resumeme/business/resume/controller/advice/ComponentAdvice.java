package org.devcourse.resumeme.business.resume.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.v2.ComponentCreateRequestV2;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@RestControllerAdvice
@RequiredArgsConstructor
public class ComponentAdvice implements RequestBodyAdvice {

    private static final String TYPE = "type";

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getParameterType().equals(ComponentCreateRequestV2.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        Map<String, Object> result = objectMapper.readValue(inputMessage.getBody(), HashMap.class);

        Map<String, Object> response = new HashMap<>();
        response.put("activities", null);
        response.put("careers", null);
        response.put("certifications", null);
        response.put("foreignLanguages", null);
        response.put("projects", null);
        response.put("trainings", null);
        response.put("links", null);

        response.put(getTypeFromPathVariable(), result);

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                String inputString = objectMapper.writeValueAsString(response);

                return new ByteArrayInputStream(inputString.getBytes(UTF_8));
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

        };
    }

    private String getTypeFromPathVariable() {
        ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Map<String, String> attributes = (Map<String, String>) request.getRequest().getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        return attributes.get(TYPE);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
