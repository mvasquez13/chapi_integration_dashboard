package com.chapi.integration.service;

import com.chapi.integration.model.Dashboard;
import com.chapi.integration.model.Indicator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IndicatorService {

    private final Random random = new Random();

    public Dashboard getDashboardData() {
        Dashboard dashboard = new Dashboard();
        dashboard.setTitle("Chapi Integration Dashboard");
        dashboard.setLastUpdate(LocalDateTime.now());
        dashboard.setIndicators(generateIndicators());
        dashboard.setStatus(calculateOverallStatus(dashboard.getIndicators()));
        return dashboard;
    }

    private List<Indicator> generateIndicators() {
        List<Indicator> indicators = new ArrayList<>();

        // Indicadores de rendimiento
        indicators.add(new Indicator(
                "CPU Usage",
                String.valueOf(random.nextInt(100)),
                "%",
                getTrend(),
                "performance"
        ));

        indicators.add(new Indicator(
                "Memory Usage",
                String.valueOf(random.nextInt(100)),
                "%",
                getTrend(),
                "performance"
        ));

        indicators.add(new Indicator(
                "Disk Usage",
                String.valueOf(random.nextInt(100)),
                "%",
                getTrend(),
                "performance"
        ));

        // Indicadores de negocio
        indicators.add(new Indicator(
                "Active Users",
                String.valueOf(1000 + random.nextInt(5000)),
                "users",
                getTrend(),
                "business"
        ));

        indicators.add(new Indicator(
                "Requests/min",
                String.valueOf(500 + random.nextInt(2000)),
                "req/min",
                getTrend(),
                "business"
        ));

        indicators.add(new Indicator(
                "Uptime",
                "99." + (90 + random.nextInt(10)),
                "%",
                "up",
                "reliability"
        ));

        indicators.add(new Indicator(
                "Response Time",
                String.valueOf(50 + random.nextInt(200)),
                "ms",
                getTrend(),
                "performance"
        ));

        indicators.add(new Indicator(
                "Error Rate",
                "0." + random.nextInt(10),
                "%",
                getTrend(),
                "reliability"
        ));

        return indicators;
    }

    private String getTrend() {
        String[] trends = {"up", "down", "stable"};
        return trends[random.nextInt(trends.length)];
    }

    private String calculateOverallStatus(List<Indicator> indicators) {
        // Lógica simple para determinar el estado general
        long criticalCount = indicators.stream()
                .filter(i -> i.getCategory().equals("performance") && 
                        Integer.parseInt(i.getValue().split("\\.")[0]) > 80)
                .count();

        if (criticalCount > 2) {
            return "critical";
        } else if (criticalCount > 0) {
            return "warning";
        }
        return "healthy";
    }
}
