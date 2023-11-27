package org.devcourse.resumeme.business.user.service.mentor;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.repository.mentor.MentorRepository;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationEventPublisher;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTOR_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final MentorApplicationEventPublisher mentorApplicationEventPublisher;

    @Transactional(readOnly = true)
    public Mentor getOne(Long mentorId) {
        return mentorRepository.findWithPositions(mentorId)
                .orElseThrow(() -> new CustomException(MENTOR_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Mentor getOneSimple(Long mentorId) {
        return mentorRepository.findById(mentorId)
                .orElseThrow(() -> new CustomException(MENTOR_NOT_FOUND));
    }

    public Mentor create(Mentor mentor) {
        Mentor savedMentor = mentorRepository.save(mentor);
        mentorApplicationEventPublisher.publishMentorApplicationEvent(savedMentor);
        return savedMentor;
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        Mentor findMentor = getOne(id);
        findMentor.updateRefreshToken(refreshToken);
    }

    public void updateRole(Long mentorId, ApplicationProcessType type) {
        Mentor mentor = getOne(mentorId);
        mentor.updateRole(type.getRole());
    }

    public Long update(Long mentorId, MentorInfoUpdateRequest mentorInfoUpdateRequest) {
        Mentor mentor = getOne(mentorId);
        mentor.updateInfos(mentorInfoUpdateRequest);

        return mentor.getId();
    }

    public void deleteRefreshToken(Long id) {
        updateRefreshToken(id, null);
    }

    public List<Mentor> getAllByIds(List<Long> mentorIds) {
        return mentorRepository.findAllByIds(mentorIds);
    }
}
