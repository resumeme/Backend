package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.entity.ResumeInfo;

public class ResumeBasicInfoUpdateModel implements ResumeUpdateModel {

    private ResumeInfo resumeInfo;

    public ResumeBasicInfoUpdateModel(ResumeInfo resumeInfo) {
        this.resumeInfo = resumeInfo;
    }

    @Override
    public void update(Resume resume) {
        resume.updateResumeInfo(resumeInfo);
    }

}
