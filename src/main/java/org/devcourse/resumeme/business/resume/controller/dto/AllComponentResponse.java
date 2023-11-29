package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.CareerResponse;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTemplate;

import java.util.List;

@Data
@NoArgsConstructor
public class AllComponentResponse {

    private List<ActivityResponse> activities;
    private List<CareerResponse> careers;
    private List<CertificationResponse> certifications;
    private List<ForeignLanguageResponse> foreignLanguages;
    private List<ProjectResponse> projects;
    private List<TrainingResponse> trainings;
    private List<ResumeLinkResponse> links;

    public AllComponentResponse(List<ActivityResponse> activities, List<CareerResponse> careers,
            List<CertificationResponse> certifications, List<ForeignLanguageResponse> foreignLanguages,
            List<ProjectResponse> projects, List<TrainingResponse> trainings, List<ResumeLinkResponse> links) {
        this.activities = activities.isEmpty() ? null : activities;
        this.careers = careers.isEmpty() ? null : careers;
        this.certifications = certifications.isEmpty() ? null : certifications;
        this.foreignLanguages = foreignLanguages.isEmpty() ? null : foreignLanguages;
        this.projects = projects.isEmpty() ? null : projects;
        this.trainings = trainings.isEmpty() ? null : trainings;
        this.links = links.isEmpty() ? null : links;
    }


    public static AllComponentResponse from(ResumeTemplate template) {
        List<ActivityResponse> activities = template.getActivity().stream()
                .map(ActivityResponse::new)
                .toList();
        List<CareerResponse> careers = template.getCareer().stream()
                .map(CareerResponse::new)
                .toList();
        List<CertificationResponse> certifications = template.getCertification().stream()
                .map(CertificationResponse::new)
                .toList();
        List<ForeignLanguageResponse> foreignLanguages = template.getForeignLanguage().stream()
                .map(ForeignLanguageResponse::new)
                .toList();
        List<ProjectResponse> projects = template.getProject().stream()
                .map(ProjectResponse::new)
                .toList();
        List<TrainingResponse> trainings = template.getTraining().stream()
                .map(TrainingResponse::new)
                .toList();
        List<ResumeLinkResponse> links = template.getReferenceLink().stream()
                .map(ResumeLinkResponse::new)
                .toList();

        return new AllComponentResponse(activities, careers, certifications,
                foreignLanguages, projects, trainings, links);
    }

}