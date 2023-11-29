package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.entity.Resume;

public class ResumeReflectFeedbackModel implements ResumeUpdateModel {

    @Override
    public void update(Resume resume) {
        resume.makeToOrigin();
    }

}
