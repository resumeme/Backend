package org.devcourse.resumeme.business.user.controller.mentor.dto;

import java.util.Set;

public record MentorInfoUpdateRequest(String realName, String phoneNumber, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {

}
