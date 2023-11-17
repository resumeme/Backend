package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;

import java.util.Set;

public record BasicResumeInfo(String title, String position, Set<String> skills, String introduce,
                              OwnerInfo ownerInfo) {

    public BasicResumeInfo(Resume resume) {
        this(resume.getTitle(), resume.getResumeInfo().getPosition(), resume.getResumeInfo().getSkillSet(), resume.getResumeInfo().getIntroduce(),
                new OwnerInfo(resume.getMentee()));
    }

    record OwnerInfo(Long id, String name, String phoneNumber) {

        public OwnerInfo(Mentee mentee) {
            this(mentee.getId(), mentee.getRequiredInfo().getRealName(), mentee.getRequiredInfo().getPhoneNumber());
        }

    }

}
