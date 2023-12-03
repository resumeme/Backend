package org.devcourse.resumeme.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devcourse.resumeme.business.comment.controller.CommentController;
import org.devcourse.resumeme.business.comment.service.CommentService;
import org.devcourse.resumeme.business.event.controller.EventController;
import org.devcourse.resumeme.business.event.controller.MenteeToEventController;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.mail.service.EmailService;
import org.devcourse.resumeme.business.result.controller.ResultNoticeController;
import org.devcourse.resumeme.business.result.service.ResultService;
import org.devcourse.resumeme.business.resume.controller.ComponentController;
import org.devcourse.resumeme.business.resume.controller.ResumeController;
import org.devcourse.resumeme.business.resume.controller.ResumeControllerV2;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.business.resume.service.ResumeCopier;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.snapshot.controller.SnapshotController;
import org.devcourse.resumeme.business.snapshot.service.CommentCapture;
import org.devcourse.resumeme.business.snapshot.service.SnapshotService;
import org.devcourse.resumeme.business.user.controller.FollowController;
import org.devcourse.resumeme.business.user.controller.MentorApplicationController;
import org.devcourse.resumeme.business.user.controller.UserController;
import org.devcourse.resumeme.business.user.service.AccountService;
import org.devcourse.resumeme.business.user.service.UserInfoProvider;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationService;
import org.devcourse.resumeme.business.user.service.mentee.FollowService;
import org.devcourse.resumeme.business.userevent.controller.UserEventController;
import org.devcourse.resumeme.common.controller.EnumController;
import org.devcourse.resumeme.common.support.NullableHttpRequestSnippet;
import org.devcourse.resumeme.common.support.NullableHttpResponseSnippet;
import org.devcourse.resumeme.global.auth.filter.FilterTestController;
import org.devcourse.resumeme.global.auth.filter.JwtAuthorizationFilter;
import org.devcourse.resumeme.global.auth.filter.OAuthTokenResponseFilter;
import org.devcourse.resumeme.global.auth.filter.handler.CustomLogoutHandler;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2FailureHandler;
import org.devcourse.resumeme.global.auth.filter.handler.OAuth2SuccessHandler;
import org.devcourse.resumeme.global.auth.model.jwt.JwtProperties;
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
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.global.auth.model.jwt.JwtProperties.TokenInfo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest({
        EnumController.class,
        EventController.class,
        ResumeController.class,
        CommentController.class,
        MentorApplicationController.class,
        ResultNoticeController.class,
        UserController.class,
        ComponentController.class,
        FilterTestController.class,
        UserEventController.class,
        ResumeControllerV2.class,
        SnapshotController.class,
        FollowController.class,
        MenteeToEventController.class,
        SnapshotController.class
})
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ControllerUnitTest {

    @MockBean
    protected CommentCapture commentCapture;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected EventPositionService eventPositionService;

    @MockBean
    protected ComponentService componentService;

    @MockBean
    protected EventService eventService;

    @MockBean
    protected ResumeService resumeService;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected OAuth2InfoRedisService oAuth2InfoRedisService;

    @MockBean
    protected CommentService reviewService;

    @MockBean
    protected MentorApplicationService mentorApplicationService;

    @MockBean
    protected ResultService resultService;

    @MockBean
    protected MenteeToEventService menteeToEventService;

    @MockBean
    protected ProviderManager providerManager;

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected SnapshotService snapshotService;

    @MockBean
    protected FollowService followService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected UserInfoProvider userInfoProvider;

    @MockBean
    protected ResumeCopier resumeCopier;

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        OAuthTokenResponseFilter oauthTokenResponseFilter = new OAuthTokenResponseFilter(providerManager, mapper);
        oauthTokenResponseFilter.setAuthenticationSuccessHandler(new OAuth2SuccessHandler(new JwtService(new JwtProperties("null", new TokenInfo("Authorization", 20), new TokenInfo("Refresh-Token", 30))), userService));
        oauthTokenResponseFilter.setAuthenticationFailureHandler(new OAuth2FailureHandler());

        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtService, userService);
        LogoutFilter logoutFilter =
                new LogoutFilter(new HttpStatusReturningLogoutSuccessHandler(), new CustomLogoutHandler(userService));
        logoutFilter.setFilterProcessesUrl("/api/v1/logout");

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilter(oauthTokenResponseFilter)
                .addFilter(jwtAuthorizationFilter)
                .addFilter(logoutFilter)
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

    protected static ExceptionResponseFieldsSnippet exceptionResponse(List<String> exceptions) {
        return new ExceptionResponseFieldsSnippet("exception-response", Map.of("exceptions", String.join(", ", exceptions)));
    }

    protected static NullableHttpRequestSnippet nullableHttpRequestSnippet() {
        return new NullableHttpRequestSnippet();
    }

    protected static NullableHttpResponseSnippet nullableHttpResponseSnippet() {
        return new NullableHttpResponseSnippet();
    }

    protected static class ExceptionResponseFieldsSnippet extends TemplatedSnippet {

        protected ExceptionResponseFieldsSnippet(String snippetName, Map<String, Object> attributes) {
            super(snippetName, attributes);
        }

        @Override
        protected Map<String, Object> createModel(Operation operation) {
            return getAttributes();
        }

    }

}
