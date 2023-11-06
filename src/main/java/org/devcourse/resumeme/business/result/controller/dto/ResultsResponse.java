package org.devcourse.resumeme.business.result.controller.dto;

import org.devcourse.resumeme.business.result.domain.ResultNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public record ResultsResponse(List<ResultResponse> notice, PageableResponse pageData) {

    public ResultsResponse(Page<ResultNotice> resultNotices) {
        this(
                resultNotices.getContent().stream()
                .map(ResultResponse::new)
                .toList(),
                new PageableResponse(resultNotices)
        );
    }

    public record ResultResponse(Long resultId, String title) {

        public ResultResponse(ResultNotice result) {
            this(result.getId(), result.getResume().getTitle());
        }

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

        private PageableResponse(Page<ResultNotice> page) {
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
