package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.repository.ResumeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Long create(Resume resume) {
        Resume saved = resumeRepository.save(resume);
        return saved.getId();
    }

}
