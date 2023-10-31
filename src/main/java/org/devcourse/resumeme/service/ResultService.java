package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.result.ResultNotice;
import org.devcourse.resumeme.repository.ResultNoticeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultNoticeRepository repository;

    public Long create(ResultNotice resultNotice) {
        return repository.save(resultNotice).getId();
    }

}
