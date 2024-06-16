package org.devcourse.resumeme.business.resume.service.v2;

import org.devcourse.resumeme.business.resume.domain.model.ResumeReflectFeedbackModel;
import org.devcourse.resumeme.business.resume.domain.model.ResumeUpdateModel;

public class ResumeReflectFeedback implements ResumeUpdateVo {

    @Override
    public ResumeUpdateModel toModel() {
        return new ResumeReflectFeedbackModel();
    }

}
