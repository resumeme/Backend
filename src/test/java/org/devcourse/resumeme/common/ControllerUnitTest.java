package org.devcourse.resumeme.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.common.controller.EnumController;
import org.devcourse.resumeme.controller.EventController;
import org.devcourse.resumeme.controller.MenteeController;
import org.devcourse.resumeme.controller.MentorController;
import org.devcourse.resumeme.controller.ResumeController;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.repository.OAuth2InfoRedisRepository;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.controller.ReviewController;
import org.devcourse.resumeme.service.EventService;
import org.devcourse.resumeme.service.MentorService;
import org.devcourse.resumeme.service.ResumeService;
import org.devcourse.resumeme.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest({
        EnumController.class,
        EventController.class,
        ResumeController.class,
        MenteeController.class,
        MentorController.class
        ReviewController.class
})
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ControllerUnitTest {

    @MockBean
    protected EventService eventService;

    @MockBean
    protected ResumeService resumeService;

    @MockBean
    protected MentorService mentorService;

    @MockBean
    protected MenteeService menteeService;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected OAuth2InfoRedisRepository oAuth2InfoRedisRepository;

    @MockBean
    protected ReviewService reviewService;

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
