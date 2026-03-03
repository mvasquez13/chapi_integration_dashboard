package com.chapi.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {
    private String title;
    private LocalDateTime lastUpdate;
    private List<Indicator> indicators;
    private String status; // "healthy", "warning", "critical"
}
