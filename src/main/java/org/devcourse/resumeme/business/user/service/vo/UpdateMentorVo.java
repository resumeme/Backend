package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;

import java.util.Set;

public class UpdateMentorVo implements UpdateUserVo {

    private String nickname;

    private String phoneNumber;

    private Set<String> experiencedPositions;

    private String careerContent;

    private int careerYear;

    private String introduce;

    public UpdateMentorVo(String nickname, String phoneNumber, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.experiencedPositions = experiencedPositions;
        this.careerContent = careerContent;
        this.careerYear = careerYear;
        this.introduce = introduce;
    }

    @Override
    public User update(User user) {
        Mentor mentor = Mentor.of(user);
        mentor.updateInfos(nickname, phoneNumber, experiencedPositions, careerContent, careerYear, introduce);

        return mentor.from();
    }

}
