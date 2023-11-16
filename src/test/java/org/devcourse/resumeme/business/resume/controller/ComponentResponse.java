package org.devcourse.resumeme.business.resume.controller;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class ComponentResponse {

    public static ResponseFieldsSnippet activityResponseSnippet() {
        return responseFields(
                fieldWithPath("activities.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("activities.[].activityName").type(STRING).description("활동명"),
                fieldWithPath("activities.[].startDate").type(STRING).description("시작일"),
                fieldWithPath("activities.[].endDate").type(STRING).description("종료일"),
                fieldWithPath("activities.[].inProgress").type(BOOLEAN).description("진행 중 여부"),
                fieldWithPath("activities.[].link").type(STRING).description("링크"),
                fieldWithPath("activities.[].description").type(STRING).description("설명")
        );
    }

    public static ResponseFieldsSnippet careerResponseSnippet() {
        return responseFields(
                fieldWithPath("careers.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("careers.[].companyName").type(STRING).description("회사명"),
                fieldWithPath("careers.[].position").type(STRING).description("직책"),
                fieldWithPath("careers.[].skills").type(ARRAY).description("기술 목록"),
                fieldWithPath("careers.[].duties[].title").type(STRING).description("업무 제목"),
                fieldWithPath("careers.[].duties[].startDate").type(STRING).description("업무 시작일"),
                fieldWithPath("careers.[].duties[].endDate").type(STRING).description("업무 종료일"),
                fieldWithPath("careers.[].duties[].description").type(STRING).description("업무 설명"),
                fieldWithPath("careers.[].currentlyEmployed").type(BOOLEAN).description("현재 재직 상태"),
                fieldWithPath("careers.[].careerStartDate").type(STRING).description("경력 시작일"),
                fieldWithPath("careers.[].endDate").type(STRING).description("경력 종료일"),
                fieldWithPath("careers.[].careerContent").type(STRING).description("경력 상세 내용")
        );
    }

    public static ResponseFieldsSnippet certificationResponseSnippet() {
        return responseFields(
                fieldWithPath("certifications.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("certifications.[].certificationTitle").type(STRING).description("자격증 제목"),
                fieldWithPath("certifications.[].acquisitionDate").type(STRING).description("취득 일자"),
                fieldWithPath("certifications.[].issuingAuthority").type(STRING).description("발급 기관"),
                fieldWithPath("certifications.[].link").type(STRING).description("링크"),
                fieldWithPath("certifications.[].description").type(STRING).description("설명")
        );
    }

    public static ResponseFieldsSnippet foreignLanguageResponseSnippet() {
        return responseFields(
                fieldWithPath("foreign-languages.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("foreign-languages.[].language").type(STRING).description("언어"),
                fieldWithPath("foreign-languages.[].examName").type(STRING).description("시험명"),
                fieldWithPath("foreign-languages.[].scoreOrGrade").type(STRING).description("점수 또는 학점")
        );
    }

    public static ResponseFieldsSnippet linkResponseSnippet() {
        return responseFields(
                fieldWithPath("links.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("links.[].linkType").type(STRING).description(generateLinkCode(BLOCK_TYPE)),
                fieldWithPath("links.[].url").type(STRING).description("링크 URL")
        );
    }

    public static ResponseFieldsSnippet projectResponseSnippet() {
        return responseFields(
                fieldWithPath("projects.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("projects.[]projectName").type(STRING).description("프로젝트명"),
                fieldWithPath("projects.[]productionYear").type(NUMBER).description("제작 연도"),
                fieldWithPath("projects.[]team").type(BOOLEAN).description("팀 프로젝트 여부"),
                fieldWithPath("projects.[]teamMembers").type(STRING).optional().description("팀 구성원 (옵션)"),
                fieldWithPath("projects.[]skills").type(ARRAY).description("기술 목록"),
                fieldWithPath("projects.[]projectContent").type(STRING).description("프로젝트 내용"),
                fieldWithPath("projects.[]projectUrl").type(STRING).description("프로젝트 URL")
        );
    }

    public static ResponseFieldsSnippet trainingResponseSnippet() {
        return responseFields(
                fieldWithPath("trainings.[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("trainings.[].organization").type(STRING).description("기관명"),
                fieldWithPath("trainings.[].major").type(STRING).description("전공"),
                fieldWithPath("trainings.[].degree").type(STRING).description("학위"),
                fieldWithPath("trainings.[].admissionDate").type(STRING).description("입학일"),
                fieldWithPath("trainings.[].graduationDate").type(STRING).description("졸업일"),
                fieldWithPath("trainings.[].gpa").type(NUMBER).description("평점"),
                fieldWithPath("trainings.[].maxGpa").type(NUMBER).description("최고 평점"),
                fieldWithPath("trainings.[].explanation").type(STRING).description("설명")
        );
    }

}
