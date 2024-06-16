package org.devcourse.resumeme.business.result.repository;

import org.devcourse.resumeme.business.result.domain.ResultNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResultNoticeRepository extends JpaRepository<ResultNotice, Long>  {

    @Query("select r from ResultNotice r join fetch r.resume where r.resume.openStatus = TRUE")
    Page<ResultNotice> findOpenResumeAll(Pageable pageable);

}
