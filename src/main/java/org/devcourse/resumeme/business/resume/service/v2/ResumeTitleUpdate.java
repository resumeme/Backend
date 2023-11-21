package org.devcourse.resumeme.business.resume.service.v2;

import org.devcourse.resumeme.business.resume.domain.model.ResumeTitleUpdateModel;
import org.devcourse.resumeme.business.resume.domain.model.ResumeUpdateModel;

public class ResumeTitleUpdate implements ResumeUpdateVo {

    private final String title;

    public ResumeTitleUpdate(String title) {
        this.title = title;
    }

    @Override
    public ResumeUpdateModel toModel() {
        return new ResumeTitleUpdateModel(title);
    }

}
