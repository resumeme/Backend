package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.admin.MentorApplication;
import org.devcourse.resumeme.repository.MentorApplicationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorApplicationService {

    private final MentorApplicationRepository repository;

    public void create(MentorApplication mentorApplication) {
        repository.save(mentorApplication);
    }

}
