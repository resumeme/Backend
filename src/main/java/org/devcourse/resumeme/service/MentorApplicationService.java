package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.admin.MentorApplication;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MentorApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.APPLICANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MentorApplicationService {

    private final MentorApplicationRepository repository;

    public void create(MentorApplication mentorApplication) {
        repository.save(mentorApplication);
    }

    public List<MentorApplication> getAll() {
        return repository.findAll();
    }

    public Long delete(Long applicationId) {
        MentorApplication mentorApplication = repository.findById(applicationId)
                .orElseThrow(() -> new CustomException(APPLICANT_NOT_FOUND));
        Long mentorId = mentorApplication.mentorId();

        repository.delete(mentorApplication);

        return mentorId;
    }

}
