package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.user.User;
import org.devcourse.resumeme.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 아이디의 회원이 없습니다."));
    }

    public Long completeSignUp(User userWithPrimaryInfo) {
        return userRepository.save(userWithPrimaryInfo).getId();
    }

}
