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
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[].activityName").type(STRING).description("활동명"),
                fieldWithPath("[].startDate").type(STRING).description("시작일"),
                fieldWithPath("[].endDate").type(STRING).description("종료일"),
                fieldWithPath("[].inProgress").type(BOOLEAN).description("진행 중 여부"),
                fieldWithPath("[].link").type(STRING).description("링크"),
                fieldWithPath("[].description").type(STRING).description("설명")
        );
    }

    public static ResponseFieldsSnippet careerResponseSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[].companyName").type(STRING).description("회사명"),
                fieldWithPath("[].position").type(STRING).description("직책"),
                fieldWithPath("[].skills").type(ARRAY).description("기술 목록"),
                fieldWithPath("[].duties[].title").type(STRING).description("업무 제목"),
                fieldWithPath("[].duties[].startDate").type(STRING).description("업무 시작일"),
                fieldWithPath("[].duties[].endDate").type(STRING).description("업무 종료일"),
                fieldWithPath("[].duties[].description").type(STRING).description("업무 설명"),
                fieldWithPath("[].currentlyEmployed").type(BOOLEAN).description("현재 재직 상태"),
                fieldWithPath("[].careerStartDate").type(STRING).description("경력 시작일"),
                fieldWithPath("[].endDate").type(STRING).description("경력 종료일"),
                fieldWithPath("[].careerContent").type(STRING).description("경력 상세 내용")
        );
    }

    public static ResponseFieldsSnippet certificationResponseSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[].certificationTitle").type(STRING).description("자격증 제목"),
                fieldWithPath("[].acquisitionDate").type(STRING).description("취득 일자"),
                fieldWithPath("[].issuingAuthority").type(STRING).description("발급 기관"),
                fieldWithPath("[].link").type(STRING).description("링크"),
                fieldWithPath("[].description").type(STRING).description("설명")
        );
    }

    public static ResponseFieldsSnippet foreignLanguageResponseSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[].language").type(STRING).description("언어"),
                fieldWithPath("[].examName").type(STRING).description("시험명"),
                fieldWithPath("[].scoreOrGrade").type(STRING).description("점수 또는 학점")
        );
    }

    public static ResponseFieldsSnippet linkResponseSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[].linkType").type(STRING).description(generateLinkCode(BLOCK_TYPE)),
                fieldWithPath("[].url").type(STRING).description("링크 URL")
        );
    }

    public static ResponseFieldsSnippet projectResponseSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[]projectName").type(STRING).description("프로젝트명"),
                fieldWithPath("[]productionYear").type(NUMBER).description("제작 연도"),
                fieldWithPath("[]team").type(BOOLEAN).description("팀 프로젝트 여부"),
                fieldWithPath("[]teamMembers").type(STRING).optional().description("팀 구성원 (옵션)"),
                fieldWithPath("[]skills").type(ARRAY).description("기술 목록"),
                fieldWithPath("[]projectContent").type(STRING).description("프로젝트 내용"),
                fieldWithPath("[]projectUrl").type(STRING).description("프로젝트 URL")
        );
    }

    public static ResponseFieldsSnippet trainingResponseSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("[].organization").type(STRING).description("기관명"),
                fieldWithPath("[].major").type(STRING).description("전공"),
                fieldWithPath("[].degree").type(STRING).description("학위"),
                fieldWithPath("[].admissionDate").type(STRING).description("입학일"),
                fieldWithPath("[].graduationDate").type(STRING).description("졸업일"),
                fieldWithPath("[].gpa").type(NUMBER).description("평점"),
                fieldWithPath("[].maxGpa").type(NUMBER).description("최고 평점"),
                fieldWithPath("[].explanation").type(STRING).description("설명")
        );
    }

}
