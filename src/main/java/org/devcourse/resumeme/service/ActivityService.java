package org.devcourse.resumeme.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.Activity;
import org.devcourse.resumeme.repository.ActivityRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Long create(Activity activity) {
        return activityRepository.save(activity).getId();
    }

}
