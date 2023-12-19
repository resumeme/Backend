package org.devcourse.resumeme.business.event.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;

import java.util.List;

public interface CustomMenteeToEventRepository {

    List<MenteeToEvent> findAllBy(BooleanExpression... expressions);

}
