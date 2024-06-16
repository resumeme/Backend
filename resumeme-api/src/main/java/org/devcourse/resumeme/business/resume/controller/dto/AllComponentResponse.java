package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.activity.ActivityResponse;
import org.devcourse.resumeme.business.resume.controller.dto.career.CareerResponse;
import org.devcourse.resumeme.business.resume.controller.dto.certification.CertificationResponse;
import org.devcourse.resumeme.business.resume.controller.dto.language.ForeignLanguageResponse;
import org.devcourse.resumeme.business.resume.controller.dto.link.ResumeLinkResponse;
import org.devcourse.resumeme.business.resume.controller.dto.project.ProjectResponse;
import org.devcourse.resumeme.business.resume.controller.dto.training.TrainingResponse;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTemplate;

import java.util.List;

@Getter
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
        this.activities = activities;
        this.careers = careers;
        this.certifications = certifications;
        this.foreignLanguages = foreignLanguages;
        this.projects = projects;
        this.trainings = trainings;
        this.links = links;
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
