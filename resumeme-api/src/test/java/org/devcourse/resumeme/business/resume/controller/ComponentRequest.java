package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.dto.career.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.certification.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.activity.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.language.ForeignLanguageCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.project.ProjectCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.link.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.controller.dto.training.TrainingCreateRequest;
import org.springframework.restdocs.payload.RequestFieldsSnippet;

import java.time.LocalDate;
import java.util.List;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

public class ComponentRequest {

    public static ActivityCreateRequest activityCreateRequest() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        return new ActivityCreateRequest("활동1", startDate, endDate, false, "https://example.com", "활동 설명");
    }

    public static CareerCreateRequest careerCreateRequest() {
        return new CareerCreateRequest("company name", "BACK", List.of("java", "spring"), List.of(new CareerCreateRequest.DutyRequest("title", "description", LocalDate.now(), LocalDate.now().plusYears(1L))), false, LocalDate.now(), LocalDate.now().plusYears(1L), "content");
    }

    public static CertificationCreateRequest certificationCreateRequest() {
        return new CertificationCreateRequest("인증서", "2023-10-01", "발급기관", "https://example.com", "설명");
    }

    public static ForeignLanguageCreateRequest foreignLanguageCreateRequest() {
        return new ForeignLanguageCreateRequest("English", "TOEIC", "900");
    }

    public static ResumeLinkRequest resumeLinkRequest() {
        return new ResumeLinkRequest("GITHUB", "https://github.com");
    }

    public static ProjectCreateRequest projectCreateRequest() {
        return new ProjectCreateRequest("프로젝트", 2023L, true, "member1, member2, member3", List.of("java", "Spring"), "content", "https://example.com");
    }

    public static TrainingCreateRequest trainingCreateRequest() {
        return new TrainingCreateRequest(
                "데브대", "컴퓨터공학과", "학사 학위", LocalDate.of(2018, 3, 1),
                LocalDate.of(2022, 2, 28), 4.0, 4.5, "성적 우수"
        );
    }

    public static RequestFieldsSnippet activitySnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("activityName").type(STRING).description("활동명"),
                fieldWithPath("startDate").type(STRING).description("시작일"),
                fieldWithPath("endDate").type(STRING).description("종료일"),
                fieldWithPath("inProgress").type(BOOLEAN).description("진행 중 여부").optional(),
                fieldWithPath("link").type(STRING).description("링크").optional(),
                fieldWithPath("description").type(STRING).description("설명").optional()
        );
    }

    public static RequestFieldsSnippet careerSnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("companyName").type(STRING).description("회사명"),
                fieldWithPath("position").type(STRING).description("포지션").optional(),
                fieldWithPath("skills[]").type(ARRAY).description("List of skills").optional(),
                fieldWithPath("duties[].title").type(STRING).description("제목"),
                fieldWithPath("duties[].description").type(STRING).description("설명").optional(),
                fieldWithPath("duties[].startDate").type(STRING).description("시작일"),
                fieldWithPath("duties[].endDate").type(STRING).description("종료일"),
                fieldWithPath("currentlyEmployed").type(BOOLEAN).description("현재 근무 여부").attributes(constraints("false일 시 endDate 필수")),
                fieldWithPath("careerStartDate").type(STRING).description("경력 시작일"),
                fieldWithPath("endDate").type(STRING).description("종료일"),
                fieldWithPath("careerContent").type(STRING).description("경력 내용").optional()
        );
    }

    public static RequestFieldsSnippet certificationSnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("certificationTitle").type(STRING).description("인증서명"),
                fieldWithPath("acquisitionDate").type(STRING).description("취득일").optional(),
                fieldWithPath("issuingAuthority").type(STRING).description("발급기관").optional(),
                fieldWithPath("link").type(STRING).description("링크").optional(),
                fieldWithPath("description").type(STRING).description("설명").optional()
        );
    }

    public static RequestFieldsSnippet foreignLanguageSnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("language").type(STRING).description("언어"),
                fieldWithPath("examName").type(STRING).description("시험명"),
                fieldWithPath("scoreOrGrade").type(STRING).description("점수 또는 학점")
        );
    }

    public static RequestFieldsSnippet linkSnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("linkType").type(STRING).description(generateLinkCode(BLOCK_TYPE)),
                fieldWithPath("url").type(STRING).description("링크 주소")
        );
    }

    public static RequestFieldsSnippet projectSnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("projectName").type(STRING).description("프로젝트명"),
                fieldWithPath("productionYear").type(NUMBER).description("생산 연도"),
                fieldWithPath("team").type(BOOLEAN).description("팀 프로젝트 여부").optional().attributes(constraints("true일 시 teamMembers 필수")),
                fieldWithPath("teamMembers").type(STRING).description("팀원 목록").optional(),
                fieldWithPath("skills[]").type(ARRAY).description("기술 목록").optional(),
                fieldWithPath("projectContent").type(STRING).description("프로젝트 내용").optional(),
                fieldWithPath("projectUrl").type(STRING).description("프로젝트 URL").optional()
        );
    }

    public static RequestFieldsSnippet trainingSnippet() {
        return requestFields(
                fieldWithPath("type").type(STRING).description("서버쪽에서 동적으로 처리합니다 보내지 마세요").optional(),
                fieldWithPath("organization").type(STRING).description("교육 기관"),
                fieldWithPath("major").type(STRING).description("전공"),
                fieldWithPath("degree").type(STRING).description("학위"),
                fieldWithPath("admissionDate").type(STRING).description("입학일"),
                fieldWithPath("graduationDate").type(STRING).description("졸업일"),
                fieldWithPath("gpa").type(NUMBER).description("학점").optional(),
                fieldWithPath("maxGpa").type(NUMBER).description("최고 학점").optional().attributes(constraints("gpa보다 큰 값이어야 함")),
                fieldWithPath("explanation").type(STRING).description("설명").optional()
        );
    }

}
