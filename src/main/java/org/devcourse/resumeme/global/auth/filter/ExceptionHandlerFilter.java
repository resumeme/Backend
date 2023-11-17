package org.devcourse.resumeme.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;
import org.devcourse.resumeme.global.exception.TokenException;
import org.devcourse.resumeme.global.exception.UnAuthenticatedException;
import org.devcourse.resumeme.global.exception.advice.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_ACCESS_TOKEN;
import static org.devcourse.resumeme.global.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public ExceptionHandlerFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            log.error("Exception Message : {}, \nException Type : {}", e.getMessage(), e.getClass().getSimpleName(), e);
            sendErrorResponse(response, 400, INVALID_ACCESS_TOKEN);
        } catch (UnAuthenticatedException e) {
            log.error("Exception Message : {}, \nException Type : {}", e.getMessage(), e.getClass().getSimpleName(), e);
            sendErrorResponse(response, 401, ExceptionCode.LOGIN_REQUIRED);
        } catch (CustomException e) {
            log.error("Exception Message : {}, \nException Type : {}", e.getMessage(), e.getClass().getSimpleName(), e);
            sendErrorResponse(response, 400, ExceptionCode.valueOf(e.getCode()));
        } catch (Exception e) {
            log.error("Exception Message : {}, \nException Type : {}", e.getMessage(), e.getClass().getSimpleName(), e);
            sendErrorResponse(response, 500, SERVER_ERROR);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, ExceptionCode code) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(new ErrorResponse(code.getMessage(), code.name())));
    }

}
