package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.admin.MentorApplication;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MentorApplicationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorApplicationService {

    private final MentorApplicationRepository repository;

    public void create(MentorApplication mentorApplication) {
        repository.save(mentorApplication);
    }

    public Long delete(Long applicationId) {
        MentorApplication mentorApplication = repository.findById(applicationId)
                .orElseThrow(() -> new CustomException("NOT_FOUND_APPLICANT", "신청 이력이 없습니다"));
        Long mentorId = mentorApplication.getMentorId();

        repository.delete(mentorApplication);

        return mentorId;
    }

}
