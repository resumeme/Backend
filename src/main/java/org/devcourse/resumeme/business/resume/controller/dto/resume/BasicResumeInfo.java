package org.devcourse.resumeme.business.resume.controller.dto.resume;

import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;

import java.util.Set;

public record BasicResumeInfo(String title, String position, Set<String> skills, String introduce,
                              Long originResumeId, OwnerInfo ownerInfo) {

    public BasicResumeInfo(Resume resume, Mentee mentee) {
        this(resume.getTitle(), resume.getResumeInfo().getPosition(), resume.getResumeInfo().getSkillSet(), resume.getResumeInfo().getIntroduce(),
                resume.getOriginResumeId(), new OwnerInfo(mentee));
    }

    record OwnerInfo(Long id, String name, String phoneNumber) {

        public OwnerInfo(Mentee mentee) {
            this(mentee.getId(), mentee.getRequiredInfo().getRealName(), mentee.getRequiredInfo().getPhoneNumber());
        }

    }

}
