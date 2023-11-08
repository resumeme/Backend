package org.devcourse.resumeme.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.business.comment.controller.CommentController;
import org.devcourse.resumeme.business.comment.service.CommentService;
import org.devcourse.resumeme.business.event.controller.EventController;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.result.controller.ResultNoticeController;
import org.devcourse.resumeme.business.result.service.ResultService;
import org.devcourse.resumeme.business.resume.controller.ActivityController;
import org.devcourse.resumeme.business.resume.controller.CareerController;
import org.devcourse.resumeme.business.resume.controller.CertificationController;
import org.devcourse.resumeme.business.resume.controller.ForeignLanguageController;
import org.devcourse.resumeme.business.resume.controller.ProjectController;
import org.devcourse.resumeme.business.resume.controller.ResumeController;
import org.devcourse.resumeme.business.resume.controller.TrainingController;
import org.devcourse.resumeme.business.resume.service.ActivityService;
import org.devcourse.resumeme.business.resume.service.CareerService;
import org.devcourse.resumeme.business.resume.service.CertificationService;
import org.devcourse.resumeme.business.resume.service.ForeignLanguageService;
import org.devcourse.resumeme.business.resume.service.ProjectService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.resume.service.TrainingService;
import org.devcourse.resumeme.business.user.controller.admin.MentorApplicationController;
import org.devcourse.resumeme.business.user.controller.mentee.MenteeController;
import org.devcourse.resumeme.business.user.controller.mentor.MentorController;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationService;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.common.controller.EnumController;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2FailureHandler;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2SuccessHandler;
import org.devcourse.resumeme.global.auth.service.jwt.JwtService;
import org.devcourse.resumeme.global.auth.service.login.OAuth2InfoRedisService;
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
import org.springframework.security.authentication.ProviderManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest({
        EnumController.class,
        EventController.class,
        ResumeController.class,
        MenteeController.class,
        MentorController.class,
        CommentController.class,
        CareerController.class,
        ProjectController.class,
        CertificationController.class,
        MentorApplicationController.class,
        MentorApplicationController.class,
        ActivityController.class,
        ForeignLanguageController.class,
        ResultNoticeController.class,
        TrainingController.class
})
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ControllerUnitTest {

    @MockBean
    protected EventPositionService eventPositionService;
    
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
    protected OAuth2InfoRedisService oAuth2InfoRedisService;

    @MockBean
    protected CommentService reviewService;

    @MockBean
    protected CareerService careerService;

    @MockBean
    protected MentorApplicationService mentorApplicationService;

    @MockBean
    protected ProjectService projectService;

    @MockBean
    protected CertificationService certificationService;

    @MockBean
    protected ActivityService activityService;

    @MockBean
    protected ForeignLanguageService foreignLanguageService;

    @MockBean
    protected ResultService resultService;

    @MockBean
    protected TrainingService trainingService;

    @MockBean
    protected MenteeToEventService menteeToEventService;

    @MockBean
    protected ProviderManager providerManager;

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        OAuthTokenResponseFilter filter = new OAuthTokenResponseFilter(providerManager, mapper);
        filter.setAuthenticationSuccessHandler(new OAuth2SuccessHandler(new JwtService(), mentorService, menteeService));
        filter.setAuthenticationFailureHandler(new OAuth2FailureHandler());

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilter(filter)
                .build();
    }

    protected String toJson(Object data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    protected void setId(Object target, Long injectId) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(target, injectId);
    }

}
