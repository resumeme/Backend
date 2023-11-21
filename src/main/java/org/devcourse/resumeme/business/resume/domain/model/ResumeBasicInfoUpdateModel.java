package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.ResumeInfo;

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
