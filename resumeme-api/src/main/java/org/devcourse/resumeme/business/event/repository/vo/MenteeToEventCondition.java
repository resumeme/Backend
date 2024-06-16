package org.devcourse.resumeme.business.event.repository.vo;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

import static org.devcourse.resumeme.business.event.domain.QMenteeToEvent.menteeToEvent;

public record MenteeToEventCondition(Long mentorId, Long menteeId) {

    public BooleanExpression[] getExpression() {
        List<BooleanExpression> booleanExpressions = new ArrayList<>();
        booleanExpressions.add(equalMentorId());
        booleanExpressions.add(equalMenteeId());

        return booleanExpressions.toArray(BooleanExpression[]::new);
    }

    private BooleanExpression equalMentorId() {
        if (mentorId == null) {
            return null;
        }

        return menteeToEvent.event.mentorId.eq(mentorId);
    }

    private BooleanExpression equalMenteeId() {
        if (menteeId == null) {
            return null;
        }

        return menteeToEvent.menteeId.eq(menteeId);
    }

}
