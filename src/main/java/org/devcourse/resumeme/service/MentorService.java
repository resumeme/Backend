package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.ApplicationProcessType;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MentorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    @Transactional(readOnly = true)
    public Mentor getOne(Long id) {
        return mentorRepository.findById(id).orElseThrow(() -> new CustomException("MENTOR_NOT_FOUND", "존재하지 않는 회원입니다."));
    }

    public Mentor create(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        Mentor findMentor = getOne(id);
        findMentor.updateRefreshToken(refreshToken);
    }

    public void updateRole(Long mentorId, ApplicationProcessType type) {
        Mentor mentor = getOne(mentorId);
        mentor.updateRole(type.getRole());
    }

}
