package org.devcourse.resumeme.business.result.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.result.domain.ResultNotice;
import org.devcourse.resumeme.business.result.repository.ResultNoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultNoticeRepository repository;

    public Long create(ResultNotice resultNotice) {
        return repository.save(resultNotice).getId();
    }

    public Page<ResultNotice> getAll(Pageable pageable) {
        return repository.findOpenResumeAll(pageable);
    }

}
