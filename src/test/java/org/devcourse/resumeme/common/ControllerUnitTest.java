package org.devcourse.resumeme.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.common.controller.EnumController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest({
        EnumController.class
})
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ControllerUnitTest {

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    protected String toJson(Object data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

}
