package org.devcourse.resumeme.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MentorRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    public Mentor getOne(Long id) {
        return mentorRepository.findById(id).orElseThrow(() -> new CustomException("MENTOR_NOT_FOUND", "존재하지 않는 회원입니다."));
    }

    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        Mentor findMentor = getOne(id);
        findMentor.updateRefreshToken(refreshToken);
    }

}
