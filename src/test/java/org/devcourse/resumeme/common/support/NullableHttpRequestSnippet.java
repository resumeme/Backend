package org.devcourse.resumeme.common.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.restdocs.http.HttpRequestSnippet;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;

import java.util.HashMap;
import java.util.Map;

public class NullableHttpRequestSnippet extends HttpRequestSnippet {

    public NullableHttpRequestSnippet() {
        super();
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        Map<String, Object> model = super.createModel(operation);
        try {
            model.put("requestBody", getRequestBody(operation.getRequest()));

            return model;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRequestBody(OperationRequest operation) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String content = operation.getContentAsString();
            Map<String, Object> contents = mapper.readValue(content, Map.class);

            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, Object> entry : contents.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }

            return "\n%s".formatted(mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
