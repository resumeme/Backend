package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;
import org.devcourse.resumeme.repository.MenteeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenteeService {

    private final MenteeRepository menteeRepository;

    public Mentee getOne(Long id) {
        return menteeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.MENTEE_NOT_FOUND));
    }

}
