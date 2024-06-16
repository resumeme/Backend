package org.devcourse.resumeme.business.statistics.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.repository.PassInfoRepository;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ResumeRepository resumeRepository;

    private final PassInfoRepository passInfoRepository;

    public double calculatePassRate() {
        long totalResumes = resumeRepository.count();

        long passedResumes = passInfoRepository.countByPassStatus(true);

        if (totalResumes == 0) {
            return 0.0;
        }

        return getFormattedPercentage(passedResumes, totalResumes);
    }

    public double calculatePassRateByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        long totalResumes = resumeRepository.countByCreatedDateBetween(startDate, endDate);
        long passedResumes = passInfoRepository.countByPassStatusIsTrueAndPassDateBetween(startDate, endDate);

        if (totalResumes == 0) {
            return 0.0;
        }

        return getFormattedPercentage(passedResumes, totalResumes);
    }

    public Double getFormattedPercentage(long passedResumes, long totalResumes) {
        double percentage = ((double) passedResumes / totalResumes) * 100;

        return Double.valueOf(String.format("%.2f", percentage));
    }

}
