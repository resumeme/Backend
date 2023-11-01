package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProjectTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();
        resume = new Resume("title", mentee);
    }

    @Test
    void 프로젝트_이름과_년도가_없을_시_예외_발생() {
        String projectName = null;
        Long productionYear = null;
        boolean isTeam = true;
        String teamMembers = "member1, member2, member3";
        List<String> skills = new ArrayList<>();
        String projectContent = "프로젝트 설명";
        String projectUrl = "https://example.com/project";

        assertThatThrownBy(() -> new Project(resume, projectName, productionYear, isTeam, teamMembers, skills, projectContent, projectUrl))
                .isInstanceOf(CustomException.class);
    }

}
