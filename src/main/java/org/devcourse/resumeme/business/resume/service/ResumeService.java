package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.ResumeInfo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

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

    public Long updateReferenceLink(Resume resume, ReferenceLink referenceLink) {
        resume.updateReferenceLink(referenceLink);
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

    public List<Resume> getAllByMenteeId(Long menteeId) {
        return resumeRepository.findAllByMenteeId(menteeId);
    }

}
