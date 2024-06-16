package org.devcourse.resumeme.business.user.controller.dto.mentee;

import org.devcourse.resumeme.business.user.service.vo.UpdateMenteeVo;
import org.devcourse.resumeme.business.user.service.vo.UpdateUserVo;

import java.util.Set;

public record MenteeInfoUpdateRequest(String nickname, String phoneNumber, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {

    public MenteeInfoUpdateRequest(String nickname, String phoneNumber, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.interestedPositions = replaceNullWithEmptySet(interestedPositions);
        this.interestedFields = replaceNullWithEmptySet(interestedFields);
        this.introduce = introduce;
    }

    private Set<String> replaceNullWithEmptySet(Set<String> attributes) {
        if (attributes == null) {
            return Set.of();
        }

        return attributes;
    }

    public UpdateUserVo toVo() {
        return new UpdateMenteeVo(nickname, phoneNumber, interestedPositions, interestedFields, introduce);
    }

}
