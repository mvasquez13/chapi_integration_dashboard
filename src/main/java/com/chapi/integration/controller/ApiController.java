package com.chapi.integration.controller;

import com.chapi.integration.model.Dashboard;
import com.chapi.integration.service.IndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private IndicatorService indicatorService;

    @GetMapping("/dashboard")
    public Dashboard getDashboard() {
        return indicatorService.getDashboardData();
    }
}
