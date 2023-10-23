package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.user.User;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND","존재하지 않는 회원입니다."));
    }

    public Long completeSignUp(User userWithPrimaryInfo) {
        return userRepository.save(userWithPrimaryInfo).getId();
    }

}
