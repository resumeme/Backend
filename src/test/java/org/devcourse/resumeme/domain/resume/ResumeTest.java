package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.domain.user.User;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ResumeTest {

    private User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("email")
                .provider(Provider.KAKAO)
                .role(Role.ROLE_MENTOR)
                .build();
    }

}
