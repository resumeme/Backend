package org.devcourse.resumeme.business.event.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.devcourse.resumeme.business.event.domain.QEvent.event;
import static org.devcourse.resumeme.business.event.domain.QMenteeToEvent.menteeToEvent;

@Repository
@RequiredArgsConstructor
public class CustomMenteeToEventRepositoryImpl implements CustomMenteeToEventRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenteeToEvent> findAllBy(BooleanExpression... expressions) {
        return queryFactory.selectFrom(menteeToEvent)
                .join(menteeToEvent.event, event).fetchJoin()
                .where(expressions)
                .fetch();
    }

}
