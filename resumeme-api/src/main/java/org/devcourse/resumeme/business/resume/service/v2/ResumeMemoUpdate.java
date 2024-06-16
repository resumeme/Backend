package org.devcourse.resumeme.business.resume.service.v2;

import org.devcourse.resumeme.business.resume.domain.model.ResumeMemoUpdateModel;
import org.devcourse.resumeme.business.resume.domain.model.ResumeUpdateModel;

public class ResumeMemoUpdate implements ResumeUpdateVo {

    private final String memo;

    public ResumeMemoUpdate(String memo) {
        this.memo = memo;
    }

    @Override
    public ResumeUpdateModel toModel() {
        return new ResumeMemoUpdateModel(memo);
    }

}
