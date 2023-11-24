package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.domain.Resume;

public class ResumeReflectFeedbackModel implements ResumeUpdateModel {

    @Override
    public void update(Resume resume) {
        resume.makeToOrigin();
    }

}
