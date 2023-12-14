package org.devcourse.resumeme.business.resume.controller.dto.resume;

import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;

import java.util.Set;

public record BasicResumeInfo(String title, String position, Set<String> skills, String introduce,
                              Long originResumeId, OwnerInfo ownerInfo) {

    public BasicResumeInfo(Resume resume, UserResponse mentee) {
        this(resume.getTitle(), resume.getResumeInfo().getPosition(), resume.getResumeInfo().getSkillSet(), resume.getResumeInfo().getIntroduce(),
                resume.getOriginResumeId(), new OwnerInfo(mentee));
    }

    record OwnerInfo(Long id, String name, String phoneNumber) {

        public OwnerInfo(UserResponse mentee) {
            this(mentee.userId(), mentee.name(), mentee.phoneNumber());
        }

    }

}
