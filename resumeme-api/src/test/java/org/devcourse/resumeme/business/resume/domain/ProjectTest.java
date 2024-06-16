package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.domain.project.Project;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectTest {

    @Test
    void 프로젝트_이름과_년도가_없을_시_예외_발생() {
        String projectName = null;
        Long productionYear = null;
        String teamMembers = "member1, member2, member3";
        List<String> skills = new ArrayList<>();
        String projectContent = "프로젝트 설명";
        String projectUrl = "https://example.com/project";

        assertThatThrownBy(() -> new Project(projectName, productionYear, teamMembers, skills, projectContent, projectUrl))
                .isInstanceOf(CustomException.class);
    }

}
