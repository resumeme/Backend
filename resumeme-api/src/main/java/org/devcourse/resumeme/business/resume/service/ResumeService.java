package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.service.ParticipantProvider;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.domain.model.ResumeUpdateModel;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.resume.service.v2.ResumeUpdateVo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.RESUME_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final ParticipantProvider participantProvider;

    public Long create(Resume resume) {
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
        return resumeRepository.findAllByMenteeId(menteeId).stream()
                .filter(Resume::isOrigin)
                .toList();
    }

    public void delete(Long resumeId) {
        Resume resume = getOne(resumeId);

        resumeRepository.delete(resume);
    }

    public void update(Long resumeId, ResumeUpdateVo update) {
        Resume resume = getOne(resumeId);
        ResumeUpdateModel model = update.toModel();

        model.update(resume);

        participantProvider.finishProgress(resume.getMenteeId(), resumeId);
    }

}
