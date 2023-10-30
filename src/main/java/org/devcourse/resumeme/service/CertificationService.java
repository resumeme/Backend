package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.Career;
import org.devcourse.resumeme.domain.resume.Certification;
import org.devcourse.resumeme.repository.CareerRepository;
import org.devcourse.resumeme.repository.CertificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public Long create(Certification certification) {
        return certificationRepository.save(certification).getId();
    }

}

