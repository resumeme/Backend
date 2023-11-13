package org.devcourse.resumeme.business.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.statistics.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/pass-rate")
    public Double getPassRate() {
        return statisticsService.calculatePassRate();
    }

    @GetMapping("/pass-rate-by-period")
    public Double getPassRateByPeriod(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return statisticsService.calculatePassRateByPeriod(startDate, endDate);
    }

}
