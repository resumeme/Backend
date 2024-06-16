package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.entity.Resume;

public class ResumeMemoUpdateModel implements ResumeUpdateModel {

    private String memo;

    public ResumeMemoUpdateModel(String memo) {
        this.memo = memo;
    }

    @Override
    public void update(Resume resume) {
        resume.updateMeme(memo);
    }

}
