package org.devcourse.resumeme.business.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.statistics.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/pass-rate")
    public Double getPassRate() {
        return statisticsService.calculatePassRate();
    }

}
