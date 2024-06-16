package org.devcourse.resumeme.business.user.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.repository.UserRepository;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserInfoProvider implements UserProvider {

    private final UserRepository userRepository;
    
    @Override
    public UserResponse getOne(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return new UserResponse(user);
    }

    @Override
    public List<UserResponse> getByIds(List<Long> userIds) {
        return userRepository.findAllByIds(userIds).stream()
                .map(UserResponse::new)
                .toList();
    }

}
