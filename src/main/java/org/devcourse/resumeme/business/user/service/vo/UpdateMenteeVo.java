package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.entity.User;

import java.util.Set;

public class UpdateMenteeVo implements UpdateUserVo {

    private String nickname;

    private String phoneNumber;

    private Set<String> interestedPositions;

    private Set<String> interestedFields;

    private String introduce;

    public UpdateMenteeVo(String nickname, String phoneNumber, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.interestedPositions = interestedPositions;
        this.interestedFields = interestedFields;
        this.introduce = introduce;
    }

    @Override
    public User update(User user) {
        Mentee mentee = Mentee.of(user);
        mentee.updateBasicInfo(nickname, phoneNumber, introduce);
        mentee.updatePositionAndFields(interestedPositions, interestedFields);

        return mentee.from();
    }

}
