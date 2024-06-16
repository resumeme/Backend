package org.devcourse.resumeme.business.user.service.mentee;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.business.user.repository.FollowRepository;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.ALREADY_FOLLOWING;
import static org.devcourse.resumeme.global.exception.ExceptionCode.EXCEEDED_FOLLOW_MAX;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final int MAX_FOLLOW_SIZE = 3;

    private final FollowRepository followRepository;

    public Long create(Follow follow) {
        List<Follow> followList = getFollowings(follow.getMenteeId());

        if (followList.size() > MAX_FOLLOW_SIZE - 1) {
            throw new CustomException(EXCEEDED_FOLLOW_MAX);
        }
        if (isFollowing(followList, follow.getMentorId())) {
            throw new CustomException(ALREADY_FOLLOWING);
        }

        return followRepository.save(follow).getId();
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowings(Long menteeId) {
        return followRepository.findAllByMenteeId(menteeId);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowers(Long mentorId) {
        return followRepository.findAllByMentorId(mentorId);
    }

    public void unfollow(Long followId) {
        followRepository.deleteById(followId);
    }

    @Transactional(readOnly = true)
    public Long getFollow(Long menteeId, Long mentorId) {
        return followRepository.findByMenteeIdAndMentorId(menteeId, mentorId)
                .map(Follow::getId)
                .orElse(null);
    }

    private boolean isFollowing(List<Follow> followList, Long mentorId) {
        return followList.stream().anyMatch(follow -> follow.getMentorId().equals(mentorId));
    }

}
