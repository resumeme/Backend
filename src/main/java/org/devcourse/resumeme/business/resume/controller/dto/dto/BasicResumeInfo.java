package org.devcourse.resumeme.business.resume.controller.dto.dto;

import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;

import java.util.List;

public record BasicResumeInfo(String title, String position, List<String> skills, String introduce,
                              OwnerInfo ownerInfo) {

    public BasicResumeInfo(Resume resume) {
        this(resume.getTitle(), resume.getResumeInfo().getPosition(), resume.getResumeInfo().getSkills(), resume.getResumeInfo().getIntroduce(),
                new OwnerInfo(resume.getMentee()));
    }

    record OwnerInfo(Long id, String name, String phoneNumber) {

        public OwnerInfo(Mentee mentee) {
            this(mentee.getId(), mentee.getRequiredInfo().getRealName(), mentee.getRequiredInfo().getPhoneNumber());
        }

    }

}
