package org.devcourse.resumeme.business.resume.repository;

import org.devcourse.resumeme.business.resume.entity.PassInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PassInfoRepository extends JpaRepository<PassInfo, Long> {

    long countByPassStatus(boolean passStatus);

    long countByPassStatusIsTrueAndPassDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
