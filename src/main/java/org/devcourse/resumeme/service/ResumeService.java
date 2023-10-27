package org.devcourse.resumeme.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.repository.ResumeRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Long create(Resume resume) {
        Resume saved = resumeRepository.save(resume);

        return saved.getId();
    }

}
