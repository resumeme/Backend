package org.devcourse.resumeme.business.user.controller.dto.mentee;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;

import java.util.Set;
import java.util.stream.Collectors;

public record MenteeInfoResponse(String imageUrl, String realName, String nickname, String phoneNumber, Set<String> interestedPositions,
                             Set<String> interestedFields, String introduce) {

    public MenteeInfoResponse(Mentee mentee) {
        this(mentee.getImageUrl(), mentee.getRequiredInfo().getRealName(), mentee.getRequiredInfo().getNickname(), mentee.getRequiredInfo().getPhoneNumber(),
                getInterestedPositions(mentee),
                getInterestedFields(mentee),
                mentee.getIntroduce());
    }

    private static Set<String> getInterestedPositions(Mentee mentee) {
        return mentee.getInterestedPositions().stream().map(Enum::name).collect(Collectors.toSet());
    }

    private static Set<String> getInterestedFields(Mentee mentee) {
        return mentee.getInterestedFields().stream().map(Enum::name).collect(Collectors.toSet());
    }

}
