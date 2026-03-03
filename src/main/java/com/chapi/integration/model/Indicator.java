package com.chapi.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indicator {
    private String name;
    private String value;
    private String unit;
    private String trend; // "up", "down", "stable"
    private String category;
}
