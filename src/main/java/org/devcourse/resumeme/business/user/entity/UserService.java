package org.devcourse.resumeme.business.user.entity;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.controller.admin.dto.ApplicationProcessType;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationEventPublisher;
import org.devcourse.resumeme.business.user.service.vo.CreatedUserVo;
import org.devcourse.resumeme.business.user.service.vo.UpdateUserVo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTEE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final MentorApplicationEventPublisher mentorApplicationEventPublisher;

    public CreatedUserVo create(User user) {
        User savedUser = userRepository.save(user);
        if (user.getRequiredInfo().getRole().equals(Role.ROLE_PENDING)) {
            mentorApplicationEventPublisher.publishMentorApplicationEvent(Mentor.of(savedUser));
        }

        return CreatedUserVo.of(savedUser);
    }

    public void updateRefreshToken(Long id, String refreshToken) {
        User user = getOne(id);
        user.updateRefreshToken(refreshToken);
    }

    public User getOne(Long userId) {
        return userRepository.findWithPositionsAndFields(userId)
                .orElseThrow(() -> new CustomException(MENTEE_NOT_FOUND));
    }

    public void deleteRefreshToken(Long id) {
        updateRefreshToken(id, null);
    }

    public List<User> getByIds(List<Long> ids) {
        return userRepository.findAllByIds(ids);
    }

    public void updateRole(Long mentorId, ApplicationProcessType type) {
        User user = getOne(mentorId);
        Mentor mentor = Mentor.of(user);
        mentor.updateRole(type.getRole());
        User newUser = mentor.from();
        userRepository.save(newUser);
    }

    public Long update(Long userId, UpdateUserVo updateVo) {
        User user = getOne(userId);
        User updatedUser = updateVo.update(user);
        userRepository.save(updatedUser);

        return updatedUser.getId();
    }

}
