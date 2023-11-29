package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.domain.activity.Activity;
import org.devcourse.resumeme.business.resume.domain.certification.Certification;
import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;
import org.devcourse.resumeme.business.resume.domain.project.Project;
import org.devcourse.resumeme.business.resume.domain.link.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.training.Training;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTemplate;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.lang.reflect.Field;
import java.util.List;

import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.activityCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.careerCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.certificationCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.foreignLanguageCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.projectCreateRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.resumeLinkRequest;
import static org.devcourse.resumeme.business.resume.controller.ComponentRequest.trainingCreateRequest;
import static org.devcourse.resumeme.business.resume.domain.Property.ACTIVITIES;
import static org.devcourse.resumeme.business.resume.domain.Property.CAREERS;
import static org.devcourse.resumeme.business.resume.domain.Property.CERTIFICATIONS;
import static org.devcourse.resumeme.business.resume.domain.Property.FOREIGNLANGUAGES;
import static org.devcourse.resumeme.business.resume.domain.Property.LINKS;
import static org.devcourse.resumeme.business.resume.domain.Property.PROJECTS;
import static org.devcourse.resumeme.business.resume.domain.Property.TRAININGS;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class ComponentResponse {

    public static ResumeTemplate activityResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = activityCreateRequest().toVo().toComponent(1L);
        Component superComponent = new Component(ACTIVITIES, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<Activity> converters = Activity.of(superComponent);

        return ResumeTemplate.builder()
                .activity(converters)
                .career(List.of())
                .certification(List.of())
                .foreignLanguage(List.of())
                .project(List.of())
                .training(List.of())
                .referenceLink(List.of())
                .build();
    }

    public static ResumeTemplate careerResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = careerCreateRequest().toVo().toComponent(1L);
        Component superComponent = new Component(CAREERS, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<Career> converters = Career.of(superComponent);

        return ResumeTemplate.builder()
                .activity(List.of())
                .career(converters)
                .certification(List.of())
                .foreignLanguage(List.of())
                .project(List.of())
                .training(List.of())
                .referenceLink(List.of())
                .build();
    }

    public static ResumeTemplate certificationResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = certificationCreateRequest().toVo().toComponent(1L);
        Component superComponent = new Component(CERTIFICATIONS, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<Certification> converters = Certification.of(superComponent);

        return ResumeTemplate.builder()
                .activity(List.of())
                .career(List.of())
                .foreignLanguage(List.of())
                .project(List.of())
                .training(List.of())
                .referenceLink(List.of())
                .certification(converters)
                .build();
    }

    public static ResumeTemplate foreignLanguageResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = foreignLanguageCreateRequest().toVo().toComponent(1L);
        Component superComponent = new Component(FOREIGNLANGUAGES, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<ForeignLanguage> converters = ForeignLanguage.of(superComponent);

        return ResumeTemplate.builder()
                .activity(List.of())
                .career(List.of())
                .certification(List.of())
                .project(List.of())
                .training(List.of())
                .referenceLink(List.of())
                .foreignLanguage(converters)
                .build();
    }

    public static ResumeTemplate referenceLinkResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = resumeLinkRequest().toVo().toComponent(1L);
        Component superComponent = new Component(LINKS, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<ReferenceLink> converters = ReferenceLink.of(superComponent);

        return ResumeTemplate.builder()
                .activity(List.of())
                .career(List.of())
                .certification(List.of())
                .foreignLanguage(List.of())
                .project(List.of())
                .training(List.of())
                .referenceLink(converters)
                .build();
    }

    public static ResumeTemplate projectResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = projectCreateRequest().toVo().toComponent(1L);
        Component superComponent = new Component(PROJECTS, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<Project> converters = Project.of(superComponent);

        return ResumeTemplate.builder()
                .activity(List.of())
                .career(List.of())
                .certification(List.of())
                .foreignLanguage(List.of())
                .training(List.of())
                .referenceLink(List.of())
                .project(converters)
                .build();
    }

    public static ResumeTemplate trainingResumeTemplate() throws NoSuchFieldException, IllegalAccessException {
        Component component = trainingCreateRequest().toVo().toComponent(1L);
        Component superComponent = new Component(TRAININGS, null, null, null, 1L, List.of(component));
        Field id = component.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(component, 1L);
        Field field = component.getClass().getDeclaredField("originComponentId");
        field.setAccessible(true);
        field.set(component, 2L);
        List<Training> converters = Training.of(superComponent);

        return ResumeTemplate.builder()
                .activity(List.of())
                .career(List.of())
                .certification(List.of())
                .foreignLanguage(List.of())
                .project(List.of())
                .referenceLink(List.of())
                .training(converters)
                .build();
    }

    public static ResponseFieldsSnippet activityResponseSnippet() {
        return responseFields(
                fieldWithPath("activities.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("activities.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("activities.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
                fieldWithPath("activities.[].activityName").type(STRING).description("활동명"),
                fieldWithPath("activities.[].startDate").type(STRING).description("시작일"),
                fieldWithPath("activities.[].endDate").type(STRING).description("종료일"),
                fieldWithPath("activities.[].inProgress").type(BOOLEAN).description("진행 중 여부"),
                fieldWithPath("activities.[].link").type(STRING).description("링크"),
                fieldWithPath("activities.[].description").type(STRING).description("설명"),
                fieldWithPath("careers.").ignored(),
                fieldWithPath("certifications.").ignored(),
                fieldWithPath("foreignLanguages.").ignored(),
                fieldWithPath("links.").ignored(),
                fieldWithPath("projects.").ignored(),
                fieldWithPath("trainings.").ignored()
        );
    }

    public static ResponseFieldsSnippet careerResponseSnippet() {
        return responseFields(
                fieldWithPath("careers.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("careers.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("careers.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
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
                fieldWithPath("careers.[].careerContent").type(STRING).description("경력 상세 내용"),
                fieldWithPath("activities").ignored(),
                fieldWithPath("certifications").ignored(),
                fieldWithPath("foreignLanguages").ignored(),
                fieldWithPath("links").ignored(),
                fieldWithPath("projects").ignored(),
                fieldWithPath("trainings").ignored()
        );
    }

    public static ResponseFieldsSnippet certificationResponseSnippet() {
        return responseFields(
                fieldWithPath("certifications.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("certifications.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("certifications.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
                fieldWithPath("certifications.[].certificationTitle").type(STRING).description("자격증 제목"),
                fieldWithPath("certifications.[].acquisitionDate").type(STRING).description("취득 일자"),
                fieldWithPath("certifications.[].issuingAuthority").type(STRING).description("발급 기관"),
                fieldWithPath("certifications.[].link").type(STRING).description("링크"),
                fieldWithPath("certifications.[].description").type(STRING).description("설명"),
                fieldWithPath("activities").ignored(),
                fieldWithPath("careers").ignored(),
                fieldWithPath("foreignLanguages").ignored(),
                fieldWithPath("links").ignored(),
                fieldWithPath("projects").ignored(),
                fieldWithPath("trainings").ignored()
        );
    }

    public static ResponseFieldsSnippet foreignLanguageResponseSnippet() {
        return responseFields(
                fieldWithPath("foreignLanguages.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("foreignLanguages.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("foreignLanguages.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
                fieldWithPath("foreignLanguages.[].language").type(STRING).description("언어"),
                fieldWithPath("foreignLanguages.[].examName").type(STRING).description("시험명"),
                fieldWithPath("foreignLanguages.[].scoreOrGrade").type(STRING).description("점수 또는 학점"),
                fieldWithPath("activities").ignored(),
                fieldWithPath("careers").ignored(),
                fieldWithPath("certifications").ignored(),
                fieldWithPath("links").ignored(),
                fieldWithPath("projects").ignored(),
                fieldWithPath("trainings").ignored()
        );
    }

    public static ResponseFieldsSnippet linkResponseSnippet() {
        return responseFields(
                fieldWithPath("links.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("links.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("links.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
                fieldWithPath("links.[].linkType").type(STRING).description(generateLinkCode(BLOCK_TYPE)),
                fieldWithPath("links.[].url").type(STRING).description("링크 URL"),
                fieldWithPath("activities").ignored(),
                fieldWithPath("careers").ignored(),
                fieldWithPath("certifications").ignored(),
                fieldWithPath("foreignLanguages").ignored(),
                fieldWithPath("projects").ignored(),
                fieldWithPath("trainings").ignored()
        );
    }

    public static ResponseFieldsSnippet projectResponseSnippet() {
        return responseFields(
                fieldWithPath("projects.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("projects.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("projects.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
                fieldWithPath("projects.[]projectName").type(STRING).description("프로젝트명"),
                fieldWithPath("projects.[]productionYear").type(NUMBER).description("제작 연도"),
                fieldWithPath("projects.[]team").type(BOOLEAN).description("팀 프로젝트 여부"),
                fieldWithPath("projects.[]teamMembers").type(STRING).optional().description("팀 구성원 (옵션)"),
                fieldWithPath("projects.[]skills").type(ARRAY).description("기술 목록"),
                fieldWithPath("projects.[]projectContent").type(STRING).description("프로젝트 내용"),
                fieldWithPath("projects.[]projectUrl").type(STRING).description("프로젝트 URL"),
                fieldWithPath("activities").ignored(),
                fieldWithPath("careers").ignored(),
                fieldWithPath("certifications").ignored(),
                fieldWithPath("foreignLanguages").ignored(),
                fieldWithPath("links").ignored(),
                fieldWithPath("trainings").ignored()
        );
    }

    public static ResponseFieldsSnippet trainingResponseSnippet() {
        return responseFields(
                fieldWithPath("trainings.[].componentId").type(NUMBER).description("블럭 아이디"),
                fieldWithPath("trainings.[].originComponentId").type(NUMBER).description("원본 이력서 블럭 아이디 (Null 가능)"),
                fieldWithPath("trainings.[].reflectFeedback").type(BOOLEAN).description("피드백 반영 여부"),
                fieldWithPath("trainings.[].organization").type(STRING).description("기관명"),
                fieldWithPath("trainings.[].major").type(STRING).description("전공"),
                fieldWithPath("trainings.[].degree").type(STRING).description("학위"),
                fieldWithPath("trainings.[].admissionDate").type(STRING).description("입학일"),
                fieldWithPath("trainings.[].graduationDate").type(STRING).description("졸업일"),
                fieldWithPath("trainings.[].gpa").type(NUMBER).description("평점"),
                fieldWithPath("trainings.[].maxGpa").type(NUMBER).description("최고 평점"),
                fieldWithPath("trainings.[].explanation").type(STRING).description("설명"),
                fieldWithPath("activities").ignored(),
                fieldWithPath("careers").ignored(),
                fieldWithPath("certifications").ignored(),
                fieldWithPath("foreignLanguages").ignored(),
                fieldWithPath("links").ignored(),
                fieldWithPath("projects").ignored()
        );
    }

}
