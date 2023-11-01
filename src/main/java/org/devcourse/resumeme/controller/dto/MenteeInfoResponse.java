package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentee.Mentee;

import java.util.Set;
import java.util.stream.Collectors;

public record MenteeInfoResponse(String imageUrl, String phoneNumber, String role, Set<String> interestedPositions,
                             Set<String> interestedFields, String introduce) {

    public MenteeInfoResponse(Mentee mentee) {
        this(mentee.getImageUrl(), mentee.getRequiredInfo().getPhoneNumber(), mentee.getRequiredInfo().getRole().toString(),
                mentee.getInterestedPositions().stream().map(menteePosition -> menteePosition.getPosition().toString()).collect(Collectors.toSet()),
                mentee.getInterestedFields().stream().map(menteeField -> menteeField.getField().toString()).collect(Collectors.toSet()),
                mentee.getIntroduce());
    }

}
