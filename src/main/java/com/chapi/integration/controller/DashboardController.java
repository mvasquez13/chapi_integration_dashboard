package com.chapi.integration.controller;

import com.chapi.integration.model.Dashboard;
import com.chapi.integration.service.IndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private IndicatorService indicatorService;

    @GetMapping("/")
    public String dashboard(Model model) {
        Dashboard dashboard = indicatorService.getDashboardData();
        model.addAttribute("dashboard", dashboard);
        return "dashboard";
    }
}
