package org.devcourse.resumeme.business.resume.controller.dto.career;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.domain.career.Duty;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class CareerResponse extends ComponentResponse {

    private String companyName;

    private String position;

    private List<String> skills;

    private List<DutyResponse> duties;

    private boolean currentlyEmployed;

    private LocalDate careerStartDate;

    private LocalDate endDate;

    private String careerContent;

    public CareerResponse(Career career) {
        super(career.getComponentInfo());
        this.companyName = career.getCompanyName();
        this.position = career.getPosition();
        this.skills = career.getSkills();
        this.duties = mapDuties(career.getDuties());
        this.currentlyEmployed = career.getCareerPeriod().getEndDate() == null;
        this.careerStartDate = career.getCareerPeriod().getStartDate();
        this.endDate = career.getCareerPeriod().getEndDate();
        this.careerContent = career.getCareerContent();
    }

    private static List<DutyResponse> mapDuties(List<Duty> duties) {
        return duties.stream().map(DutyResponse::new).toList();
    }

    public record DutyResponse(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String description
    ) {

        public DutyResponse(Duty duty) {
            this(
                    duty.getTitle(),
                    duty.getStartDate(),
                    duty.getEndDate(),
                    duty.getDescription()
            );
        }

    }

}
