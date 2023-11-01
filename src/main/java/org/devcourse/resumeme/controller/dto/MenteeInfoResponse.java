package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentee.Mentee;

import java.util.Set;
import java.util.stream.Collectors;

public record MenteeInfoResponse(String imageUrl, String phoneNumber, String role, Set<String> interestedPositions,
                             Set<String> interestedFields, String introduce) {

    public MenteeInfoResponse(Mentee mentee) {
        this(mentee.getImageUrl(), mentee.getRequiredInfo().getPhoneNumber(), mentee.getRequiredInfo().getRole().name(),
                getInterestedPositions(mentee),
                getInterestedFields(mentee),
                mentee.getIntroduce());
    }

    private static Set<String> getInterestedPositions(Mentee mentee) {
        return mentee.getInterestedPositions().stream().map(menteePosition -> menteePosition.getPosition().name()).collect(Collectors.toSet());
    }

    private static Set<String> getInterestedFields(Mentee mentee) {
        return mentee.getInterestedFields().stream().map(menteeField -> menteeField.getField().name()).collect(Collectors.toSet());
    }

}
