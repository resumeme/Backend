package org.devcourse.resumeme.domain.review;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.event.EventInfo;
import org.devcourse.resumeme.domain.event.EventPosition;
import org.devcourse.resumeme.domain.event.EventTimeInfo;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReviewTest {

    @Test
    void 리뷰_작성에_성공한다() {
        // given
        String content = "내용 수정해주세요";
        BlockType type = BlockType.CAREER;
        Resume resume = new Resume("title", Mentee.builder().build());

        // when
        Review review = new Review(content, type, resume);

        // then
        assertThat(review).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("reviewCreate")
    void 이벤트포지션_생성_검증조건_미달로인해_생성에_실패한다_null입력_오류(String content, BlockType type, Resume resume) {
        // then
        assertThatThrownBy(() -> new Review(content, type, resume))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> reviewCreate() {
        String content = "내용 수정해주세요";
        BlockType type = BlockType.CAREER;
        Resume resume = new Resume("title", Mentee.builder().build());

        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of(content, null, null),
                Arguments.of(null, type, null),
                Arguments.of(null, null, resume),
                Arguments.of("", type, null),
                Arguments.of("   ", null, resume)
        );
    }

}
