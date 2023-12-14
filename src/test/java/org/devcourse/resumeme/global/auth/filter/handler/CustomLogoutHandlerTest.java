package org.devcourse.resumeme.global.auth.filter.handler;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomLogoutHandlerTest extends ControllerUnitTest {

    @Test
    @WithMockCustomUser
    void 로그아웃에_성공한다() throws Exception {
        // given
        doNothing().when(userService).deleteRefreshToken(any(Long.class));

        // when
        ResultActions result = mvc.perform(post("/api/v1/logout"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(document("logout",
                                getDocumentRequest(),
                                getDocumentResponse()
                        )
                );

    }

}
