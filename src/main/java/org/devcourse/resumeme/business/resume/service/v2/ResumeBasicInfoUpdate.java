package org.devcourse.resumeme.business.resume.service.v2;

import org.devcourse.resumeme.business.resume.entity.ResumeInfo;
import org.devcourse.resumeme.business.resume.domain.model.ResumeBasicInfoUpdateModel;
import org.devcourse.resumeme.business.resume.domain.model.ResumeUpdateModel;

import java.util.List;

public class ResumeBasicInfoUpdate implements ResumeUpdateVo {

    private String position;

    private List<String> skills;

    private String introduce;

    public ResumeBasicInfoUpdate(String position, List<String> skills, String introduce) {
        this.position = position;
        this.skills = skills;
        this.introduce = introduce;
    }

    @Override
    public ResumeUpdateModel toModel() {
        ResumeInfo resumeInfo = new ResumeInfo(position, skills, introduce);

        return new ResumeBasicInfoUpdateModel(resumeInfo);
    }

}
