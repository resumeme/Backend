package org.devcourse.resumeme.business.statistics.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ResumeRepository resumeRepository; // 적절한 Repository로 변경

    public double calculatePassRate() {
        long totalResumes = resumeRepository.count();

        long passedResumes = resumeRepository.countByPassStatus(true);

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
