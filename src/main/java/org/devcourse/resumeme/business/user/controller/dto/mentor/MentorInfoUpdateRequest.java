package org.devcourse.resumeme.business.user.controller.dto.mentor;

import org.devcourse.resumeme.business.user.service.vo.UpdateMentorVo;
import org.devcourse.resumeme.business.user.service.vo.UpdateUserVo;

import java.util.Set;

public record MentorInfoUpdateRequest(String nickname, String phoneNumber, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {

    public UpdateUserVo toVo() {
        return new UpdateMentorVo(nickname, phoneNumber, experiencedPositions, careerContent, careerYear, introduce);
    }

}
