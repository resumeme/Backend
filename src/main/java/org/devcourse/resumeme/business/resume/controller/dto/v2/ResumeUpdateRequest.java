package org.devcourse.resumeme.business.resume.controller.dto.v2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.service.v2.ResumeReflectFeedback;
import org.devcourse.resumeme.business.resume.service.v2.ResumeBasicInfoUpdate;
import org.devcourse.resumeme.business.resume.service.v2.ResumeMemoUpdate;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTitleUpdate;
import org.devcourse.resumeme.business.resume.service.v2.ResumeUpdateVo;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ResumeUpdateRequest {

    private String position;

    private List<String> skills = new ArrayList<>();

    private String introduce;

    private String memo;

    private String title;

    public ResumeUpdateRequest(String position, List<String> skills, String introduce, String memo, String title) {
        this.position = position;
        this.skills = skills;
        this.introduce = introduce;
        this.memo = memo;
        this.title = title;
    }

    public ResumeUpdateVo toVo() {
        if (position == null && introduce == null && memo == null && title == null) {
            return new ResumeReflectFeedback();
        }

        if (title != null) {
            return new ResumeTitleUpdate(title);
        }

        if (memo != null) {
            return new ResumeMemoUpdate(memo);
        }

        return new ResumeBasicInfoUpdate(position, skills, introduce);
    }

}
