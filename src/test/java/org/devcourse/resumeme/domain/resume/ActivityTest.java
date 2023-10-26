package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ActivityTest {

    private String activityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean inProgress;
    private String link;
    private String description;

    @BeforeEach
    void init() {
        activityName = "코딩 챌린지";
        startDate = LocalDate.of(2023, 1, 1);
        endDate = LocalDate.of(2023, 1, 15);
        inProgress = false;
        link = "https://example.com";
        description = "코딩 챌린지 완료";
    }

    @Test
    void 활동_생성을_성공한다() {
        Activity activity = new Activity(activityName, startDate, endDate, inProgress, link, description);

        assertEquals(activityName, activity.getActivityName());
    }

    @Test
    void 활동명_누락_시_예외_발생() {
        assertThatThrownBy(() -> new Activity(null, startDate, endDate, inProgress, link, description))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 시작일_누락_시_예외_발생() {
        assertThatThrownBy(() -> new Activity(activityName, null, endDate, inProgress, link, description))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 종료일_누락_및_진행중이_아닌_경우_예외_발생() {
        assertThatThrownBy(() -> new Activity(activityName, startDate, null, false, link, description))
                .isInstanceOf(CustomException.class);
    }
}
