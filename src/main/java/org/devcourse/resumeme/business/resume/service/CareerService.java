package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Career;
import org.devcourse.resumeme.business.resume.repository.CareerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CareerService {

    private final CareerRepository careerRepository;

    public Long create(Career career) {
        return careerRepository.save(career).getId();
    }

    @Transactional(readOnly = true)
    public Career getOne(Long careerId) {
        return careerRepository.getReferenceById(careerId);
    }

}
