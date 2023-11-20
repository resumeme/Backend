package org.devcourse.resumeme.business.comment.domain;

import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentTest {

    @Test
    void 리뷰_작성에_성공한다() {
        // given
        String content = "내용 수정해주세요";
        BlockType type = BlockType.CAREER;
        Mentee mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();
        Resume resume = new Resume("title", mentee);
        Component component = new Component("career", "career", null, null, 1L, List.of());

        // when
        Comment comment = new Comment(content, 1L, resume);

        // then
        assertThat(comment).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("reviewCreate")
    void 이벤트포지션_생성_검증조건_미달로인해_생성에_실패한다_null입력_오류(String content, BlockType type, Long componentId, Resume resume) {
        // then
        assertThatThrownBy(() -> new Comment(content, componentId, resume))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> reviewCreate() {
        String content = "내용 수정해주세요";
        BlockType type = BlockType.CAREER;
        Mentee mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        Resume resume = new Resume("title", mentee);

        return Stream.of(
                Arguments.of(null, null, 1L, null),
                Arguments.of(content, null, 1L, null),
                Arguments.of(null, type, 1L, null),
                Arguments.of(null, null, 1L, resume),
                Arguments.of("", type, 1L, null),
                Arguments.of("   ", null, null, resume)
        );
    }

}
