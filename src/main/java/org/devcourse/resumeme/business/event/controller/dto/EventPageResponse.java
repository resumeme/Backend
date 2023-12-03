package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public record EventPageResponse(List<EventResponse> events, PageableResponse pageData) {

    public static EventPageResponse of(List<EventPosition> positions, List<UserResponse> mentors, Page<Event> pageAbleEvent) {
        Map<Object, List<EventPosition>> positionsMap = positions.stream()
                .collect(groupingBy(position -> position.getEvent().getId(), toList()));
        Map<Long, UserResponse> mentorsMap = mentors.stream()
                .collect(Collectors.toMap(UserResponse::userId, Function.identity()));

        List<EventResponse> responses = getEvents(pageAbleEvent).stream()
                .map(event -> new EventResponse(event, positionsMap.get(event.getId()), mentorsMap.get(event.getMentorId())))
                .toList();

        return new EventPageResponse(responses, new PageableResponse(pageAbleEvent));
    }

    private static List<Event> getEvents(Page<Event> events) {
        List<Event> content = new ArrayList<>(events.getContent());
        Collections.sort(content);

        return content;
    }

    private record PageableResponse(
            boolean first,
            boolean last,
            int number,
            int size,
            Sort sort,
            int totalPages,
            long totalElements
    ) {

        private PageableResponse(Page<Event> page) {
            this(
                    page.isFirst(),
                    page.isLast(),
                    page.getNumber(),
                    page.getSize(),
                    page.getSort(),
                    page.getTotalPages(),
                    page.getTotalElements()
            );
        }

    }

}
