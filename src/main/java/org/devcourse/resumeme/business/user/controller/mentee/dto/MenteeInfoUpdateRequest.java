package org.devcourse.resumeme.business.user.controller.mentee.dto;

import java.util.Set;

public record MenteeInfoUpdateRequest(String nickname, String phoneNumber, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {

}
