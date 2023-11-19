package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public record EventPageResponse(List<EventResponse> events, PageableResponse pageData) {

    public EventPageResponse(List<EventResponse> events, Page<Event> page) {
        this(events, new PageableResponse(page));
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
