package org.devcourse.resumeme.business.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.statistics.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/pass-rate")
    public String adminPage(Model model) {
        model.addAttribute("passRate", statisticsService.calculatePassRate());
        return "admin";
    }

    @GetMapping("/pass-rate-by-period")
    public String passRateByPeriod(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            Model model) {
        double passRateByPeriod = statisticsService.calculatePassRateByPeriod(startDate, endDate);

        model.addAttribute("passRateByPeriod", passRateByPeriod);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin";
    }

}
