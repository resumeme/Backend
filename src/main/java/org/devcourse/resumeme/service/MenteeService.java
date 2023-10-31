package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MenteeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenteeService {

    private final MenteeRepository menteeRepository;

    public Mentee create(Mentee mentee) {
        return menteeRepository.save(mentee);
    }

    @Transactional(readOnly = true)
    public Mentee getOne(Long id) {
        return menteeRepository.findById(id).orElseThrow(() -> new CustomException("MENTEE_NOT_FOUND", "존재하지 않는 회원입니다."));
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        Mentee findMentee = getOne(id);
        findMentee.updateRefreshToken(refreshToken);
    }

    public void update(Long id, MenteeInfoUpdateRequest updateRequest) {
        Mentee findMentee = getOne(id);
        findMentee.updateInfos(updateRequest);
    }

}
