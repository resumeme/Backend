package org.devcourse.resumeme.business.resume.service;

import org.devcourse.resumeme.business.resume.repository.ResumeRepository;
import org.devcourse.resumeme.business.statistics.service.StatisticsService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StatisticsServiceTest {

    @Test
    public void 합격률_계산에_성공한다() {
        ResumeRepository resumeRepository = Mockito.mock(ResumeRepository.class);

        when(resumeRepository.count()).thenReturn(10L);
        when(resumeRepository.countByPassStatus(true)).thenReturn(5L);

        StatisticsService statisticsService = new StatisticsService(resumeRepository);

        double passRate = statisticsService.calculatePassRate();

        assertEquals(50.0, passRate, 0.01);
    }
}

