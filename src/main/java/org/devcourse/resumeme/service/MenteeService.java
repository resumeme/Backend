package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MenteeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenteeService {

    private final MenteeRepository menteeRepository;

    @Transactional(readOnly = true)
    public Mentee getOne(Long id) {
        return menteeRepository.findById(id).orElseThrow(() -> new CustomException("MENTEE_NOT_FOUND", "존재하지 않는 회원입니다."));
    }

    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        Mentee findMentee = getOne(id);
        findMentee.updateRefreshToken(refreshToken);
    }

}
