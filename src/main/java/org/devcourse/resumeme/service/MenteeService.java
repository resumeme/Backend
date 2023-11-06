package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.MenteeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.MENTEE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MenteeService {

    private final MenteeRepository menteeRepository;

    public Mentee create(Mentee mentee) {
        return menteeRepository.save(mentee);
    }

    @Transactional(readOnly = true)
    public Mentee getOne(Long menteeId) {
        return menteeRepository.findWithPositionsAndFields(menteeId)
                .orElseThrow(() -> new CustomException(MENTEE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Mentee getOneSimple(Long menteeId) {
        return menteeRepository.findById(menteeId)
                .orElseThrow(() -> new CustomException(MENTEE_NOT_FOUND));
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        Mentee findMentee = getOne(id);
        findMentee.updateRefreshToken(refreshToken);
    }

    public Long update(Long id, MenteeInfoUpdateRequest updateRequest) {
        Mentee findMentee = getOne(id);
        findMentee.updateInfos(updateRequest);

        return findMentee.getId();
    }

    @Transactional(readOnly = true)
    public List<Mentee> getAll() {
        return menteeRepository.findAll();
    }

}
