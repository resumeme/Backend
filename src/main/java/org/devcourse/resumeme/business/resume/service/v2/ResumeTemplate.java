package org.devcourse.resumeme.business.resume.service.v2;

import lombok.Builder;
import lombok.Getter;
import org.devcourse.resumeme.business.resume.domain.Converter;

import java.util.List;

@Getter
public class ResumeTemplate {

    private List<Converter> activity;

    private List<Converter> career;

    private List<Converter> certification;

    private List<Converter> foreignLanguage;

    private List<Converter> project;

    private List<Converter> training;

    private List<Converter> referenceLink;

    @Builder
    public ResumeTemplate(List<Converter> activity, List<Converter> career,
            List<Converter> certification, List<Converter> foreignLanguage,
            List<Converter> project, List<Converter> training, List<Converter> referenceLink) {
        this.activity = activity;
        this.career = career;
        this.certification = certification;
        this.foreignLanguage = foreignLanguage;
        this.project = project;
        this.training = training;
        this.referenceLink = referenceLink;
    }

}
