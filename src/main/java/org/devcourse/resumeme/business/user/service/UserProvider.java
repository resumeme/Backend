package org.devcourse.resumeme.business.user.service;

import org.devcourse.resumeme.business.user.service.vo.UserResponse;

import java.util.List;

public interface UserProvider {

    UserResponse getOne(Long userId);

    List<UserResponse> getByIds(List<Long> userIds);

}
