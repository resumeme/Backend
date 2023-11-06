package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.resume.ResumeInfo;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.RESUME_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Long create(Resume resume) {
        Resume saved = resumeRepository.save(resume);

        return saved.getId();
    }

    public Long updateResumeInfo(Resume resume, ResumeInfo resumeInfo) {
        resume.updateResumeInfo(resumeInfo);
        Resume saved = resumeRepository.save(resume);

        return saved.getId();
    }

    public Long updateTitle(Resume resume, String title) {
        resume.updateTitle(title);
        Resume saved = resumeRepository.save(resume);

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public Resume getOne(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new CustomException(RESUME_NOT_FOUND));
    }

    public Long copyResume(Long id) {
        return create(getOne(id).copy());
    }

    public List<Resume> getAll(List<Long> resumeIds) {
        return resumeRepository.findAllById(resumeIds);
    }

}
