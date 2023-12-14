package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.entity.Resume;

public class ResumeTitleUpdateModel implements ResumeUpdateModel {

    private String title;

    public ResumeTitleUpdateModel(String title) {
        this.title = title;
    }

    @Override
    public void update(Resume resume) {
        resume.updateTitle(title);
    }

}
