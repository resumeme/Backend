package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.Training;
import org.devcourse.resumeme.business.resume.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public Long create(Training training) {
        return trainingRepository.save(training).getId();
    }

}
