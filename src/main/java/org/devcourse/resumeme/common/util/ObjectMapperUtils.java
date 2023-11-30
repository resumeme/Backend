package org.devcourse.resumeme.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;

import static lombok.AccessLevel.PRIVATE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.SERVER_ERROR;

@NoArgsConstructor(access = PRIVATE)
public class ObjectMapperUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T getObjectMapper(String data, Class<T> clazz) {
        try {
            return MAPPER.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    public static String getData(Object target) {
        try {
            return MAPPER.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

}
